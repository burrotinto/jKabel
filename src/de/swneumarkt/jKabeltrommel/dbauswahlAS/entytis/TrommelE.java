package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 20.05.16.
 */
public class TrommelE {
    private  int start;
    private String lagerPlatz;
    private int id;
    private String trommelnummer;
    private int gesamtlaenge;
    private final KabeltypE kabelTyp;

    public TrommelE(KabeltypE kabelTyp, int id, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start) {
        this.kabelTyp = kabelTyp;
        this.lagerPlatz = lagerPlatz;
        this.id = id;
        this.start = start;
        this.trommelnummer = trommelnummer;
        this.gesamtlaenge = gesamtlaenge;
    }


    public TrommelE(KabeltypE kabelTyp, String trommelnummer, int gesamtlaenge,String lagerPlatz, int start) {
        this.kabelTyp = kabelTyp;
        this.lagerPlatz = lagerPlatz;
        this.trommelnummer = trommelnummer;
        this.gesamtlaenge = gesamtlaenge;
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getLagerPlatz() {
        return lagerPlatz;
    }

    public void setLagerPlatz(String lagerPlatz) {
        this.lagerPlatz = lagerPlatz;
    }

    public int getMaterialNummer() {
        return kabelTyp.getMaterialNummer();
    }

    public int getGesamtlaenge() {
        return gesamtlaenge;
    }

    public void setGesamtlaenge(int gesamtlaenge) {
        this.gesamtlaenge = gesamtlaenge;
    }

    public int getId() {
        return id;
    }

    public String getTrommelnummer() {
        return trommelnummer;
    }


    public void setTrommelnummer(String trommelnummer) {
        this.trommelnummer = trommelnummer;
    }

    @Override
    public String toString() {
        return "TrommelE{" +
                "id=" + id +
                ", trommelnummer='" + trommelnummer + '\'' +
                ", gesamtlaenge=" + gesamtlaenge +
                ", kabelTyp=" + kabelTyp.toString() +
                '}';
    }
}
