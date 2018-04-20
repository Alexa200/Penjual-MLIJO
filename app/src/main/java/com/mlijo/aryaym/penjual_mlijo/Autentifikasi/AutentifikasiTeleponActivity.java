package com.mlijo.aryaym.penjual_mlijo.Autentifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.penjual_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.penjual_mlijo.MainActivity;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowAlertDialog;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutentifikasiTeleponActivity extends AppCompatActivity {

    @BindView(R.id.txt_countdown)
    TextView txtCountdown;
    @BindView(R.id.input_kode_verifikasi)
    EditText inputKodeVerifikasi;
    @BindView(R.id.txt_phone_number)
    TextView txtPhoneNumber;
    @BindView(R.id.btn_kirim_ulang_kode)
    Button btnKirimUlangKode;
    @BindView(R.id.input_nomor_telepon)
    EditText inputNomorTelepon;
    @BindView(R.id.layout_input_nomor)
    LinearLayout layoutInputNomor;
    @BindView(R.id.layout_input_kode)
    LinearLayout layoutInputKode;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    private String mVerificationId, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikasi_telepon);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutInputKode.setVisibility(View.GONE);
    }

    public boolean cekNomor() {
        boolean statusNomor = true;
        phoneNumber = ccp.getFullNumberWithPlus().concat(inputNomorTelepon.getText().toString());
        if (TextUtils.isEmpty(inputNomorTelepon.getText()) || TextUtils.getTrimmedLength(inputNomorTelepon.getText()) < 10) {
            statusNomor = false;
            ShowAlertDialog.showAlert("Mohon perhatikan nomor telepon Anda dengan benar !", this);
        }
        return statusNomor;
    }

    public void requestCode() {
        txtPhoneNumber.setText(phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, AutentifikasiTeleponActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(AutentifikasiTeleponActivity.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                txtCountdown.setText("kirim kode dalam :  " + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                txtCountdown.setVisibility(View.GONE);
                                btnKirimUlangKode.setVisibility(View.VISIBLE);
                            }
                        }.start();

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        //Toast.makeText(ActivityPhoneAuth.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AutentifikasiTeleponActivity.this, "autentifikasi berhasil", Toast.LENGTH_SHORT).show();
                            mDatabase.child(Constants.PENJUAL).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                                        if (penjualModel != null) {
                                            Toast.makeText(AutentifikasiTeleponActivity.this, "login berhasil", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AutentifikasiTeleponActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(AutentifikasiTeleponActivity.this, "lengkapi data diri anda", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AutentifikasiTeleponActivity.this, DataUserBaruActivity.class);
                                            intent.putExtra(Constants.TELPON, phoneNumber);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(AutentifikasiTeleponActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn() {
        String code = inputKodeVerifikasi.getText().toString();
        if (TextUtils.isEmpty(code))
            return;
        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }

    @OnClick(R.id.btn_selanjutnya)
    public void onBtnSelanjutnyaClicked(View view) {
        if (cekNomor()) {
            layoutInputKode.setVisibility(View.VISIBLE);
            layoutInputNomor.setVisibility(View.GONE);
            requestCode();
            Log.d("nilai cpp", ""+ phoneNumber);
        }
    }

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {
        signIn();
    }

    @OnClick(R.id.btn_kirim_ulang_kode)
    public void onBtnKirimUlangKodeClicked() {
        requestCode();
    }

    @OnClick(R.id.btn_edit_nomor)
    public void onViewClicked() {
        layoutInputKode.setVisibility(View.GONE);
        layoutInputNomor.setVisibility(View.VISIBLE);
    }
}
