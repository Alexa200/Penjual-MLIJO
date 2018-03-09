package com.mlijo.aryaym.penjual_mlijo.DBModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AryaYM on 08/07/2017.
 */

public class ProdukModel implements Serializable {

    String userId, namaProduk, idKategori,  digitSatuan, namaSatuan, deskripsiProduk;
    String idProduk;
    Double hargaProduk;
    ArrayList<String> gambarProduk = new ArrayList<>();


    public ProdukModel(){}

    public String getUid() {
        return userId;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public String getKategoriProduk() {
        return idKategori;
    }

    public Double getHargaProduk() {
        return hargaProduk;
    }

    public String getSatuanProduk() {
        return digitSatuan;
    }

    public String getNamaSatuan() {
        return namaSatuan;
    }

    public String getDeskripsiProduk() {
        return deskripsiProduk;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public ArrayList<String> getGambarProduk() {
        return gambarProduk;
    }

}
