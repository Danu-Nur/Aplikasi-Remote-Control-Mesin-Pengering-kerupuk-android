package com.example.bulangkulon_remote;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Struct;

public class DataMesin implements Parcelable {
    String iddata, mesin, status, suhu,suhuatas,suhubawah, batt, kompor, blower, listrik;

    public DataMesin(String iddata, String mesin, String status, String suhu, String suhuatas, String suhubawah, String batt, String kompor, String blower, String listrik) {
        this.iddata = iddata;
        this.mesin = mesin;
        this.status = status;
        this.suhu = suhu;
        this.suhuatas = suhuatas;
        this.suhubawah = suhubawah;
        this.batt = batt;
        this.kompor = kompor;
        this.blower = blower;
        this.listrik = listrik;
    }

    public DataMesin() { }

    protected DataMesin(Parcel in) {
        iddata = in.readString();
        mesin = in.readString();
        status = in.readString();
        suhu = in.readString();
        suhuatas = in.readString();
        suhubawah = in.readString();
        batt = in.readString();
        kompor = in.readString();
        blower = in.readString();
        listrik = in.readString();
    }


    public static final Creator<DataMesin> CREATOR = new Creator<DataMesin>() {
        @Override
        public DataMesin createFromParcel(Parcel in) {
            return new DataMesin(in);
        }

        @Override
        public DataMesin[] newArray(int size) {
            return new DataMesin[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iddata);
        dest.writeString(mesin);
        dest.writeString(status);
        dest.writeString(suhu);
        dest.writeString(suhuatas);
        dest.writeString(suhubawah);
        dest.writeString(batt);
        dest.writeString(kompor);
        dest.writeString(blower);
        dest.writeString(listrik);
    }

    public String getIddata() { return iddata; }

    public String getMesin() { return mesin; }

    public String getStatus() { return status; }

    public String getSuhu() { return suhu; }

    public String getSuhuatas() { return suhuatas; }

    public String getSuhubawah() { return suhubawah; }

    public String getBatt() { return batt; }

    public String getKompor() { return kompor; }

    public String getBlower() { return blower; }

    public String getListrik() { return listrik; }

    public void setIddata(String iddata) { this.iddata = iddata; }

    public void setMesin(String mesin) { this.mesin = mesin; }

    public void setStatus(String status) { this.status = status; }

    public void setSuhu(String suhu) { this.suhu = suhu; }

    public void setSuhuatas(String suhuatas) { this.suhuatas = suhuatas; }

    public void setSuhubawah(String suhubawah) { this.suhubawah = suhubawah; }

    public void setBatt(String batt) { this.batt = batt; }

    public void setKompor(String kompor) { this.kompor = kompor; }

    public void setBlower(String blower) { this.blower = blower; }

    public void setListrik(String listrik) { this.listrik = listrik; }
}
