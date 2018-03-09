package com.mlijo.aryaym.penjual_mlijo.Bantuan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowSnackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 25/12/2017.
 */

public class BantuanFragment extends Fragment {

    @BindView(R.id.set_feedback_layout)
    LinearLayout feedbackLayout;
    @BindView(R.id.set_panduan_layout)
    LinearLayout panduanLayout;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_bantuan);
        View view = inflater.inflate(R.layout.fragment_bantuan, container, false);
        unbinder = ButterKnife.bind(this, view);

        feedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimFeedback();
            }
        });

        panduanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Masih dalam pembuatan", Toast.LENGTH_LONG).show();
            }
        });


        return view;

    }

    private void kirimFeedback() {
        try {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", Constants.EMAIL_ADMIN, null));
            startActivity(i);
        } catch (Exception e) {
            ShowSnackbar.showSnack(getActivity(), getResources().getString(R.string.msg_error));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
