package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowAlertDialog;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowSnackbar;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mlijo.aryaym.penjual_mlijo.Utils.Constants.REQUEST_CODE_READ_EXTERNAL_STORAGE;

public class BuatProdukActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NewPostActivity";
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.input_nama_produk)
    EditText inputNamaProduk;
    @BindView(R.id.input_kategori)
    Spinner spnKategoriProduk;
    @BindView(R.id.photoContainer)
    LinearLayout photoContainer;
    @BindView(R.id.btn_upload)
    ImageButton btnUpload;
    @BindView(R.id.imageContainer)
    HorizontalScrollView imageContainer;
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
    @BindView(R.id.activity)
    LinearLayout activity;
    @BindView(R.id.spn_area_penjual)
    Spinner spnAreaPenjual;

    private ArrayAdapter<String> spinnerKategoriAdapter, spinnerSatuanAdapter, spinnerAreaPenjual;
    private String namaProduk, kategoriProduk, namaSatuan, deskripsiProduk, namaAreaPenjual;
    private Double hargaProduk;
    private int satuanProduk;
    // [START declare_database_ref]
    private FirebaseFirestore mFirestore;
    // [END declare_database_ref
    private ProdukPresenter produkPresenter;
    private ArrayList<Uri> listImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_produk);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_tambah_produk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // [START initialize_database_ref]
        mFirestore = FirebaseFirestore.getInstance();
        produkPresenter = new ProdukPresenter(this);

        btnUpload.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);

        handleSpinner();
    }

    private void handleSpinner() {
        //spinnerKategori
        spinnerKategoriAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrKategori));
        spinnerKategoriAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnKategoriProduk.setAdapter(spinnerKategoriAdapter);
        spnKategoriProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String itemKategori = adapterView.getItemAtPosition(position).toString();
                kategoriProduk = itemKategori;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinnerSatuan
        spinnerSatuanAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrSatuan));
        spinnerSatuanAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnNamaSatuan.setAdapter(spinnerSatuanAdapter);
        spnNamaSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String itemSatuan = adapterView.getItemAtPosition(position).toString();
                namaSatuan = itemSatuan;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinnerSatuan
        spinnerAreaPenjual = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrKecamatan));
        spinnerAreaPenjual.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnAreaPenjual.setAdapter(spinnerAreaPenjual);
        spnAreaPenjual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String itemArea = adapterView.getItemAtPosition(position).toString();
                namaAreaPenjual = itemArea;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean verifyStoragePermission() {
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            //request permission
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CODE_READ_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addPhoto();
                }
                break;
        }
    }

    private void addPhoto() {
        Config config = new Config();
        config.setSelectionMin(1);
        config.setSelectionLimit(4);
        config.setToolbarTitleRes(R.string.chooseImage);

        ImagePickerActivity.setConfig(config);

        Intent myIntent = new Intent(BuatProdukActivity.this, ImagePickerActivity.class);
        myIntent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, listImage);
        startActivityForResult(myIntent, INTENT_REQUEST_GET_IMAGES);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == RESULT_OK) {
            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            listImage.clear();
            for (Uri uri : image_uris) {
                //   String uriString = uri.toString();
                listImage.add(uri);
            }
            // mImage = data.getStringArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

            onPickImageSuccess();
        }
    }

    private void onPickImageSuccess() {
        int previewImageSize = getPixelValue(BuatProdukActivity.this, 150);
        photoContainer.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(previewImageSize, previewImageSize);
        params.setMargins(5, 0, 5, 0);

        for (Uri uri : listImage) {
            ImageView photo = new ImageView(this);
            photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo.setLayoutParams(params);
            //using Glide to load image
            ImageLoader.getInstance().loadImageOther(BuatProdukActivity.this, uri.toString(), photo);

            photoContainer.addView(photo);
        }
    }

    private int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    private boolean cekKolomIsian() {
        boolean hasil;
        if (TextUtils.isEmpty(inputNamaProduk.getText()) || TextUtils.isEmpty(inputHargaProduk.getText()) ||
                TextUtils.isEmpty(inputNominalSatuan.getText()) || TextUtils.isEmpty(inputDeskripsiProduk.getText())) {
            hasil = false;
        } else {
            hasil = true;
        }
        return hasil;
    }

    private void simpanProduk(){

        if (cekKolomIsian() == true) {
            namaProduk = inputNamaProduk.getText().toString();
            hargaProduk = Double.valueOf(inputHargaProduk.getText().toString());
            satuanProduk = Integer.parseInt(inputNominalSatuan.getText().toString());
            deskripsiProduk = inputDeskripsiProduk.getText().toString();
            long waktuDibuat = new Date().getTime();
            Log.d("nilai harga", "" + hargaProduk);
            if (InternetConnection.getInstance().isOnline(BuatProdukActivity.this)) {
                try {
                    //String statusTambahProduk = "tambah produk berhasil";
                    if (listImage.size() == 0) {
                        ShowAlertDialog.showAlert("Anda harus memilih gambar produk minimal (1)!", this);
                    } else {
                        showProgessDialog();
                        ProdukModel produkModel = new ProdukModel(getUid(), waktuDibuat, namaProduk, kategoriProduk,
                                hargaProduk, satuanProduk, namaSatuan, namaAreaPenjual, deskripsiProduk);
                        produkPresenter.buatProdukBaru(produkModel, listImage);
                    }
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
        } else {
            ShowAlertDialog.showAlert("Anda harus mengisi semua Form yang tersedia !", this);
        }
    }

    // [END write_fan_out]
    @Override
    public void onClick(View view) {
        if (view == btnUpload) {
            if (verifyStoragePermission()) {
                addPhoto();
            }
        } else if (view == btnSimpan) {
                simpanProduk();
        }
    }
}
