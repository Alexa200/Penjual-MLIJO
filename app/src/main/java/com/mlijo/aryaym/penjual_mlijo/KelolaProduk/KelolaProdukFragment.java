package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.MyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 12/08/2017.
 */

public class KelolaProdukFragment extends Fragment implements
        View.OnClickListener, KelolaProdukAdapter.OnProdukListener {

    @BindView(R.id.recycler_my_produk)
    RecyclerView mRecycler;
    @BindView(R.id.fab_new_produk)
    FloatingActionButton fabNewProduk;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;
   // Unbinder unbinder;

    private View rootView;

    private String penjualId;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private Query produkQuery;
    //private List<ProdukModel> produkList = new ArrayList<>();
    //private Que
   // private List<PostRefModel> postRefModels = new ArrayList<>();
    private KelolaProdukAdapter kelolaProdukAdapter;
    private MyLinearLayoutManager customLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        getActivity().setTitle(R.string.title_activity_kelola_produk);
        rootView = inflater.inflate(R.layout.fragment_kelola_produk, container, false);
        ButterKnife.bind(this, rootView);

        penjualId = BaseActivity.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //kelolaProdukAdapter = new KelolaProdukAdapter(postRefModels, getActivity());
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
        produkQuery = mFirestore.collection(Constants.PRODUK_REGULER)
                .whereEqualTo(Constants.ID_PENJUAL, BaseActivity.getUid())
                .orderBy("waktuDibuat", Query.Direction.DESCENDING) // sek error
                .limit(20);

        kelolaProdukAdapter = new KelolaProdukAdapter(produkQuery, this){
            @Override
            protected void onDataChanged(){
                if (getItemCount() == 0){
                    noItemData();
                    Log.d("nilai Data", "Current item data: " + getItemCount());
                }else {
                    showItemData();
                    Log.d("nilai Data", "Current item data: " + getItemCount());
                }
            }
        };
        defaultDataFilter();

        customLinearLayoutManager = new MyLinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(customLinearLayoutManager);
        mRecycler.setAdapter(kelolaProdukAdapter);
        fabNewProduk.setOnClickListener(this);
        return rootView;
    }

    private void showItemData() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        imgNoResult.setVisibility(View.GONE);
    }

    public void noItemData(){
        imgNoResult.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        //if (kelolaProdukAdapter != null){
            kelolaProdukAdapter.startListening();
        //}
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();

        if (kelolaProdukAdapter != null){
            kelolaProdukAdapter.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (kelolaProdukAdapter != null) {
            kelolaProdukAdapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fabNewProduk){
            Intent intent = new Intent(KelolaProdukFragment.this.getActivity(), BuatProdukActivity.class);
            startActivity(intent);
        }
    }

    private void defaultDataFilter(){
        FilterProduk.getDefault(penjualId);
    }

    @Override
    public void onProdukSelected(DocumentSnapshot produk) {
        Intent intent = new Intent(getActivity(), DetailProdukActivity.class);
        intent.putExtra(Constants.ID_PRODUK, produk.getId());

        startActivity(intent);
    }

    @Override
    public void onMenuClicked(final DocumentSnapshot produkRef) {
        final CharSequence[] dialogitem = {"edit", "hapus"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                            Intent intent = new Intent(getActivity(), UbahProdukActivity.class);
                            intent.putExtra(Constants.ID_PRODUK, produkRef.getId());
                            startActivity(intent);
                        break;
                    case 1:
                        final AlertDialog.Builder delBuilder = new AlertDialog.Builder(rootView.getContext());
                        delBuilder.setMessage(R.string.msg_hapusProduk).setCancelable(false)
                                .setPositiveButton(R.string.lbl_ya, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mFirestore.collection(Constants.PRODUK_REGULER).document(produkRef.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(rootView.getContext(), "Produk berhasil di hapus", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(R.string.lbl_tidak, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        delBuilder.create().show();
                        break;
                }
            }
        });
        builder.create().show();
    }
}
