package com.bertolero.scuoledonorionefano.notizie;

public class Notizia {
    private String titolo;
    private String link;
    private String descrizione;
    private String image;


    public Notizia(){

    }

    public Notizia(String titolo, String link){
        this.titolo = titolo;
        this.link = link;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
