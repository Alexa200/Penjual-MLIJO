package com.mlijo.aryaym.penjual_mlijo.KelolaPenjualan;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity.getUid;

/**
 * Created by AryaYM on 06/03/2018.
 */

public class DaftarTransaksiPresenter {

    private DaftarTransaksiActivity view;
    private DatabaseReference mDatabase;
    private Query mQuery;
    private List<TransaksiModel> transaksiList = new ArrayList<>();
    private DaftarTransaksiAdapter daftarTransaksiAdapter;
    private String jt;

    DaftarTransaksiPresenter(DaftarTransaksiActivity view){
        this.view = view;
        daftarTransaksiAdapter = new DaftarTransaksiAdapter(view, transaksiList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        jt  = view.jenisTransaksi;
        mQuery = mDatabase.child(Constants.PENJUAL).child(getUid())
                .child(Constants.DAFTAR_TRANSAKSI).child(jt);
    }

    public void getDataTransaksi(){
        try {
            mQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        view.hideItemData();
                        mQuery.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        Log.d("nilai model t", ""+ dataSnapshot);
                                        TransaksiModel transaksiModel = dataSnapshot.getValue(TransaksiModel.class);
                                        view.showItemData(transaksiModel);
                                    }
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        Log.d("nilai model t", ""+ dataSnapshot);
                                        view.transaksiList.clear();
                                        view.daftarTransaksiAdapter.notifyDataSetChanged();
                                    }

                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot != null){
                                        Log.d("nilai model t", ""+ dataSnapshot);
                                        view.transaksiList.clear();
                                        view.daftarTransaksiAdapter.notifyDataSetChanged();
                                    }

                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        Log.d("nilai model t", ""+ dataSnapshot);
                                        view.transaksiList.clear();
                                        view.daftarTransaksiAdapter.notifyDataSetChanged();
                                    }

                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else {
                        view.noItemData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
    }
}
