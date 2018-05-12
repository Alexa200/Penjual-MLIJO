package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowSnackbar;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UbahProdukActivity extends BaseActivity implements
        EventListener<DocumentSnapshot> {

    @BindView(R.id.input_nama_produk)
    EditText inputNamaProduk;
    @BindView(R.id.input_harga_produk)
    EditText inputHargaProduk;
    @BindView(R.id.nominal_satuan)
    EditText inputNominalSatuan;
    @BindView(R.id.nama_satuan)
    Spinner spnNamaSatuan;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;
    @BindView(R.id.btn_batal)
    Button btnBatal;
    @BindView(R.id.deskripsiProduk)
    EditText inputDeskripsiProduk;
    @BindView(R.id.nama_satuan_view)
    TextView namaSatuanView;
    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity)
    LinearLayout activity;

    private FirebaseFirestore mFirestore;
    private DocumentReference mProdukRef;
    private ProdukPresenter produkPresenter;
    private ListenerRegistration mProdukReg;
    private String produkId;
    private ArrayAdapter<String> spinnerSatuanAdapter;
    private String namaSatuan, namaProduk, deskripsiProduk;
    private Double hargaProduk;
    private int satuanProduk;
    private boolean isSpinnerTouched = false;
    private AdapterImagePager mViewImagePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_produk);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        produkId = getIntent().getExtras().getString(Constants.ID_PRODUK);
        mFirestore = FirebaseFirestore.getInstance();
        mProdukRef = mFirestore.collection(Constants.PRODUK_REGULER).document(produkId);
        produkPresenter = new ProdukPresenter(this);
        spinnerData();
    }

    private void spinnerData() {
        //spinnerSatuan
        spinnerSatuanAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrSatuan));
        spinnerSatuanAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnNamaSatuan.setAdapter(spinnerSatuanAdapter);
        spnNamaSatuan.setSelected(false);
        spnNamaSatuan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });
        spnNamaSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerTouched) {
                    ((TextView) view).setText(null);
                } else {
                    ((TextView) view).setText(null);
                    namaSatuanView.setText(spnNamaSatuan.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mProdukReg = mProdukRef.addSnapshotListener(this);
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("detail", "restaurant:onEvent", e);
            return;
        }
        onProductLoaded(snapshot.toObject(ProdukModel.class));
    }

    private void onProductLoaded(ProdukModel produkModel) {
        Log.d("nilai model satuan", "" + produkModel.getSatuanProduk());
        try {
            setTitle("Ubah produk  " + produkModel.getNamaProduk());
            inputNamaProduk.setText(produkModel.getNamaProduk());
            inputHargaProduk.setText(String.valueOf(produkModel.getHargaProduk()));
            inputDeskripsiProduk.setText(produkModel.getDeskripsiProduk());
            if (produkModel.getGambarProduk().size() > 0) {
                imgProduk.setVisibility(View.GONE);
                mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
                viewPager.setAdapter(mViewImagePager);
            } else {
                viewPager.setVisibility(View.GONE);
                imgProduk.setImageResource(R.drawable.no_image);
            }
            mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
            viewPager.setAdapter(mViewImagePager);
            namaSatuanView.setText(produkModel.getNamaSatuan().toString());
            inputNominalSatuan.setText(String.valueOf(produkModel.getSatuanProduk()));

        } catch (Exception e) {
        }
    }

    private void perbaruiDataProduk() {
        namaProduk = inputNamaProduk.getText().toString();
        hargaProduk = Double.valueOf(inputHargaProduk.getText().toString());
        satuanProduk = Integer.parseInt(inputNominalSatuan.getText().toString());
        deskripsiProduk = inputDeskripsiProduk.getText().toString();
        namaSatuan = namaSatuanView.getText().toString();
        long waktuDibuat = new Date().getTime();

        HashMap<String, Object> dataProduk = new HashMap<>();
        dataProduk.put(Constants.WAKTU_DIBUAT, waktuDibuat);
        dataProduk.put(Constants.NAMAPRODUK, namaProduk);
        dataProduk.put(Constants.HARGAPRODUK, hargaProduk);
        dataProduk.put(Constants.DIGITSATUAN, satuanProduk);
        dataProduk.put(Constants.NAMASATUAN, namaSatuan);
        dataProduk.put(Constants.DESKRIPSI, deskripsiProduk);
        //mFirestore.collection(Constants.PRODUK_REGULER).document(produkId).update(dataProduk);
        Log.d("nilai dataProduk", "" + dataProduk);
        showProgessDialog();
        Toast.makeText(getApplicationContext(), "Produk anda telah diperbarui", Toast.LENGTH_SHORT).show();

        if (InternetConnection.getInstance().isOnline(UbahProdukActivity.this)) {
            try {
                showProgessDialog();
                ProdukModel produkModel = new ProdukModel(waktuDibuat, namaProduk, hargaProduk,
                        satuanProduk, namaSatuan, deskripsiProduk);
                produkPresenter.perbaruiProduk(produkModel, produkId);
            } catch (Exception e) {
                ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
            }
        } else {
            final Snackbar snackbar = Snackbar.make(activity, getResources().getString(R.string.msg_noInternet), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    @OnClick(R.id.btn_simpan)
    public void onBtnSimpanClicked() {
        perbaruiDataProduk();
        finish();
    }

    @OnClick(R.id.btn_batal)
    public void onBtnBatalClicked() {
    }
}
