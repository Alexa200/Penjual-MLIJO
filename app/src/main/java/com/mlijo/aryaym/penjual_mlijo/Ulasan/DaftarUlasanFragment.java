package com.mlijo.aryaym.penjual_mlijo.Ulasan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.DBModel.UlasanModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 30/10/2017.
 */

public class DaftarUlasanFragment extends Fragment {

    @BindView(R.id.recycler_list_ulasan)
    RecyclerView mRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;
    Unbinder unbinder;

    private DatabaseReference mDatabase;
    private List<UlasanModel> ulasanList = new ArrayList<>();
    private DaftarUlasanAdapter daftarUlasanAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daftar_ulasan, container, false);
        getActivity().setTitle(R.string.title_activity_ulasan);
        unbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadDataPenjual();
        daftarUlasanAdapter = new DaftarUlasanAdapter(this.getActivity(), ulasanList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecycler.setAdapter(daftarUlasanAdapter);
        return view;
    }

    private void showItemData() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        imgNoResult.setVisibility(View.GONE);
    }

    private void hideItemData() {
        progressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        imgNoResult.setVisibility(View.GONE);
    }
    private void tidakAdaUlasan() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
        imgNoResult.setVisibility(View.VISIBLE);
    }

    private Query getListUlasan(){
        Query query = mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.ULASAN);
        return query;
    }

    private void loadDataPenjual(){
        try {
            getListUlasan().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        hideItemData();
                        getListUlasan().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        UlasanModel ulasanModel = dataSnapshot.getValue(UlasanModel.class);
                                        if (!ulasanList.contains(ulasanModel)){
                                            ulasanList.add(ulasanModel);
                                            daftarUlasanAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    showItemData();
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else {
                        tidakAdaUlasan();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
