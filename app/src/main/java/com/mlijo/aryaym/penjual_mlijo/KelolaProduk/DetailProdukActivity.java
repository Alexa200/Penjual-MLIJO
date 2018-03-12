package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailProdukActivity extends BaseActivity implements EventListener<DocumentSnapshot> {


    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.nama_produk_view)
    TextView namaProdukView;
    @BindView(R.id.harga_produk_view)
    TextView hargaProdukView;
    @BindView(R.id.kategori_produk_view)
    TextView kategoriProdukView;
    @BindView(R.id.satuan_produk_view)
    TextView satuanProdukView;
    @BindView(R.id.detail_produk_view)
    TextView detailProdukView;

    private FirebaseFirestore mFirestore;
    private DocumentReference mProdukRef;
    private ListenerRegistration mProdukReg;
    private AdapterImagePager mViewImagePager;
    private String produkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        produkId = getIntent().getExtras().getString(Constants.ID_PRODUK);
        mFirestore = FirebaseFirestore.getInstance();
        mProdukRef = mFirestore.collection("produk_reguler").document(produkId);

    }

    @Override
    public void onStart() {
        super.onStart();
        mProdukReg = mProdukRef.addSnapshotListener(this);
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("detail", "restaurant:onEvent", e);
            return;
        }
        if (snapshot.exists()) {
            onProductLoaded(snapshot.toObject(ProdukModel.class));
        }
    }

    private void onProductLoaded(ProdukModel produkModel) {
        Log.d("nilai model", "" + produkModel);
        try {
            setTitle(produkModel.getNamaProduk());
            namaProdukView.setText(produkModel.getNamaProduk());
            kategoriProdukView.setText(produkModel.getKategoriProduk());
            hargaProdukView.setText("Rp." + rupiah().format(produkModel.getHargaProduk()));
            satuanProdukView.setText(produkModel.getSatuanProduk() + " " + produkModel.getNamaSatuan());
            detailProdukView.setText(produkModel.getDeskripsiProduk());
            //txtWaktuUpdate.setText("Update terakhir : " + DateFormat.getDateTimeInstance().format(produkModel.getWaktuDibuat()));

            if (produkModel.getGambarProduk().size() > 0) {
                imgProduk.setVisibility(View.GONE);
                mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
                viewPager.setAdapter(mViewImagePager);
            } else {
                viewPager.setVisibility(View.GONE);
                imgProduk.setImageResource(R.drawable.no_image);
            }
            mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
            viewPager.setAdapter(mViewImagePager);
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.btn_ubah_produk)
    public void onViewClicked() {
        Intent intent = new Intent(this, UbahProdukActivity.class);
        intent.putExtra(Constants.ID_PRODUK, produkId);
        startActivity(intent);
    }
}
