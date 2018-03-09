package com.mlijo.aryaym.penjual_mlijo.Pengaturan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.penjual_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.mlijo.aryaym.penjual_mlijo.Utils.EncodeImage;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowAlertDialog;
import com.mlijo.aryaym.penjual_mlijo.Utils.ShowSnackbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mlijo.aryaym.penjual_mlijo.Utils.Constants.PERMISSIONS_CAMERA;
import static com.mlijo.aryaym.penjual_mlijo.Utils.Constants.PERMISSIONS_STORAGE;
import static com.mlijo.aryaym.penjual_mlijo.Utils.Constants.REQUEST_CODE_CAMERA;
import static com.mlijo.aryaym.penjual_mlijo.Utils.Constants.REQUEST_CODE_READ_STORAGE;

public class PengaturanProfilActivity extends BaseActivity {

    @BindView(R.id.imgPenjual)
    ImageView imgPenjual;
    @BindView(R.id.input_alamat_penjual)
    EditText inputAlamatPenjual;
    @BindView(R.id.input_telepon_penjual)
    EditText inputTeleponPenjual;
    @BindView(R.id.chk_sayuran)
    CheckBox chkSayuran;
    @BindView(R.id.chk_buah)
    CheckBox chkBuah;
    @BindView(R.id.chk_daging)
    CheckBox chkDaging;
    @BindView(R.id.chk_ikan)
    CheckBox chkIkan;
    @BindView(R.id.chk_palawija)
    CheckBox chkPalawija;
    @BindView(R.id.chk_bumbu)
    CheckBox chkBumbu;
    @BindView(R.id.chk_peralatan)
    CheckBox chkPeralatan;
    @BindView(R.id.chk_lain)
    CheckBox chkLain;
    @BindView(R.id.btn_simpan_profil)
    Button btnSimpanProfil;

