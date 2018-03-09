package com.mlijo.aryaym.penjual_mlijo.Base;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mlijo.aryaym.penjual_mlijo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.Locale;


public class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;


    public void showProgessDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.msg_loading));
            mProgressDialog.setCancelable(false);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            };
            Handler handlerCancel = new Handler();
            handlerCancel.postDelayed(runnable, 10000);
        }
        try {
            mProgressDialog.show();
        } catch (Exception e) {
        }

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static NumberFormat rupiah(){
        Locale localeID = new Locale("in", "ID");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(localeID);

        return numberFormat;
    }
}
