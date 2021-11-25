package com.example.simplebookwormapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Formats implements Parcelable {
    @SerializedName("image/jpeg")
    private String image_jpeg;
    @SerializedName("application/epub+zip")
    private String epub_zip;
    @SerializedName("application/rdf+xml")
    private String rdf_xml;
    @SerializedName("text/html; charset=utf-8")
    private String text_html_utf8;
    @SerializedName("application/x-mobipocket-ebook")
    private String x_mobipocket_ebook;
    @SerializedName("application/zip")
    private String zip;
    @SerializedName("text/plain; charset=utf-8")
    private String text_plain_utf_8;

    public String getText_plain_ascii() {
        return text_plain_ascii;
    }

    public void setText_plain_ascii(String text_plain_ascii) {
        this.text_plain_ascii = text_plain_ascii;
    }

    @SerializedName("text/plain; charset=us-ascii")
    private String text_plain_ascii;




    public String getImage_jpeg() {
        return image_jpeg;
    }

    public void setImage_jpeg(String image_jpeg) {
        this.image_jpeg = image_jpeg;
    }

    public String getEpub_zip() {
        return epub_zip;
    }

    public void setEpub_zip(String epub_zip) {
        this.epub_zip = epub_zip;
    }

    public String getRdf_xml() {
        return rdf_xml;
    }

    public void setRdf_xml(String rdf_xml) {
        this.rdf_xml = rdf_xml;
    }

    public String getText_html_utf8() {
        return text_html_utf8;
    }

    public void setText_html_utf8(String text_html_utf8) {
        this.text_html_utf8 = text_html_utf8;
    }

    public String getX_mobipocket_ebook() {
        return x_mobipocket_ebook;
    }

    public void setX_mobipocket_ebook(String x_mobipocket_ebook) {
        this.x_mobipocket_ebook = x_mobipocket_ebook;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getText_plain_utf_8() {
        return text_plain_utf_8;
    }

    public void setText_plain_utf_8(String text_plain_utf_8) {
        this.text_plain_utf_8 = text_plain_utf_8;
    }

    public static Creator<Formats> getCREATOR() {
        return CREATOR;
    }

    public Formats(Parcel in) {
        image_jpeg = in.readString();
        epub_zip = in.readString();
        rdf_xml = in.readString();
        text_html_utf8 = in.readString();
        x_mobipocket_ebook = in.readString();
        zip = in.readString();
        text_plain_utf_8 = in.readString();
        text_plain_ascii = in.readString();
    }

    public static final Creator<Formats> CREATOR = new Creator<Formats>() {
        @Override
        public Formats createFromParcel(Parcel in) {
            return new Formats(in);
        }

        @Override
        public Formats[] newArray(int size) {
            return new Formats[size];
        }
    };

    public Formats() {
    }

    public Formats(String image_jpeg, String epub_zip, String rdf_xml, String text_html_utf8, String x_mobipocket_ebook, String zip, String text_plain_utf_8, String text_plain_ascii) {
        this.image_jpeg = image_jpeg;
        this.epub_zip = epub_zip;
        this.rdf_xml = rdf_xml;
        this.text_html_utf8 = text_html_utf8;
        this.x_mobipocket_ebook = x_mobipocket_ebook;
        this.zip = zip;
        this.text_plain_utf_8 = text_plain_utf_8;
        this.text_plain_ascii = text_plain_ascii;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_jpeg);
        parcel.writeString(epub_zip);
        parcel.writeString(rdf_xml);
        parcel.writeString(text_html_utf8);
        parcel.writeString(x_mobipocket_ebook);
        parcel.writeString(zip);
        parcel.writeString(text_plain_utf_8);
        parcel.writeString(text_plain_ascii);
    }
}
