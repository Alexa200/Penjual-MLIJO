package com.mlijo.aryaym.penjual_mlijo.Obrolan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 24/10/2017.
 */

public class DaftarObrolanFragment extends Fragment implements ChildEventListener{

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;
    Unbinder unbinder;
    private View view;
    private RecyclerView mRecycler;
    private List<String> listUid;
    private DaftarObrolanAdapter daftarObrolanAdapter;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daftar_obrolan, container, false);
        getActivity().setTitle(R.string.title_activity_obrolan);
        mRecycler = view.findViewById(R.id.recycler_chat);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listUid = new ArrayList<>();
        daftarObrolanAdapter = new DaftarObrolanAdapter(getActivity(), listUid);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (InternetConnection.getInstance().isOnline(getActivity())) {
                getSemueObrolan().addChildEventListener(this);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            ShowAlertDialog.showAlert(getActivity().getResources().getString(R.string.msg_retry), getActivity());
            progressBar.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(daftarObrolanAdapter);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void showItemData() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
    }

    private Query getSemueObrolan() {
        return mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.OBROLAN).orderByChild(Constants.TIMESTAMP);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        try {
            if (dataSnapshot != null) {
                if (!listUid.contains(dataSnapshot.getKey())) {
                    listUid.add(dataSnapshot.getKey());
                    daftarObrolanAdapter.notifyDataSetChanged();
                }
            }
            showItemData();
        } catch (Exception e) {

        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        try {
            if (dataSnapshot != null) {
                listUid.clear();
                daftarObrolanAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
