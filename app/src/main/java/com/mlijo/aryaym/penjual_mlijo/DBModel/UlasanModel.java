package com.mlijo.aryaym.penjual_mlijo.DBModel;

import java.io.Serializable;

/**
 * Created by AryaYM on 30/10/2017.
 */

public class UlasanModel implements Serializable {
    String idKonsumen, idPenjual, idUlasan, textUlasan;
    String idKategori, tipeTransaksi, idProduk;
    long waktuUlasan;
    float ratingProduk, ratingPelayanan;

    public UlasanModel(){}

    public float getRatingProduk() {
        return ratingProduk;
    }

    public float getRatingPelayanan() {
        return ratingPelayanan;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public String getTipeTransaksi() {
        return tipeTransaksi;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public String getIdKonsumen() {
        return idKonsumen;
    }

    public String getIdPenjual() {
        return idPenjual;
    }

    public String getIdUlasan() {
        return idUlasan;
    }

    public String getTextUlasan() {
        return textUlasan;
    }

    public long getWaktuUlasan() {
        return waktuUlasan;
    }
}
