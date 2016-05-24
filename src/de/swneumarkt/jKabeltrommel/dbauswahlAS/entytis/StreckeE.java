package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 20.05.16.
 */
public class StreckeE {
    //(id IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum DATE , start INTEGER , ende INTEGER)
//     id, trommelid, ba, ort,verlegedatum,start,ende
    private int id, ba,start,ende;
    private String ort;
    private long verlegedatum;
    private TrommelE trommel;

    public StreckeE(int id, int ba,String ort, long verlegedatum,  int start,  int ende,TrommelE trommel) {
        this.ba = ba;
        this.ende = ende;
        this.id = id;
        this.ort = ort;
        this.start = start;
        this.verlegedatum = verlegedatum;
        this.trommel=trommel;
    }

    @Override
    public String toString() {
        return "StreckeE{" +
                "id=" + id +
                ", ba=" + ba +
                ", start=" + start +
                ", ende=" + ende +
                ", ort='" + ort + '\'' +
                ", verlegedatum=" + verlegedatum +
                '}';
    }

    public StreckeE(int ba, String ort, long verlegedatum , int start, int ende,TrommelE trommel) {
        this(-1,ba,ort,verlegedatum,start,ende,trommel);
    }

    public int getBa() {
        return ba;
    }

    public void setBa(int ba) {
        this.ba = ba;
    }

    public int getEnde() {
        return ende;
    }

    public void setEnde(int ende) {
        this.ende = ende;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getVerlegedatum() {
        return verlegedatum;
    }

    public void setVerlegedatum(long verlegedatum) {
        this.verlegedatum = verlegedatum;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getTrommelID(){
        return trommel.getId();
    }

    public int getMeter() {
        return Math.max(start,ende) - Math.min(start,ende);
    }

    public int getId() {
        return id;
    }
}
