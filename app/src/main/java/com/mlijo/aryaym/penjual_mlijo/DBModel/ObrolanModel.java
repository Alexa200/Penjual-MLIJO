package com.mlijo.aryaym.penjual_mlijo.DBModel;

import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AryaYM on 24/10/2017.
 */

public class ObrolanModel {
    private String konten;
    private boolean kontenFoto;
    private boolean kontenPengirim;
    private long timestamp;
    private String idPengirim;
    private String idPenerima;
    private String obrolanId;
    private boolean displayStatus;

    public ObrolanModel(){}

    public ObrolanModel(String konten, boolean kontenFoto, long timestamp, String pengirim){
        this.konten = konten;
        this.kontenFoto = kontenFoto;
        this.timestamp = timestamp;
        this.idPengirim = pengirim;
    }

    public boolean isDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public boolean isKontenFoto() {
        return kontenFoto;
    }

    public void setKontenFoto(boolean kontenFoto) {
        this.kontenFoto = kontenFoto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getObrolanId() {
        return obrolanId;
    }

    public void setObrolanId(String obrolanId) {
        this.obrolanId = obrolanId;
    }

    public String getIdPengirim() {
        return idPengirim;
    }

    public String getIdPenerima() {
        return idPenerima;
    }

    public void setIdPenerima(String idPenerima) {
        this.idPenerima = idPenerima;
    }

    public boolean isKontenPengirim() {
        return kontenPengirim;
    }

    public void setKontenPengirim(boolean kontenPengirim) {
        this.kontenPengirim = kontenPengirim;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> mapDataObrolan = new HashMap<>();
        mapDataObrolan.put(Constants.KONTEN, konten);
        mapDataObrolan.put(Constants.KONTEN_FOTO, kontenFoto);
        mapDataObrolan.put(Constants.KONTEN_PENGIRIM, kontenPengirim);
        mapDataObrolan.put(Constants.ID_PENGIRIM, idPengirim);
        mapDataObrolan.put(Constants.ID_PENERIMA, idPenerima);
        mapDataObrolan.put(Constants.TIMESTAMP, timestamp);
        return mapDataObrolan;
    }
}