    private Uri mUri;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private byte[] bitmapDataUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_profil);
        setTitle(R.string.title_activity_kelola_profil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        ambilData();
    }

    private void ambilData() {
        try {
            mDatabase.child(Constants.PENJUAL).child(getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                        if (penjualModel != null) {
                            try {
                                inputAlamatPenjual.setText(penjualModel.getDetailPenjual().get(Constants.ALAMAT).toString());
                                inputTeleponPenjual.setText(penjualModel.getDetailPenjual().get(Constants.TELPON).toString());
                                //get kategori
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_SAYURAN).toString())) {
                                    chkSayuran.setChecked(true);
                                } else {
                                    chkSayuran.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_BUAH).toString())) {
                                    chkBuah.setChecked(true);
                                } else {
                                    chkBuah.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_DAGING).toString())) {
                                    chkDaging.setChecked(true);
                                } else {
                                    chkDaging.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_IKAN).toString())) {
                                    chkIkan.setChecked(true);
                                } else {
                                    chkIkan.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_PALAWIJA).toString())) {
                                    chkPalawija.setChecked(true);
                                } else {
                                    chkPalawija.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_BUMBU).toString())) {
                                    chkBumbu.setChecked(true);
                                } else {
                                    chkBumbu.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_PERALATAN).toString())) {
                                    chkPeralatan.setChecked(true);
                                } else {
                                    chkPeralatan.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_LAIN).toString())) {
                                    chkLain.setChecked(true);
                                } else {
                                    chkLain.setChecked(false);
                                }
                                ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), imgPenjual);
                            } catch (Exception e) {

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    private boolean cekFillData() {
        boolean sukses = true;
        if (inputAlamatPenjual.getText() == null || inputTeleponPenjual.getText() == null && (inputTeleponPenjual.getText().length() > 12 || inputTeleponPenjual.getText().length() < 10)) {
            sukses = false;
            ShowAlertDialog.showAlert("Mohon diisi", this);
        }
        return sukses;
    }

    private void perbaruiData() {
        //getData
        String alamat = inputAlamatPenjual.getText().toString();
        String notelp = inputTeleponPenjual.getText().toString();
        boolean katSayur = chkSayuran.isChecked();
        boolean katBuah = chkBuah.isChecked();
        boolean katDaging = chkDaging.isChecked();
        boolean katIkan = chkIkan.isChecked();
        boolean katPala = chkPalawija.isChecked();
        boolean katBumbu = chkBumbu.isChecked();
        boolean katAlat = chkPeralatan.isChecked();
        boolean katLain = chkLain.isChecked();
        try {

            Map<String, Object> detailInfo = new HashMap<>();
            detailInfo.put(Constants.KATEGORI_SAYURAN, katSayur);
            detailInfo.put(Constants.KATEGORI_BUAH, katBuah);
            detailInfo.put(Constants.KATEGORI_DAGING, katDaging);
            detailInfo.put(Constants.KATEGORI_IKAN, katIkan);
            detailInfo.put(Constants.KATEGORI_PALAWIJA, katPala);
            detailInfo.put(Constants.KATEGORI_BUMBU, katBumbu);
            detailInfo.put(Constants.KATEGORI_PERALATAN, katAlat);
            detailInfo.put(Constants.KATEGORI_LAIN, katLain);

            mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.INFO_KATEGORI).updateChildren(detailInfo);
            mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DETAIL_PENJUAL).child(Constants.ALAMAT).setValue(alamat);
            mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DETAIL_PENJUAL).child(Constants.TELPON).setValue(notelp);
        } catch (Exception e) {

        }
    }

    //Upload Foto penjual
    private void showAlertForCamera() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.custom_dialog_up_image, null);
        builder.setView(v);
        //components in custom view
        TextView txtGallery = v.findViewById(R.id.txt_open_gallery);
        TextView txtCamera = v.findViewById(R.id.txt_open_camera);
        //show dialog
        final AlertDialog alertDialog = builder.show();
        //event click
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyStoragePermissions()) {
                    showGallery();
                }
                alertDialog.dismiss();
            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyOpenCamera()) {
                    openCamera();
                }
                alertDialog.dismiss();
            }
        });

    }

    //open gallery to choosing image
    private void showGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.GALLERY_INTENT);
    }

    //open gallery to taking a picture
    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image File Name");
        mUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(cameraIntent, Constants.CAMERA_INTENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showGallery();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
        }
    }

    //confirm request persmission
    private boolean verifyOpenCamera() {
        int camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA
            );

            return false;
        }
        return true;
    }

    //confirm request persmission
    private boolean verifyStoragePermissions() {
        // Check if we have read or write permission
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE, REQUEST_CODE_READ_STORAGE
            );

            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY_INTENT && resultCode == RESULT_OK) {
            if (InternetConnection.getInstance().isOnline(PengaturanProfilActivity.this)) {
                try {
                    //load image into imageview
                    ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, data.getData().toString(), imgPenjual);
                    Constants.USER_FILE_PATH = getRealPathFromURI(data.getData());
                    addImageUser(getUid(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            editUserPhotoURL(getUid(), taskSnapshot.getDownloadUrl().toString());
                        }
                    });
                } catch (Exception e) {
                    ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                }
            } else {
                ShowSnackbar.showSnack(PengaturanProfilActivity.this, getResources().getString(R.string.msg_noInternet));
            }
        } else if (requestCode == Constants.CAMERA_INTENT && resultCode == RESULT_OK) {
            if (InternetConnection.getInstance().isOnline(PengaturanProfilActivity.this)) {
                try {
                    if (mUri != null) {
                        //load image into imageview
                        ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, mUri.toString(), imgPenjual);
                        Constants.USER_FILE_PATH = getRealPathFromURI(mUri);
                        addImageUser(getUid(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                editUserPhotoURL(getUid(), taskSnapshot.getDownloadUrl().toString());
                            }
                        });
                    } else {
                        ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                    }
                } catch (Exception e) {
                    ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                }
            } else {
                ShowSnackbar.showSnack(PengaturanProfilActivity.this, getResources().getString(R.string.msg_noInternet));
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void editUserPhotoURL(String uid, String photoURL) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put(Constants.AVATAR, photoURL);
        mDatabase.child(Constants.PENJUAL).child(uid).child(Constants.DETAIL_PENJUAL).updateChildren(myMap);
    }

    public void addImageUser(String uid, OnSuccessListener<UploadTask.TaskSnapshot> listener) {
        if (Constants.USER_FILE_PATH != null) {
            bitmapDataUser = EncodeImage.encodeImage(Constants.USER_FILE_PATH);
        }
        if (bitmapDataUser != null) {
            StorageReference filePathAvatar = mStorage.child(Constants.USER_AVATAR).child(Constants.PENJUAL).child(uid).child(Constants.AVATAR);
            UploadTask uploadTask = filePathAvatar.putBytes(bitmapDataUser);
            uploadTask.addOnSuccessListener(listener);

            //restart bitmap
            Constants.USER_FILE_PATH = null;
        }
    }

    @OnClick(R.id.imgPenjual)
    public void onImgPenjualClicked() {
        showAlertForCamera();
    }

    @OnClick(R.id.btn_simpan_profil)
    public void onBtnSimpanProfilClicked() {
        if (cekFillData()) {
            perbaruiData();
            finish();
        }
    }
}

