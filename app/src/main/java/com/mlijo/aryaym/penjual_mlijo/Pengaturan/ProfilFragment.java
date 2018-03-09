package com.mlijo.aryaym.penjual_mlijo.Pengaturan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.DBModel.PenjualModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AryaYM on 10/09/2017.
 */

public class ProfilFragment extends Fragment {

    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.txt_header_name)
    TextView txtHeaderName;
    @BindView(R.id.btn_pengaturan)
    Button btnPengaturan;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_nomor_telpon)
    TextView txtNomorTelp;
    @BindView(R.id.txt_alamat_lengkap)
    TextView txtAlamat;
    Unbinder unbinder;
    @BindView(R.id.rb_kualitas_produk)
    RatingBar rbKualitasProduk;
    @BindView(R.id.rb_kualitas_pelayanan)
    RatingBar rbKualitasPelayanan;

    private PenjualModel penjualModel;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_activity_profil);
        final View view = inflater.inflate(R.layout.fragment_profil, container, false);
        unbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadData();
        getRatingPenjual();
        btnPengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PengaturanFragment pengaturanFragment = new PengaturanFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, pengaturanFragment).commit();
            }
        });
        return view;

    }

    private void loadData() {
        mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                if (penjualModel != null) {
                    try {
                        txtHeaderName.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                        txtEmail.setText(penjualModel.getEmail());
                        txtNomorTelp.setText(penjualModel.getDetailPenjual().get(Constants.TELPON).toString());
                        txtAlamat.setText(penjualModel.getDetailPenjual().get(Constants.ALAMAT).toString());
                        ImageLoader.getInstance().loadImageAvatar(ProfilFragment.this.getActivity(), penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), imgAvatar);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getRatingPenjual() {
        mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.ULASAN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float totalRatingProduk = 0, totalRatingPelayanan = 0;
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    float ratingProduk = data.child(Constants.RATING_PRODUK).getValue(float.class);
                    totalRatingProduk = totalRatingProduk + ratingProduk;

                    float ratingPelayanan = data.child(Constants.RATING_PELAYANAN).getValue(float.class);
                    totalRatingPelayanan = totalRatingPelayanan + ratingPelayanan;
                    count = count + 1;
                }
                rbKualitasProduk.setRating(totalRatingProduk / count);
                rbKualitasPelayanan.setRating(totalRatingPelayanan / count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
