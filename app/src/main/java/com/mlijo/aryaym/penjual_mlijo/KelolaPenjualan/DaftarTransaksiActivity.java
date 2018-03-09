package com.mlijo.aryaym.penjual_mlijo.KelolaPenjualan;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.DBModel.TransaksiModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaftarTransaksiActivity extends BaseActivity {

    String title, jenisTransaksi;
    @BindView(R.id.recycler_list_transaksi)
    RecyclerView mRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;

    public List<TransaksiModel> transaksiList = new ArrayList<>();
    public DaftarTransaksiAdapter daftarTransaksiAdapter;
    private DaftarTransaksiPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_transaksi);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra(Constants.TITLE);
        jenisTransaksi = getIntent().getStringExtra(Constants.TRANSAKSI);

        daftarTransaksiAdapter = new DaftarTransaksiAdapter(this, transaksiList);
        presenter = new DaftarTransaksiPresenter(this);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(daftarTransaksiAdapter);
    }

    public void showItemData(TransaksiModel transaksiModel) {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        if (transaksiList.contains(transaksiModel)){
            transaksiList.clear();
        }else {
            transaksiList.add(transaksiModel);
            daftarTransaksiAdapter.notifyDataSetChanged();
        }
    }
    public void hideItemData() {
        progressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        imgNoResult.setVisibility(View.GONE);
        if (transaksiList != null){
            transaksiList.clear();
        }
    }
    public void noItemData(){
        imgNoResult.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void onStart(){
        super.onStart();
        presenter.getDataTransaksi();
    }
}