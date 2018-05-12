package com.mlijo.aryaym.penjual_mlijo.InformasiKonsumen;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by AryaYM on 27/10/2017.
 */

public class KonsumenModel implements Serializable {
    private String avatar;
    private String nama;
    private String noTelp;
    private String uid;
    private String deviceToken;
    private String alamat;
    private Map<String, Object> detailKonsumen;

    public Map<String, Object> getDetailKonsumen() {
        return detailKonsumen;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
}
