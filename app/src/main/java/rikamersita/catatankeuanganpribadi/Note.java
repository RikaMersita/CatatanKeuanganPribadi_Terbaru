package rikamersita.catatankeuanganpribadi;


import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int id;
    private String title;
    private int tipe;
    private String spntipe;
    private int kategori;
    private String spnkategori;
    private String description;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int  getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public String getSpntipe() {
        return spntipe;
    }

    public void setSpntipe(String spntipe) {
        this.spntipe = spntipe;
    }

    public int getKategori() {
        return kategori;
    }

    public void setKategori(int kategori) {
        this.kategori = kategori;
    }

    public String getSpnkategori() {
        return spnkategori;
    }

    public void setSpnkategori(String spnkategori) {
        this.spnkategori = spnkategori;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Creator<Note> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.tipe);
        dest.writeString(this.spntipe);
        dest.writeInt(this.kategori);
        dest.writeString(this.spnkategori);
        dest.writeString(this.description);
        dest.writeString(this.date);
    }

    public Note() {
    }

    protected Note(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.tipe = in.readInt();
        this.spntipe = in.readString();
        this.kategori = in.readInt();
        this.spnkategori = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
