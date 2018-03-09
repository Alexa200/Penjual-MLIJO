package com.mlijo.aryaym.penjual_mlijo.DBModel;

import java.io.Serializable;

/**
 * Created by AryaYM on 20/09/2017.
 */

public class TransaksiModel implements Serializable{

    String idTransaksi, idKonsumen, idPenjual, idProduk, idKategori, catatanKonsumen, penerima, jenisProduk, tanggalKirim, waktuKirim;
    Double totalHarga, biayaKirim;
    long tanggalPesan;
    int statusTransaksi, jumlahOrderProduk;
    boolean ulasanStatus;

    public TransaksiModel(){}

    public boolean isUlasan() {
        return ulasanStatus;
    }

    public long getTanggalPesan() {
        return tanggalPesan;
    }

    public void setTanggalPesan(long tanggalPesan) {
        this.tanggalPesan = tanggalPesan;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(String idKategori) {
        this.idKategori = idKategori;
    }

    public String getIdPenjual() {
        return idPenjual;
    }

    public void setIdPenjual(String idPenjual) {
        this.idPenjual = idPenjual;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(String idProduk) {
        this.idProduk = idProduk;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public String getCatatanKonsumen() {
        return catatanKonsumen;
    }

    public void setCatatanKonsumen(String catatanKonsumen) {
        this.catatanKonsumen = catatanKonsumen;
    }

    public int getStatusTransaksi() {
        return statusTransaksi;
    }

    public int getJumlahProduk() {
        return jumlahOrderProduk;
    }

    public Double getBiayaKirim() {
        return biayaKirim;
    }

    public String getPenerima() {
        return penerima;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getIdKonsumen() {
        return idKonsumen;
    }

    public String getJenisProduk() {
        return jenisProduk;
    }

    public String getTanggalKirim() {
        return tanggalKirim;
    }

    public String getWaktuKirim() {
        return waktuKirim;
    }
}
