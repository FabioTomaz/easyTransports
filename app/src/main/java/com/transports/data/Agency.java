package com.transports.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Agency  implements Serializable {
    private String agency_key;
    private String agency_id;
    private String agency_name;
    private String agency_url;

    private String agency_timezone;

    private String agency_lang;
    private String agency_phone;
    private ArrayList<Double> agency_center;
    public Agency(String agency_key, String agency_id, String agency_url, String agency_timezone, String agency_lang, String agency_phone, ArrayList<Double> agency_center) {
        this.agency_key = agency_key;
        this.agency_id = agency_id;
        this.agency_url = agency_url;
        this.agency_timezone = agency_timezone;
        this.agency_lang = agency_lang;
        this.agency_phone = agency_phone;
        this.agency_center = agency_center;
    }

    public String getAgency_key() {
        return agency_key;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public void setAgency_key(String agency_key) {
        this.agency_key = agency_key;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getAgency_url() {
        return agency_url;
    }

    public void setAgency_url(String agency_url) {
        this.agency_url = agency_url;
    }

    public String getAgency_timezone() {
        return agency_timezone;
    }

    public void setAgency_timezone(String agency_timezone) {
        this.agency_timezone = agency_timezone;
    }

    public String getAgency_lang() {
        return agency_lang;
    }

    public void setAgency_lang(String agency_lang) {
        this.agency_lang = agency_lang;
    }

    public String getAgency_phone() {
        return agency_phone;
    }

    public void setAgency_phone(String agency_phone) {
        this.agency_phone = agency_phone;
    }

    public ArrayList<Double> getAgency_center() {
        return agency_center;
    }

    public void setAgency_center(ArrayList<Double> agency_center) {
        this.agency_center = agency_center;
    }
}
