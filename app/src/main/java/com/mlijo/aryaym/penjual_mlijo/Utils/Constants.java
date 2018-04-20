package com.mlijo.aryaym.penjual_mlijo.Utils;

import android.Manifest;

/**
 * Created by AryaYM on 17/08/2017.
 */

public class Constants {


    public static final int PLACE_PICKER_REQUEST = 1;
    public static final int INTENT_REQUEST_GET_IMAGES = 13;
    public static final String KONSUMEN = "data_konsumen";
    public static final String PENJUAL = "data_penjual";
    public static final String GEOFIRE = "geofire";
    public static final String KONSUMEN_MODEL = "konsumen_model";
    public static final String NOTIFIKASI = "notifikasi";
    public static final String EMAIL_ADMIN = "aryayudham@gmail.com";
    //PenjualModel
    public static final String AVATAR = "avatar";
    public static final String USER_AVATAR = "userAvatar";
    public static final String NAMA = "nama";
    public static final String NIK = "NIK";
    public static final String TELPON = "noTelp";
    public static final String EMAIL = "email";
    public static final String UID = "uid";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String ALAMAT = "alamat";
    public static final String ALAMAT_USER = "alamatUser";
    public static final String STATUS_BERJUALAN = "statusBerjualan";
    public static final String STATUS_LOKASI = "statusLokasi";
    public static final String INFO_KATEGORI = "infoKategori";
    public static final String INFO_LOKASI = "infoLokasi";
    public static final String DETAIL_PENJUAL = "detailPenjual";
    //InfoDetail
    public static final String KECAMATAN = "kecamatan";
    public static final String HARI_MULAI = "hariMulai";
    public static final String HARI_SELESAI = "hariSelesai";
    public static final String JAM_MULAI = "jamMulai";
    public static final String JAM_SELESAI = "jamSelesai";
    public static final String KATEGORI_SAYURAN = "kategoriSayuran";
    public static final String KATEGORI_BUAH = "kategoriBuah";
    public static final String KATEGORI_DAGING = "kategoriDaging";
    public static final String KATEGORI_IKAN = "kategoriIkan";
    public static final String KATEGORI_PALAWIJA = "kategoriPalawija";
    public static final String KATEGORI_BUMBU = "kategoriBumbu";
    public static final String KATEGORI_PERALATAN = "kategoriAlat";
    public static final String KATEGORI_LAIN = "kategoriLain";
    //Produk
    public static final String IMAGES = "images";
    public static final String TITLE = "title";
    public static final String GAMBARPRODUK = "gambarProduk";
    public static final String PRODUK = "produk";
    public static final String PRODUK_REGULER = "produk_reguler";
    public static final String PRODUK_KHUSUS = "produk_khusus";
    public static final String ID_PENJUAL ="idPenjual";
    public static final String NAMAPRODUK = "namaProduk";
    public static final String ID_KATEGORI = "idKategori";
    public static final String HARGAPRODUK = "hargaProduk";
    public static final String DIGITSATUAN = "digitSatuan";
    public static final String NAMASATUAN = "namaSatuan";
    public static final String ID_PRODUK ="idProduk";
    public static final String DESKRIPSI ="deskripsiProduk";
    public static final String WAKTU_DIBUAT ="waktuDibuat";
    public static final String ID_LOKASI ="idLokasi";
    //Pesan Produk
    public static final String PENJUALAN = "penjualan";//rename jadi daftarTransaksi
    public static final String DAFTAR_TRANSAKSI = "daftarTransaksi";
    public static final String PENJUALAN_BARU = "penjualanBaru";//rename transaksi baru
    public static final String TRANSAKSI_BARU = "transaksiBaru";
    public static final String STATUS_PENGIRIMAN = "statusPengiriman";//rename status transaksi
    public static final String PEMBELIAN = "pembelian";//rename daftar transaksi
    public static final String PEMBELIAN_BARU = "pembelianBaru";
    public static final String STATUS_PEMBELIAN = "statusPembelian";
    public static final String TRANSAKSI_COUNT = "jumlahTransaksi";
    public static final String RIWAYAT_TRANSAKSI = "riwayatTransaksi";
    public static final String STATUS_TRANSAKSI = "statusTransaksi";
    public static final String ORDER = "order";
    //Transaksi
    public static final String TIPE_TRANSAKSI = "tipeTransaksi";
    public static final String TRANSAKSI = "transaksi";
    public static final String ID_KONSUMEN = "idKonsumen";
    public static final String ID_PEMESANAN = "idPemesanan";
    public static final String NOTE_KONSUMEN = "catatatanKonsumen";
    public static final String JUMLAH_PRODUK = "jumlahProduk";
    public static final String JUMLAH_HARGA_PRODUK = "totalHarga";
    public static final String PENERIMA = "penerima";
    public static final String BIAYA_KIRIM = "biayaKirim";
    //StatusList
    public static final String MENUNGGU = "menunggu konfirmasi penjual";
    public static final String DIPROSES = "sedang diproses";
    public static final String DIKIRIM = "sedang dalam pengiriman";
    public static final String TERKIRIM = "pesanan telah dikirim";
    public static final String DITOLAK = "maaf, pesanan ditolak penjual";
    public static final String DIBATALKAN = "maaf, pembelian dibatalkan pembeli";
    public static final String DITERIMA = "pesanan telah diterima pembeli";
    //Obrolan
    public static final String OBROLAN = "obrolan";
    public static final String KONTEN = "konten";
    public static final String KONTEN_FOTO = "kontenFoto";
    public static final String KONTEN_PENGIRIM = "kontenPengirim";
    public static final String ID_PENGIRIM = "idPengirim";
    public static final String TIMESTAMP = "timestamp";
    public static final String ID_PENERIMA = "idPenerima";
    //Informasi Konsumen
    public static final String DETAIL_KONSUMEN = "detailKonsumen";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    //Ulasan
    public static final String ID_ULASAN = "idUlasan";
    public static final String ULASAN = "ulasan";
    public static final String ULASAN_STATUS = "ulasanStatus";
    public static final String WAKTU_ULASAN = "waktuUlasan";
    public static final String TEXT_ULASAN = "textUlasan";
    public static final String RATING_PRODUK = "ratingProduk";
    public static final String RATING_PELAYANAN = "ratingPelayanan";
    //permission
    public static final int GALLERY_INTENT = 2;
    public static final int CAMERA_INTENT = 3;
    public static String USER_FILE_PATH = null;
    public static final int REQUEST_CODE_READ_STORAGE = 1001;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001;
    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001;
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CODE_CAMERA = 1002;
    public static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int RESULT_LOAD_IMAGE = 1;
    // Used in checking for runtime permissions.
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
//    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001;

}
