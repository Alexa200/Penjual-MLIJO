package com.mlijo.aryaym.penjual_mlijo.Autentifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.MainActivity;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowAlertDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataUserBaruActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.input_nik)
    EditText inputNik;
    @BindView(R.id.input_nama_lengkap)
    EditText inputNamaLengkap;
    @BindView(R.id.input_alamat)
    EditText inputAlamat;
    @BindView(R.id.input_telepon)
    EditText inputTelepon;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String phoneNumber;
    private DatabaseReference mDatabase;
    //private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user_baru);
        ButterKnife.bind(this);

        phoneNumber = getIntent().getStringExtra(Constants.TELPON);
        inputTelepon.setText(phoneNumber);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase = FirebaseFirestore.getInstance();
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit){
            if (cekFillData()){
                showProgessDialog();
                submitData();
                startActivity(new Intent(DataUserBaruActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private boolean cekFillData(){
        boolean result = true;
        if (TextUtils.isEmpty(inputNik.getText()) || TextUtils.getTrimmedLength(inputNik.getText()) != 16 ||
                TextUtils.isEmpty(inputNamaLengkap.getText()) || TextUtils.isEmpty(inputAlamat.getText()) ||
                TextUtils.isEmpty(inputTelepon.getText()) || TextUtils.getTrimmedLength(inputTelepon.getText()) < 10) {
            result = false;
            ShowAlertDialog.showAlert("Wajib diisi, mohon perhatikan input data Anda dengan benar !", this);
        }
        return result;
    }

    private void submitData(){
        String dataNIK = inputNik.getText().toString();
        String dataNama = inputNamaLengkap.getText().toString();
        String dataAlamat = inputAlamat.getText().toString();
        String dataTelpon = inputTelepon.getText().toString();

        buatDataUser(dataNIK, dataNama, dataAlamat, dataTelpon);
        defaultDataUser();
    }

    private void buatDataUser(String NIK, String Nama, String Alamat, String Telpon){
        //DocumentReference documentReference = mDatabase.collection(Constants.PENJUAL).document(getUid());
        Map<String, Object> detailPenjualData = new HashMap<>();
        detailPenjualData.put(Constants.NIK, NIK);
        detailPenjualData.put(Constants.NAMA, Nama);
        detailPenjualData.put(Constants.AVATAR, "");
        detailPenjualData.put(Constants.ALAMAT, Alamat);
        detailPenjualData.put(Constants.TELPON, Telpon);

        //documentReference.collection(Constants.DETAIL_PENJUAL).add(detailPenjualData);
        mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DETAIL_PENJUAL).setValue(detailPenjualData);
    }

    private void defaultDataUser(){
        //DocumentReference documentReference = mDatabase.collection(Constants.PENJUAL).document(getUid());
        Map<String, Object> defaultPenjualData = new HashMap<>();
        defaultPenjualData.put(Constants.STATUS_BERJUALAN, false);
        defaultPenjualData.put(Constants.STATUS_LOKASI, false);
        defaultPenjualData.put(Constants.UID, getUid());

        //documentReference.set(defaultPenjualData);
        mDatabase.child(Constants.PENJUAL).child(getUid()).setValue(defaultPenjualData);
    }
}
