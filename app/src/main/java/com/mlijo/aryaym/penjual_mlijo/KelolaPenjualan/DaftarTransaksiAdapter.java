package com.mlijo.aryaym.penjual_mlijo.KelolaPenjualan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.InformasiKonsumen.KonsumenModel;
import com.mlijo.aryaym.penjual_mlijo.KelolaProduk.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.DateFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AryaYM on 20/09/2017.
 */

public class DaftarTransaksiAdapter extends RecyclerView.Adapter<DaftarTransaksiAdapter.DaftarTransaksiVH> {

    private Activity activity;
    private List<TransaksiModel> listTransaksi;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;

    public DaftarTransaksiAdapter(Activity activity, List<TransaksiModel> listTransaksi) {
        this.activity = activity;
        this.listTransaksi = listTransaksi;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public DaftarTransaksiVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(activity, R.layout.item_transaksi, null);
        return new DaftarTransaksiVH(rootView);
    }

    @Override
    public void onBindViewHolder(final DaftarTransaksiVH holder, int position) {
        final TransaksiModel transaksiModel = listTransaksi.get(position);

        holder.txtTanggalTransaksi.setText(DateFormatter.formatDateByYMD(transaksiModel.getTanggalPesan()));
        holder.txtJumlahProduk.setText("Jumlah :" + " " + String.valueOf(transaksiModel.getJumlahProduk()));
        holder.txtTanggalKirim.setText(transaksiModel.getTanggalKirim());
        holder.txtWaktuKirim.setText(transaksiModel.getWaktuKirim());
        if (transaksiModel.getStatusTransaksi() == 1) {
            holder.txtStatusTransaksi.setTextColor(Color.RED);
            holder.txtStatusTransaksi.setText(Constants.MENUNGGU);
        } else if (transaksiModel.getStatusTransaksi() == 2) {
            holder.txtStatusTransaksi.setTextColor(Color.GREEN);
            holder.txtStatusTransaksi.setText(Constants.DIPROSES);
        } else if (transaksiModel.getStatusTransaksi() == 3) {
            holder.txtStatusTransaksi.setTextColor(Color.GREEN);
            holder.txtStatusTransaksi.setText(Constants.DIKIRIM);
        } else if (transaksiModel.getStatusTransaksi() == 4) {
            holder.txtStatusTransaksi.setTextColor(Color.GREEN);
            holder.txtStatusTransaksi.setText(Constants.TERKIRIM);
        } else if (transaksiModel.getStatusTransaksi() == 5) {
            holder.txtStatusTransaksi.setTextColor(Color.RED);
            holder.txtStatusTransaksi.setText(Constants.DITOLAK);
        } else if (transaksiModel.getStatusTransaksi() == 6) {
            holder.txtStatusTransaksi.setTextColor(Color.RED);
            holder.txtStatusTransaksi.setText(Constants.DIBATALKAN);
        } else if (transaksiModel.getStatusTransaksi() == 7) {
            holder.txtStatusTransaksi.setTextColor(Color.GREEN);
            holder.txtStatusTransaksi.setText(Constants.DITERIMA);
        }

        mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                if (konsumenModel != null) {
                    holder.txtAlamatKonsumen.setText(konsumenModel.getDetailKonsumen().get(Constants.ALAMAT).toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Log.d("nilai adaptor t", ""+ transaksiModel.getTipeTransaksi());
        mFirestore.collection(transaksiModel.getJenisProduk()).document(transaksiModel.getIdProduk())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException ex) {
                if (snapshot != null){
                    ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
                    Log.d("nilai firestore", ""+ produkModel.getNamaProduk());
                    if (produkModel != null) {
                        try {
                            holder.txtNamaProduk.setText(produkModel.getNamaProduk());
                            ImageLoader.getInstance().loadImageAvatar(activity, produkModel.getGambarProduk().get(0), holder.imgProduk);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailTransaksiActivity.class);
                intent.putExtra(Constants.TRANSAKSI, transaksiModel);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    static class DaftarTransaksiVH extends RecyclerView.ViewHolder {

        @BindView(R.id.img_produk)
        CircleImageView imgProduk;
        @BindView(R.id.txt_tanggal_transaksi)
        TextView txtTanggalTransaksi;
        @BindView(R.id.txt_nama_produk)
        TextView txtNamaProduk;
        @BindView(R.id.txt_jumlah_produk)
        TextView txtJumlahProduk;
        @BindView(R.id.txt_alamat_konsumen)
        TextView txtAlamatKonsumen;
        @BindView(R.id.txt_tanggal_kirim)
        TextView txtTanggalKirim;
        @BindView(R.id.txt_waktu_kirim)
        TextView txtWaktuKirim;
        @BindView(R.id.txt_status_transaksi)
        TextView txtStatusTransaksi;

        public DaftarTransaksiVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}