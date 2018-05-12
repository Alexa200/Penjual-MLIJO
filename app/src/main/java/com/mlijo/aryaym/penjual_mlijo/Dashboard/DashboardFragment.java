package com.mlijo.aryaym.penjual_mlijo.Dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Pengaturan.PenjualModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 04/07/2017.
 */

public class DashboardFragment extends Fragment
        implements ValueEventListener, CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.switchStatus)
    Switch switchStatus;
    @BindView(R.id.txt_ucapan)
    TextView txtUcapan;
    Unbinder unbinder;

    private DatabaseReference mDatabase, penjualRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        penjualRef = mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid());
        penjualRef.addValueEventListener(this);

        switchStatus.setOnCheckedChangeListener(this);
        return view;
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
            try {
                if (!penjualModel.isStatusBerjualan()) {
                    switchStatus.setChecked(false);
                } else if (penjualModel.isStatusBerjualan()) {
                    switchStatus.setChecked(true);
                }
            }catch (Exception e){

            }
        }
        // setStatusTeks();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            txtStatus.setText("Aktif");
            txtStatus.setTextColor(Color.GREEN);
            txtUcapan.setText("Selamat Bekerja");
            txtUcapan.setTextColor(Color.GREEN);
            mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_BERJUALAN).setValue(true);
        }else if (!isChecked){
            txtStatus.setText("Non-Aktif");
            txtStatus.setTextColor(Color.RED);
            txtUcapan.setText("Selamat Berlibur");
            txtUcapan.setTextColor(Color.RED);
            mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_BERJUALAN).setValue(false);
            mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_LOKASI).setValue(false);
        }
    }

    private void setStatusTeks(){
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txtStatus.setText("Aktif");
                    txtStatus.setTextColor(Color.GREEN);
                    txtUcapan.setText("Selamat Bekerja");
                    txtUcapan.setTextColor(Color.GREEN);
                    mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_BERJUALAN).setValue(true);
                }else if (!isChecked){
                    txtStatus.setText("Non-Aktif");
                    txtStatus.setTextColor(Color.RED);
                    txtUcapan.setText("Selamat Berlibur");
                    txtUcapan.setTextColor(Color.RED);
                    mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_BERJUALAN).setValue(false);
                    mDatabase.child(Constants.PENJUAL).child(BaseActivity.getUid()).child(Constants.STATUS_LOKASI).setValue(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
