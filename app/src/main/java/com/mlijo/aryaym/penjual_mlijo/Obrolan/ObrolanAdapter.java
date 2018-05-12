package com.mlijo.aryaym.penjual_mlijo.Obrolan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AryaYM on 24/10/2017.
 */

public class ObrolanAdapter extends RecyclerView.Adapter<ObrolanAdapter.ObrolanViewHolder>{

    private Activity activity;
    private List<ObrolanModel> obrolanList;
    private String avatarPenerima;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    public ObrolanAdapter(Activity activity, List<ObrolanModel> obrolanList, String avatarPenerima){
        this.activity = activity;
        this.obrolanList = obrolanList;
        this.avatarPenerima = avatarPenerima;
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        //init imageLoader
        configImageLoader(activity);
    }

    @Override
    public ObrolanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(activity, R.layout.item_obrolan, null);
        return new ObrolanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ObrolanViewHolder holder, int position) {
        ObrolanModel obrolanModel = obrolanList.get(position);
        holder.setItem(obrolanModel);
        if (obrolanModel.isKontenPengirim()){
            tampilkanPesanMilikku(holder, obrolanModel);
        }else {
            tampilkanPesanPenerima(holder, obrolanModel, position);
        }
//        Log.d("pemilik konten", ""+obrolanModel.isKontenPengirim());
//        Log.d("id penerima", ""+obrolanModel.getIdPenerima());
//        Log.d("id pengirim", ""+obrolanModel.getIdPengirim());
//        Log.d("isi konten", ""+obrolanModel.getKonten());
    }

    private void tampilkanPesanPenerima(ObrolanViewHolder holder, ObrolanModel obrolanModel, int position) {
        //show avatar
        ImageLoader.getInstance().loadImageAvatar(activity, avatarPenerima, holder.avatarPenerima);
        //hide view milikku(pengirim)
        holder.txtPengirimKonten.setVisibility(View.GONE);
        holder.imgPengirim.setVisibility(View.GONE);
        holder.rlPengirim.setVisibility(View.GONE);
        //show view penerima
        holder.rlPenerima.setVisibility(View.VISIBLE);
        holder.txtPenerimaKonten.setVisibility(View.VISIBLE);
        holder.txtPenerimaKonten.setText("bejo");
        holder.imgPenerima.setVisibility(View.VISIBLE);
        holder.avatarPenerima.setVisibility(View.VISIBLE);

        holder.txtPenerimaStatus.setVisibility(View.VISIBLE);
        holder.txtPenerimaStatus.setText(caculateTimeAgo(obrolanModel.getTimestamp()));

        //handle data foto
        if (obrolanModel.isKontenFoto()){
            holder.txtPenerimaKonten.setVisibility(View.GONE);
            //holder.icRightArrow.setVisibility(View.GONE);
            holder.imgPenerima.setVisibility(View.VISIBLE);
            //load image
           // holder.imgPengirim.layout(0, 0, 0, 0);
            //  ImageLoader.getInstance().loadImageChat(activity, message.getContent(), holder.imgMineImage);
            imageLoader.displayImage(obrolanModel.getKonten(), holder.imgPenerima);
        } else {
            holder.txtPenerimaKonten.setVisibility(View.VISIBLE);
            //holder.icRightArrow.setVisibility(View.VISIBLE);
            holder.imgPenerima.setVisibility(View.GONE);
            //set text
            holder.txtPenerimaKonten.setText(obrolanModel.getKonten());
        }

    }

    private void tampilkanPesanMilikku(ObrolanViewHolder holder, ObrolanModel obrolanModel) {
        //show view milikku(pengirim)
        holder.txtPengirimKonten.setVisibility(View.VISIBLE);
        holder.imgPengirim.setVisibility(View.VISIBLE);
        holder.rlPengirim.setVisibility(View.VISIBLE);
        //hide view penerima
        holder.rlPenerima.setVisibility(View.GONE);
        holder.txtPenerimaKonten.setVisibility(View.GONE);
        holder.imgPenerima.setVisibility(View.GONE);
        holder.avatarPenerima.setVisibility(View.GONE);

        //check show status
//        if (obrolanModel.isDisplayStatus()){
//
//        }
        holder.txtPengirimStatus.setVisibility(View.VISIBLE);
        holder.txtPengirimStatus.setText(caculateTimeAgo(obrolanModel.getTimestamp()));
        //handle data foto
        if (obrolanModel.isKontenFoto()){
            holder.txtPengirimKonten.setVisibility(View.GONE);
            //holder.icRightArrow.setVisibility(View.GONE);
            holder.imgPengirim.setVisibility(View.VISIBLE);
            //load image
            holder.imgPengirim.layout(0, 0, 0, 0);
            //  ImageLoader.getInstance().loadImageChat(activity, message.getContent(), holder.imgMineImage);
            imageLoader.displayImage(obrolanModel.getKonten(), holder.imgPengirim);
        } else {
            holder.txtPengirimKonten.setVisibility(View.VISIBLE);
            //holder.icRightArrow.setVisibility(View.VISIBLE);
            holder.imgPengirim.setVisibility(View.GONE);
            //set text
            holder.txtPengirimKonten.setText(obrolanModel.getKonten());
        }
    }

    private void configImageLoader(Activity activity) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnFail(R.drawable.no_image)
                .showImageOnLoading(R.drawable.no_image)
                .build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(activity.getApplicationContext())
                .defaultDisplayImageOptions(displayImageOptions)
                .memoryCache(new WeakMemoryCache()).build();
        imageLoader.init(imageLoaderConfiguration);

    }

    private String caculateTimeAgo(long timestamp) {
        long currentDate = new Date().getTime();
        long realTime = (currentDate - timestamp) / 1000;
        if (realTime < 3600 * 24) {
            return new SimpleDateFormat("HH:mm").format(new Date(timestamp));
        } else {
            return new SimpleDateFormat("dd/MM HH:mm").format(new Date(timestamp));
        }
    }

    @Override
    public int getItemCount() {
        return obrolanList.size();
    }

    public class ObrolanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtPenerimaKonten, txtPengirimKonten, txtPenerimaStatus, txtPengirimStatus;
        ImageView imgPenerima, imgPengirim;
        CircleImageView avatarPenerima;
        RelativeLayout rlPenerima, rlPengirim;
        ObrolanModel obrolanModel;

        public ObrolanViewHolder(View itemView){
            super(itemView);
            txtPenerimaKonten = itemView.findViewById(R.id.txt_penerima_konten_chat);
            txtPengirimKonten = itemView.findViewById(R.id.txt_pengirim_konten_chat);
            imgPenerima = itemView.findViewById(R.id.img_penerima_image);
            imgPengirim = itemView.findViewById(R.id.img_pengirim_image);
            avatarPenerima = itemView.findViewById(R.id.img_avatar_penerima);
            rlPenerima = itemView.findViewById(R.id.rl_penerima_chat);
            rlPengirim = itemView.findViewById(R.id.rl_pengirim_chat);
            txtPenerimaStatus = itemView.findViewById(R.id.txt_penerima_status);
            txtPengirimStatus = itemView.findViewById(R.id.txt_pengirim_status);
            //event click
            imgPenerima.setOnClickListener(this);
            imgPengirim.setOnClickListener(this);
        }

        public void setItem(ObrolanModel obrolanModel){
            this.obrolanModel = obrolanModel;
        }

        @Override
        public void onClick(View v) {
            if (v == imgPenerima || v == imgPengirim){
                Intent intent = new Intent(activity, TampilGambarObrolanActivity.class);
                intent.putExtra(Constants.IMAGES, obrolanModel.getKonten());
                activity.startActivity(intent);
            }
        }
    }
}
