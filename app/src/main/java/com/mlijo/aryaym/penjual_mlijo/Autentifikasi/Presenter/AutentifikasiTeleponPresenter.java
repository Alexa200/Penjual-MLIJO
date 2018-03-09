package com.mlijo.aryaym.penjual_mlijo.Autentifikasi.Presenter;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import com.mlijo.aryaym.penjual_mlijo.Autentifikasi.AutentifikasiTeleponActivity;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by AryaYM on 31/01/2018.
 */

public class AutentifikasiTeleponPresenter {

    private AutentifikasiTeleponActivity view;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    //private FirebaseFirestore mDatabase;
    private String mVerificationId;

    public AutentifikasiTeleponPresenter(AutentifikasiTeleponActivity view){
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase = FirebaseFirestore.getInstance();
    }

    public void requestCode(String phoneNumber) {
       // txtPhoneNumber.setText(phoneNumber);
        if (TextUtils.isEmpty(phoneNumber))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, view, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        view.tampilToast("onVerificationFailed" + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                view.tampilHitungMundur(millisUntilFinished);
                            }

                            public void onFinish() {
                                view.tampilTombolKirimUlang();
                            }
                        }.start();

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        view.tampilToast("onCodeAutoRetrievalTimeOut :" + verificationId);
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           view.tampilToast("berhasil login");
                           //DocumentReference documentReference = mDatabase.collection(Constants.PENJUAL).document(mAuth.getCurrentUser().getUid());
//                           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                               @Override
//                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                   if (task.isSuccessful()){
//                                       DocumentSnapshot document = task.getResult();
//                                       if (document != null){
//                                       Log.d("nilai doc", String.valueOf(document));
//                                           Log.d("nilai tag", "DocumentSnapshot data: " + task.getResult().getData());
//
//                                           view.loginSukses();
//                                       }else {
//                                           Log.d("nilai tag", "no Document ");
//                                           view.lengkapiDataUserBaru();
//                                       }
//                                   }else {
//                                       Log.d("error msg", "get failed with ", task.getException());
//
//                                   }
//                               }
//                           });
//                           documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                               @Override
//                               public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                                   if (e != null) {
//                                       Log.w("mbuh", "Listen failed.", e);
//                                       return;
//                                   }
//
//                                   if (documentSnapshot != null && documentSnapshot.exists()) {
//                                       Log.d("nilai", "Current data: " + documentSnapshot.getData());
//                                       view.loginSukses();
//                                   } else {
//                                       Log.d("nilai", "Current data: null");
//                                       view.lengkapiDataUserBaru();
//                                   }
//
//                               }
//                           });

                            mDatabase.child(Constants.PENJUAL).child(mAuth.getCurrentUser().getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {

                                                view.loginSukses();
                                            } else {
                                                view.lengkapiDataUserBaru();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

//                            mDatabase.child(Constants.PENJUAL).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {//
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot != null) {
//                                        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
//                                        if (penjualModel != null) {
//                                            view.loginSukses();
//                                        } else {
//                                            view.lengkapiDataUserBaru();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
                        } else {
                            view.tampilToast("" + task.getException().getMessage());
                        }
                    }
                });
    }

    public void signIn(String code) {
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }
}