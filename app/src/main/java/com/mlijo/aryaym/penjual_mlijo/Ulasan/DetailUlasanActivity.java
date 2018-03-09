package com.mlijo.aryaym.penjual_mlijo.Ulasan;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.DateFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.DBModel.UlasanModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailUlasanActivity extends BaseActivity {

    @BindView(R.id.img_konsumen)
    ImageView imgKonsumen;
    @BindView(R.id.txt_nama_konsumen)
    TextView txtNamaKonsumen;
    @BindView(R.id.img_produk)
    ImageView imgProduk;
    @BindView(R.id.txt_nama_produk)
    TextView txtNamaProduk;
    @BindView(R.id.txt_waktu_ulasan)
    TextView txtWaktuUlasan;
    @BindView(R.id.rb_kualitas_produk)
    RatingBar rbKualitasProduk;
    @BindView(R.id.rb_kualitas_pelayanan)
    RatingBar rbKualitasPelayanan;
    @BindView(R.id.txt_ulasan)
    TextView txtUlasan;

    private DatabaseReference mDatabase;
    private UlasanModel ulasanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ulasan);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_ulasan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ulasanModel = (UlasanModel) getIntent().getSerializableExtra(Constants.ULASAN);
        loadDataUlasan();
    }

    private void loadDataUlasan() {
        txtWaktuUlasan.setText(DateFormatter.formatDateByYMD(ulasanModel.getWaktuUlasan()));
        txtUlasan.setText(ulasanModel.getTextUlasan());
        rbKualitasProduk.setRating(ulasanModel.getRatingProduk());
        rbKualitasPelayanan.setRating(ulasanModel.getRatingPelayanan());

        mDatabase.child(Constants.KONSUMEN).child(ulasanModel.getIdKonsumen()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                    txtNamaKonsumen.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                    if (konsumenModel.getDetailKonsumen().get(Constants.AVATAR) != null) {
                        ImageLoader.getInstance().loadImageAvatar(DetailUlasanActivity.this, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), imgKonsumen);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(ulasanModel.getTipeTransaksi()).child(ulasanModel.getIdKategori()).child(ulasanModel.getIdProduk()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ProdukModel produkModel = dataSnapshot.getValue(ProdukModel.class);
                    txtNamaProduk.setText(produkModel.getNamaProduk());
                    if (produkModel.getGambarProduk() != null){
                        ImageLoader.getInstance().loadImageOther(DetailUlasanActivity.this, produkModel.getGambarProduk().get(0), imgProduk);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
