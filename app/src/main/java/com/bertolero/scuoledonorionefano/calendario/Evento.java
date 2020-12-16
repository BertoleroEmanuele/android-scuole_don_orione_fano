package com.bertolero.scuoledonorionefano.calendario;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Evento implements Comparable<Evento>, Parcelable {

    private String nome;
    private int type; //-1 evento normale su più giorni, 0 evento normale in un giorno, 1 evento tutto il giorno, 2 evento tutto il giorno su più giorni
    private Date inizio;
    private Date fine;
    private String posizione = null;

    public Evento(){}

    protected Evento(Parcel in) {
        nome = in.readString();
        type = in.readInt();
        inizio = (java.util.Date) in.readSerializable();
        fine = (java.util.Date) in.readSerializable();
        posizione = in.readString();
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    @Override
    public int compareTo(Evento o) {
        return getInizio().compareTo(o.getInizio());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeInt(type);
        dest.writeSerializable(inizio);
        dest.writeSerializable(fine);
        dest.writeString(posizione);
    }
}
