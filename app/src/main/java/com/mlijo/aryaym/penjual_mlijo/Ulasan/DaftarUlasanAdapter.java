package com.mlijo.aryaym.penjual_mlijo.Ulasan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.DBModel.UlasanModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.DateFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by AryaYM on 30/10/2017.
 */

public class DaftarUlasanAdapter extends RecyclerView.Adapter<DaftarUlasanAdapter.DaftarUlasanViewHolder> {

    public Activity activity;
    public List<UlasanModel> ulasanList;
    private DatabaseReference mDatabase;

    public DaftarUlasanAdapter(Activity activity, List<UlasanModel> ulasanList){
        this.activity = activity;
        this.ulasanList = ulasanList;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public DaftarUlasanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(activity, R.layout.item_ulasan, null);
        return new DaftarUlasanViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final DaftarUlasanViewHolder holder, int position) {
        final UlasanModel ulasanModel = ulasanList.get(position);

        holder.txtWaktu.setText(DateFormatter.formatDateByYMD(ulasanModel.getWaktuUlasan()));
        mDatabase.child(Constants.KONSUMEN).child(ulasanModel.getIdKonsumen()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                    holder.txtNamaKonsumen.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                    if (konsumenModel.getDetailKonsumen().get(Constants.AVATAR) != null){
                        ImageLoader.getInstance().loadImageAvatar(activity, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), holder.imgKonsumen);
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
                if (dataSnapshot != null){
                    ProdukModel produkModel = dataSnapshot.getValue(ProdukModel.class);
                    holder.txtNamaProduk.setText(produkModel.getNamaProduk());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailUlasanActivity.class);
                intent.putExtra(Constants.ULASAN, ulasanModel);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ulasanList.size();
    }

    public class DaftarUlasanViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgKonsumen;
        public TextView txtNamaKonsumen, txtNamaProduk, txtWaktu;

        public DaftarUlasanViewHolder(View itemView) {
            super(itemView);
            imgKonsumen = (ImageView) itemView.findViewById(R.id.icon_konsumen);
            txtNamaKonsumen = (TextView) itemView.findViewById(R.id.txt_nama_konsumen);
            txtNamaProduk = (TextView) itemView.findViewById(R.id.txt_nama_produk);
            txtWaktu =  (TextView) itemView.findViewById(R.id.txt_waktu_ulasan);
        }
    }
}
