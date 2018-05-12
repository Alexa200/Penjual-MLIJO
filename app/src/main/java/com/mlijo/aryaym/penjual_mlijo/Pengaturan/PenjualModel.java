package com.mlijo.aryaym.penjual_mlijo.Pengaturan;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AryaYM on 09/09/2017.
 */

public class PenjualModel implements Serializable {
    private String avatar;
    private String email;
    private String nama;
    private String noKTP;
    private String noTelp;
    private String uid;
    private String deviceToken;
    private String alamat;
    private String judulAlamat;
    private String alamatId;
    private boolean statusLokasi, statusBerjualan;
    private String namaPenerima;
    private Map<String, Object> infoKategori;
    private Map<String, Object> infoLokasi;
    private Map<String, Object> detailPenjual;
    private HashMap<String, Object> alamatUser;

    public PenjualModel(){}

    public Map<String, Object> getDetailPenjual() {
        return detailPenjual;
    }

    public Map<String, Object> getInfoLokasi() {
        return infoLokasi;
    }

    public Map<String, Object> getInfoKategori() {
        return infoKategori;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJudulAlamat() {
        return judulAlamat;
    }

    public void setJudulAlamat(String judulAlamat) {
        this.judulAlamat = judulAlamat;
    }

    public String getAlamatId() {
        return alamatId;
    }

    public void setAlamatId(String alamatId) {
        this.alamatId = alamatId;
    }

    public HashMap<String, Object> getAlamatUser() {
        return alamatUser;
    }

    public void setAlamatUser(HashMap<String, Object> alamatUser) {
        this.alamatUser = alamatUser;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public boolean isStatusLokasi() {
        return statusLokasi;
    }

    public boolean isStatusBerjualan() {
        return statusBerjualan;
    }
}
