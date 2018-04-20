package com.mlijo.aryaym.penjual_mlijo.KelolaPenjualan;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.penjual_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.penjual_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.penjual_mlijo.DBModel.TransaksiModel;
import com.mlijo.aryaym.penjual_mlijo.InformasiKonsumen.LokasiKonsumenActivity;
import com.mlijo.aryaym.penjual_mlijo.Obrolan.ObrolanActivity;
import com.mlijo.aryaym.penjual_mlijo.R;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AryaYM on 25/09/2017.
 */

public class DetailTransaksiActivity extends BaseActivity
        implements ValueEventListener, EventListener<DocumentSnapshot>{


    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.txtNamaProduk)
    TextView txtNamaProduk;
    @BindView(R.id.jml_item_produk)
    TextView jmlItemProduk;
    @BindView(R.id.total_harga_produk)
    TextView totalHargaProduk;
    @BindView(R.id.catatan_pembeli)
    TextView catatanPembeli;
    @BindView(R.id.nama_penerima)
    TextView namaPenerima;
    @BindView(R.id.alamat_lengkap)
    TextView alamatLengkap;
    @BindView(R.id.nomortelp_penerima)
    TextView telpPenerima;
    @BindView(R.id.status_transaksi)
    TextView statusTransaksi;
    @BindView(R.id.input_biaya_kirim)
    EditText inputBiayaKirim;
    @BindView(R.id.txt_biaya_kirim)
    TextView txtBiayaKirim;
    @BindView(R.id.status_layout)
    LinearLayout statusLayout;
    @BindView(R.id.btn_tolak_pesanan)
    Button btnTolakPesanan;
    @BindView(R.id.btn_terima_pesanan)
    Button btnTerimaPesanan;
    @BindView(R.id.btn_perbarui_status)
    Button btnPerbaruiStatus;
    @BindView(R.id.penerima_layout)
    LinearLayout penerimaLayout;
    @BindView(R.id.input_penerima_pesanan)
    EditText inputPenerimaPesanan;
    @BindView(R.id.txt_penerima_pesanan)
    TextView txtPenerimaPesanan;
    @BindView(R.id.txt_harga_produk)
    TextView txtHargaProduk;
    @BindView(R.id.txt_satuan_digit)
    TextView txtSatuanDigit;
    @BindView(R.id.txt_satuan)
    TextView txtSatuan;
    @BindView(R.id.input_total_harga)
    EditText inputTotalHarga;
    @BindView(R.id.btn_kirim_obrolan)
    Button btnKirimObrolan;
    @BindView(R.id.btn_lihat_peta)
    Button btnLihatPeta;

    private static final String TAG = "DetailTransaksiActivity";
    @BindView(R.id.txt_tanggal_kirim)
    TextView txtTanggalKirim;
    @BindView(R.id.txt_waktu_kirim)
    TextView txtWaktuKirim;
    private DatabaseReference mDatabase, konsumenRef;
    private FirebaseFirestore mFirestore;
    private DocumentReference produkRef;
    private TransaksiModel transaksiModel;

    int getStatus, statusBaru;
    double biayaKirim, totalHarga, biayaDouble;
    String nama_penerima, nama_produk;
    public String transaksiId, konsumenId, jenisProduk, produkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_detail_transaksi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transaksiModel = (TransaksiModel) getIntent().getSerializableExtra(Constants.TRANSAKSI);
        transaksiId = transaksiModel.getIdTransaksi();
        jenisProduk = transaksiModel.getJenisProduk();
        produkId = transaksiModel.getIdProduk();
        konsumenId = transaksiModel.getIdKonsumen();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        konsumenRef = mDatabase.child(Constants.KONSUMEN).child(konsumenId);
        produkRef = mFirestore.collection(jenisProduk).document(produkId);

        initInfo();
        loadDataPesanan();
        handleDataType();

    }

    @Override
    protected void onStart(){
        super.onStart();
        konsumenRef.addValueEventListener(this);
        produkRef.addSnapshotListener(this);
    }

    private void initInfo() {
        if (transaksiModel.getStatusTransaksi() == 1) {
            statusTransaksi.setText(Constants.MENUNGGU);
            tombolKonfirmasiTransaksi();
            Log.d(TAG, "data" + transaksiModel.getJenisProduk());
            if (transaksiModel.getJenisProduk().equals(Constants.PRODUK_REGULER)) {
               // inputTotalHarga.setVisibility(View.GONE);
                totalHargaProduk.setVisibility(View.VISIBLE);
                inputBiayaKirim.setVisibility(View.VISIBLE);
             //   txtBiayaKirim.setVisibility(View.GONE);
            } else if (transaksiModel.getJenisProduk().equals(Constants.PRODUK_KHUSUS)) {
                tampilInputBiaya();
            }
           // statusLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 2) {
            statusTransaksi.setText(Constants.DIPROSES);
            btnPerbaruiStatus.setVisibility(View.VISIBLE);
            sembunyikanInputBiaya();
         //   penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 3) {
            statusTransaksi.setText(Constants.DIKIRIM);
            btnPerbaruiStatus.setVisibility(View.VISIBLE);
            sembunyikanInputBiaya();
            penerimaLayout.setVisibility(View.VISIBLE);
         //   txtPenerimaPesanan.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 4) {
            statusTransaksi.setText(Constants.TERKIRIM);
            btnPerbaruiStatus.setVisibility(View.VISIBLE);
            sembunyikanInputBiaya();
            btnPerbaruiStatus.setEnabled(false);
            btnPerbaruiStatus.setText("Menunggu konfirmasi penerima");
            penerimaLayout.setVisibility(View.VISIBLE);
           // inputPenerimaPesanan.setVisibility(View.GONE);
            txtPenerimaPesanan.setVisibility(View.VISIBLE);
        } else if (transaksiModel.getStatusTransaksi() == 5) {
            statusTransaksi.setText(Constants.DITOLAK);
           // sembunyikanTombol();
            sembunyikanInputBiaya();
          //  penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 6) {
            statusTransaksi.setText(Constants.DIBATALKAN);
          //  sembunyikanTombol();
            sembunyikanInputBiaya();
        //    penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 7) {
            statusTransaksi.setText(Constants.DITERIMA);
          //  sembunyikanTombol();
            sembunyikanInputBiaya();
            penerimaLayout.setVisibility(View.VISIBLE);
          //  inputPenerimaPesanan.setVisibility(View.GONE);
            txtPenerimaPesanan.setVisibility(View.VISIBLE);
        }
    }

    public void tampilInputBiaya() {
        inputTotalHarga.setVisibility(View.VISIBLE);
        inputBiayaKirim.setVisibility(View.VISIBLE);
    }

    public void sembunyikanInputBiaya() {
        totalHargaProduk.setVisibility(View.VISIBLE);
        txtBiayaKirim.setVisibility(View.VISIBLE);
    }

    public void tombolKonfirmasiTransaksi() {
        btnTolakPesanan.setVisibility(View.VISIBLE);
        btnTerimaPesanan.setVisibility(View.VISIBLE);
    }

    public void handleDataType() {
        try {
            biayaDouble = Double.parseDouble(inputBiayaKirim.getText().toString());
        } catch (NumberFormatException e) {
            biayaDouble = Double.valueOf(0);
        }
    }

    private void loadDataPesanan() {
        try {
            catatanPembeli.setText(transaksiModel.getCatatanKonsumen());
            totalHargaProduk.setText("Rp." + rupiah().format(transaksiModel.getTotalHarga()));
            jmlItemProduk.setText(String.valueOf(transaksiModel.getJumlahProduk()));
            txtBiayaKirim.setText("Rp." + rupiah().format(transaksiModel.getBiayaKirim()));
            txtPenerimaPesanan.setText(transaksiModel.getPenerima());
            txtTanggalKirim.setText(transaksiModel.getTanggalKirim());
            txtWaktuKirim.setText(transaksiModel.getWaktuKirim());
        } catch (Exception e) {

        }
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (snapshot != null){
            ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
            nama_produk = produkModel.getNamaProduk();
            try {
                txtNamaProduk.setText(produkModel.getNamaProduk());
                txtSatuanDigit.setText(produkModel.getSatuanProduk());
                txtSatuan.setText(produkModel.getNamaSatuan());
                txtHargaProduk.setText("Rp." + BaseActivity.rupiah().format(produkModel.getHargaProduk()));
                ImageLoader.getInstance().loadImageOther(DetailTransaksiActivity.this, produkModel.getGambarProduk().get(0), imgProduk);
                //nama_produk = produkModel.getNamaProduk();
            }catch (Exception er){

            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
        if (konsumenModel != null) {
            namaPenerima.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
            alamatLengkap.setText(konsumenModel.getDetailKonsumen().get(Constants.ALAMAT).toString());
            telpPenerima.setText(konsumenModel.getDetailKonsumen().get(Constants.TELPON).toString());

            btnKirimObrolan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTransaksiActivity.this, ObrolanActivity.class);
                    intent.putExtra(Constants.ID_KONSUMEN, konsumenModel.getUid());
                    intent.putExtra(Constants.AVATAR, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString());
                    intent.putExtra(Constants.NAMA, konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                    startActivity(intent);
                }
            });
            btnLihatPeta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            Intent intent = new Intent(DetailTransaksiActivity.this, LokasiKonsumenActivity.class);
                            intent.putExtra(Constants.LATITUDE, Double.parseDouble(konsumenModel.getDetailKonsumen().get(Constants.LATITUDE).toString()));
                            intent.putExtra(Constants.LONGITUDE, Double.parseDouble(konsumenModel.getDetailKonsumen().get(Constants.LONGITUDE).toString()));
                            intent.putExtra(Constants.NAMA, konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                            startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void terimaPesanan() {
        String status_transaksi_title = "diterima";
        String status_transaksi = Constants.STATUS_PEMBELIAN;
        mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU)
                .child(transaksiModel.getIdTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                if (transaksiModel.getJenisProduk().equals(Constants.PRODUK_REGULER)) {
                                    biayaKirim = biayaDouble;
                                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.STATUS_TRANSAKSI).setValue(2);
                                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.BIAYA_KIRIM).setValue(biayaKirim);

                                } else {
                                    biayaKirim = biayaDouble;
                                    totalHarga = Double.parseDouble(inputTotalHarga.getText().toString());
                                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.STATUS_TRANSAKSI).setValue(2);
                                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.BIAYA_KIRIM).setValue(biayaKirim);
                                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.JUMLAH_HARGA_PRODUK).setValue(totalHarga);
                                }

                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiModel.getIdTransaksi()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU)
                .child(transaksiModel.getIdTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                if (transaksiModel.getJenisProduk().equals(Constants.PRODUK_REGULER)) {
                                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.STATUS_TRANSAKSI).setValue(2);
                                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.BIAYA_KIRIM).setValue(biayaKirim);
                                } else {
                                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.STATUS_TRANSAKSI).setValue(2);
                                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.BIAYA_KIRIM).setValue(biayaKirim);
                                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                            .child(Constants.JUMLAH_HARGA_PRODUK).setValue(totalHarga);
                                }
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiModel.getIdTransaksi()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //buatNotifikasiTerimaOrder();
        buatNotifikasiStatusTransaksi(status_transaksi, status_transaksi_title);
    }

    private void perbaruiStatusPesanan() {
        String status_transaksi_title = "sedang di proses";
        String status_transaksi = Constants.STATUS_PEMBELIAN;
        mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI)
                .child(transaksiModel.getIdTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TransaksiModel transaksiModel = dataSnapshot.getValue(TransaksiModel.class);
                getStatus = transaksiModel.getStatusTransaksi();
                statusBaru = getStatus + 1;
                if (getStatus < 4) {
                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.STATUS_TRANSAKSI).setValue(statusBaru);
                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.STATUS_TRANSAKSI).setValue(statusBaru);
                }
                if (getStatus == 3) {
                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.STATUS_TRANSAKSI).setValue(statusBaru);
                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.STATUS_TRANSAKSI).setValue(statusBaru);
                    //tambah nama penerima
                    nama_penerima = inputPenerimaPesanan.getText().toString();
                    mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.PENERIMA).setValue(nama_penerima);
                    mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                            .child(Constants.PENERIMA).setValue(nama_penerima);
                }
                //ShowAlertDialog.showAlert("Status transaksi berhasil diperbarui", DetailTransaksiActivity.this);
                //finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buatNotifikasiStatusTransaksi(status_transaksi, status_transaksi_title);
        finish();
    }

    private void tolakPesanan() {
        String status_transaksi_title = "ditolak";
        String status_transaksi = Constants.RIWAYAT_TRANSAKSI;
        mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU)
                .child(transaksiModel.getIdTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                        .child(Constants.STATUS_TRANSAKSI).setValue(5);
                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiModel.getIdTransaksi()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU)
                .child(transaksiModel.getIdTransaksi()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiModel.getIdTransaksi())
                                        .child(Constants.STATUS_TRANSAKSI).setValue(5);
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(transaksiModel.getIdKonsumen()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiModel.getIdTransaksi()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buatNotifikasiStatusTransaksi(status_transaksi, status_transaksi_title);
    }

    private void buatNotifikasiTerimaOrder() {
        String key = mDatabase.child(Constants.NOTIFIKASI).child(transaksiModel.getIdKonsumen()).push().getKey();
        Map<String, Object> notifikasi = new HashMap<>();
        notifikasi.put(Constants.TITLE, "Status Pembelian");
        notifikasi.put(Constants.TRANSAKSI, Constants.STATUS_PEMBELIAN);
        notifikasi.put("cobaint", 1);
        notifikasi.put("penjualId", getUid());
        mDatabase.child(Constants.NOTIFIKASI).child("konsumen").child(Constants.ORDER).child(transaksiModel.getIdKonsumen()).child(key).setValue(notifikasi);
    }

    private void buatNotifikasiStatusTransaksi(String status, String title){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String konsumenId = transaksiModel.getIdKonsumen();

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic MmE5NTkyYWYtODM3OS00MTkzLTllZGEtNjA5MDM1MDRlYWE4");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"eed14716-bf93-456a-9833-203325aad307\","
                    +   "\"included_segments\": [\"All\"],"
                    +   "\"filters\": [{\"field\": \"tag\", \"key\": \"uid\", \"relation\": \"=\", \"value\": \"" + konsumenId + "\"}],"
                    +   "\"data\": {\"title\": \"Status Pembelian\",\"click_action\": \"2\",\"transaksi\": \""+ status +"\"},"
                    //+   "\"data\": {\"uid\": \"SyykXrusoxTSP8nWg2u4kYFQIdq2\",\"click_action\": \"1\"},"
                    +   "\"contents\": {\"en\": \"Pesanan anda produk " + nama_produk + " " + title + " penjual\"}"
                    + "}";


            //System.out.println("strJsonBody:\n" + strJsonBody);
            Log.d("nilai strJsonBody:", "" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            //System.out.println("httpResponse: " + httpResponse);
            Log.d("nilai httpRespone:", "" + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            //System.out.println("jsonResponse:\n" + jsonResponse);
            Log.d("nilai jsonRespone:", "" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @OnClick(R.id.btn_tolak_pesanan)
    public void onBtnTolakPesananClicked() {
        tolakPesanan();
        finish();
    }

    @OnClick(R.id.btn_terima_pesanan)
    public void onBtnTerimaPesananClicked() {
        terimaPesanan();
        finish();
    }

    @OnClick(R.id.btn_perbarui_status)
    public void onBtnPerbaruiStatusClicked() {
        perbaruiStatusPesanan();
        finish();
    }


}
