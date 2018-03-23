package com.mlijo.aryaym.penjual_mlijo.KelolaProduk;

import android.text.TextUtils;

/**
 * Created by AryaYM on 13/02/2018.
 */

public class FilterProduk {

    private String kategori = null;
    private String penjualId = null;

    public FilterProduk(){}

    public static FilterProduk getDefault(String penjualId){
        FilterProduk filterProduk = new FilterProduk();
        filterProduk.setPenjualId(penjualId);
        return filterProduk;
    }

    public boolean hasKategori(){
        return !(TextUtils.isEmpty(kategori));
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getPenjualId() {
        return penjualId;
    }

    public void setPenjualId(String penjualId) {
        this.penjualId = penjualId;
    }
}
