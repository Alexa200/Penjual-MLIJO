package com.mlijo.aryaym.penjual_mlijo.DBModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AryaYM on 08/07/2017.
 */

public class ProdukModel implements Serializable {

    String userId, namaProduk, idKategori, namaSatuan, deskripsiProduk, idLokasi;
    String idProduk;
    Double hargaProduk;
    int digitSatuan;
    ArrayList<String> gambarProduk = new ArrayList<>();
    Long waktuDibuat;

    public ProdukModel(){}


    public ProdukModel(String penjualId, long timestamp, String namaProduk, String idKategori,
                       double hargaProduk, int satuanProduk, String namaSatuan, String idLokasi,
                       String deskripsiProduk){
        this.userId = penjualId;
        this.namaProduk = namaProduk;
        this.idKategori = idKategori;
        this.namaSatuan = namaSatuan;
        this.deskripsiProduk = deskripsiProduk;
        this.hargaProduk = hargaProduk;
        this.digitSatuan = satuanProduk;
        this.idLokasi = idLokasi;
        this.waktuDibuat = timestamp;
    }

    public ProdukModel(long timestamp, String namaProduk, double hargaProduk, int satuanProduk, String namaSatuan,
                       String deskripsiProduk){
        this.namaProduk = namaProduk;
        this.namaSatuan = namaSatuan;
        this.deskripsiProduk = deskripsiProduk;
        this.hargaProduk = hargaProduk;
        this.digitSatuan = satuanProduk;
        this.waktuDibuat = timestamp;
    }

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

    public int getSatuanProduk() {
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

    public Long getWaktuDibuat() {
        return waktuDibuat;
    }

    public String getIdLokasi() {
        return idLokasi;
    }
}
