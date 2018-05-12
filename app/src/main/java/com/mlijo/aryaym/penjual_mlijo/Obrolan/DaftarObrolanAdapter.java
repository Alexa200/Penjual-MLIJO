package com.mlijo.aryaym.penjual_mlijo.Obrolan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.InformasiKonsumen.KonsumenModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AryaYM on 24/10/2017.
 */

public class DaftarObrolanAdapter extends RecyclerView.Adapter<DaftarObrolanAdapter.DaftarObrolanViewHolder>{

    public Activity activity;
    public List<String> listUid;
    private DatabaseReference mDatabase;
    private HashMap<String, Boolean> hashForLoadFirstTime;
    private int count = 0;

    public DaftarObrolanAdapter(Activity activity, List<String> listUid){
        this.activity = activity;
        this.listUid = listUid;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        hashForLoadFirstTime = new HashMap<>();
    }

    @Override
    public DaftarObrolanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(activity, R.layout.item_daftar_obrolan, null );
        return new DaftarObrolanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DaftarObrolanViewHolder holder, int position) {
        try {
            count = 0;
            final String penerimaId = listUid.get(position);
            //load data penerima
            mDatabase.child(Constants.KONSUMEN).child(penerimaId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        final KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                        if (konsumenModel != null){
                            holder.txtNamaPenerima.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                            ImageLoader.getInstance().loadImageAvatar(activity, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), holder.imgAvatar);
                        }
                        //event click
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, ObrolanActivity.class);
                                intent.putExtra(Constants.ID_KONSUMEN, konsumenModel.getUid());
                                intent.putExtra(Constants.AVATAR, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString());
                                intent.putExtra(Constants.NAMA, konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                                activity.startActivity(intent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //load last message
            mDatabase.child(Constants.OBROLAN).child(BaseActivity.getUid()).child(penerimaId).limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        for (DataSnapshot dataObrolan : dataSnapshot.getChildren()){
                            ObrolanModel obrolanModel = dataObrolan.getValue(ObrolanModel.class);
                            //simpan data
                            count++;
                            if (count <= listUid.size()){
                                if (obrolanModel != null){
                                    holder.txtWaktu.setText(caculateTimeAgo(obrolanModel.getTimestamp()));
                                    if (obrolanModel.isKontenPengirim()){
                                        if (obrolanModel.isKontenFoto()){
                                            holder.txtKonten.setText(String.format("%s %s", "Anda", "mengirimkan foto"));
                                        }else {
                                            holder.txtKonten.setText(String.format("%s %s", "Anda:", obrolanModel.getKonten()));
                                        }
                                    }else {
                                        if (obrolanModel.isKontenFoto()){
                                            holder.txtKonten.setText(String.format("%s", "mengirimkan foto untuk Anda"));
                                        }else {
                                            holder.txtKonten.setText(String.format("%s", obrolanModel.getKonten()));
                                        }
                                    }
                                }
                            }else {
                                String key = "";
                                if (obrolanModel.isKontenPengirim()){
                                    key = obrolanModel.getIdPenerima();
                                }else {
                                    key = obrolanModel.getIdPengirim();
                                }
                                updateObrolanTerakhir(key);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
    }

    private void updateObrolanTerakhir(String key){
        int pos = getPosition(key);
        if (pos >= 0){
            listUid.remove(pos);
            listUid.add(0, key);
            notifyDataSetChanged();
        } else {
            listUid.add(0, key);
            notifyDataSetChanged();
        }
    }

    private int getPosition(String key){
        int i = 0;
        for (String item : listUid) {
            if (item.equals(key)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private String caculateTimeAgo(long timestamp) {
        long currentDate = new Date().getTime();
        long realTime = (currentDate - timestamp) / 1000;
        if (realTime < 60) {
            return "Saat ini";
        } else if (realTime < 3600) {
            long minutes = (long) Math.floor(realTime / 60);
            return String.format("%d menit yang lalu", minutes);
        } else if (realTime < 3600 * 24) {
            long hours = (long) Math.floor(realTime / 3600);
            return String.format("%d jam yang lalu", hours);
        } else {
            return new SimpleDateFormat("dd/MM").format(new Date(timestamp));
        }
    }

    @Override
    public int getItemCount() {
        return listUid.size();
    }

    public class DaftarObrolanViewHolder extends RecyclerView.ViewHolder{

    public ImageView imgAvatar;
    public TextView txtNamaPenerima, txtKonten, txtWaktu;

        public DaftarObrolanViewHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_obrolan_avatar);
            txtNamaPenerima = itemView.findViewById(R.id.txt_nama_penerima);
            txtKonten = itemView.findViewById(R.id.txt_obrolan_konten);
            txtWaktu = itemView.findViewById(R.id.txt_waktu_obrolan);
        }
    }
}
