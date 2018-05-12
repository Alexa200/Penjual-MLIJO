package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.FirestoreAdapter;
import com.mlijo.aryaym.penjual_mlijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 13/08/2017.
 */

public class KelolaProdukAdapter extends FirestoreAdapter<KelolaProdukAdapter.DaftarProdukVH> {



    public interface OnProdukListener {
        void onProdukSelected(DocumentSnapshot produk);
        void onMenuClicked(DocumentSnapshot produkRef);
    }

    private OnProdukListener mListener;

    public KelolaProdukAdapter(Query query, OnProdukListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public DaftarProdukVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DaftarProdukVH(inflater.inflate(R.layout.item_produk, parent, false));
    }

    @Override
    public void onBindViewHolder(DaftarProdukVH holder, int position) {
        holder.bindData(getSnapshot(position), mListener);
    }

    static class DaftarProdukVH extends RecyclerView.ViewHolder {

        @BindView(R.id.img_icon_produk)
        ImageView imgIconProduk;
        @BindView(R.id.txt_nama_produk)
        TextView txtNamaProduk;
        @BindView(R.id.txt_nama_kategori)
        TextView txtNamaKategori;
        @BindView(R.id.txt_harga_produk)
        TextView txtHargaProduk;
        @BindView(R.id.menu_kelola_produk)
        ImageButton menuKelolaProduk;

        public DaftarProdukVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bindData(final DocumentSnapshot snapshot,
                             final OnProdukListener listener){
            ProdukModel produkModel = snapshot.toObject(ProdukModel.class);

            txtNamaProduk.setText(produkModel.getNamaProduk());
            txtNamaKategori.setText(produkModel.getKategoriProduk());
            txtHargaProduk.setText("Rp  " + BaseActivity.rupiah().format(produkModel.getHargaProduk()));
            if (produkModel.getGambarProduk() != null){
                ImageLoader.getInstance().loadImageProduk(imgIconProduk.getContext(), produkModel.getGambarProduk().get(0), imgIconProduk);
            }

            //onClick
                menuKelolaProduk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (listener != null){
                            listener.onMenuClicked(snapshot);
                        }
                    }
                });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onProdukSelected(snapshot);
                    }
                }
            });

        }
    }
}
