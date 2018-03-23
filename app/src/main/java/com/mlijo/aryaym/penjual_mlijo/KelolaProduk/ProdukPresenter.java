package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.KelolaProduk.service.UploadPhotoThread;
import com.mlijo.aryaym.penjual_mlijo.KelolaProduk.service.UploadPhotoThreadListener;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AryaYM on 11/03/2018.
 */

public class ProdukPresenter {

    private FirebaseFirestore mFirestore;
    private BuatProdukActivity view;

    public ProdukPresenter(BuatProdukActivity view){
        this.view = view;
        mFirestore = FirebaseFirestore.getInstance();
    }
    public ProdukPresenter(UbahProdukActivity view){
        view = view;
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void buatProdukBaru(final ProdukModel produkModel, final ArrayList<Uri> imageUri){
        final String pushId = mFirestore.collection(Constants.PRODUK_REGULER).document().getId();
        //buatProduk(produkModel, pushId);
        String produkId = pushId;
        HashMap<String, Object> dataProduk = new HashMap<>();
        dataProduk.put(Constants.ID_PENJUAL, produkModel.getUid());
        dataProduk.put(Constants.WAKTU_DIBUAT, produkModel.getWaktuDibuat());
        dataProduk.put(Constants.NAMAPRODUK, produkModel.getNamaProduk());
        dataProduk.put(Constants.ID_KATEGORI, produkModel.getKategoriProduk());
        dataProduk.put(Constants.HARGAPRODUK, produkModel.getHargaProduk());
        dataProduk.put(Constants.DIGITSATUAN, produkModel.getSatuanProduk());
        dataProduk.put(Constants.NAMASATUAN, produkModel.getNamaSatuan());
        dataProduk.put(Constants.ID_PRODUK, produkId);
        dataProduk.put(Constants.GAMBARPRODUK, produkModel.getGambarProduk());
        dataProduk.put(Constants.ID_LOKASI, produkModel.getIdLokasi());
        dataProduk.put(Constants.DESKRIPSI, produkModel.getDeskripsiProduk());

        mFirestore.collection(Constants.PRODUK_REGULER).document(produkId).set(dataProduk)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UploadPhotoThreadListener uploadPhotoThreadListener = new UploadPhotoThreadListener() {
                    @Override
                    public void onUploadPhotoSuccess(ArrayList<String> photoUrls) {
                        Map<String, Object> gambarProduk = new HashMap<>();
                        gambarProduk.put(Constants.GAMBARPRODUK, photoUrls);

                        mFirestore.collection(Constants.PRODUK_REGULER).document(pushId).update(gambarProduk)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                view.hideProgressDialog();
                                view.finish();
                                Toast.makeText(view.getApplicationContext(), "Produk anda telah tersimpan", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                view.hideProgressDialog();
                                view.finish();
                                Toast.makeText(view.getApplicationContext(),
                                        "Maaf, gagal melakukan upload gambar produk. Silahkan ulangi kembali!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                new UploadPhotoThread(pushId, imageUri, uploadPhotoThreadListener).execute();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getApplicationContext(),
                        "Maaf, gagal menyimpan produk. Silahkan ulangi kembali", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void perbaruiProduk(ProdukModel produkModel, String produkId){
        HashMap<String, Object> dataProduk = new HashMap<>();
        dataProduk.put(Constants.WAKTU_DIBUAT, produkModel.getWaktuDibuat());
        dataProduk.put(Constants.NAMAPRODUK, produkModel.getNamaProduk());
        dataProduk.put(Constants.HARGAPRODUK, produkModel.getHargaProduk());
        dataProduk.put(Constants.DIGITSATUAN, produkModel.getSatuanProduk());
        dataProduk.put(Constants.NAMASATUAN, produkModel.getNamaSatuan());
        dataProduk.put(Constants.DESKRIPSI, produkModel.getDeskripsiProduk());

        mFirestore.collection(Constants.PRODUK_REGULER).document(produkId).update(dataProduk);
        view.hideProgressDialog();
        view.finish();
        Toast.makeText(view.getApplicationContext(), "Produk anda telah diperbarui", Toast.LENGTH_SHORT).show();
    }
}
