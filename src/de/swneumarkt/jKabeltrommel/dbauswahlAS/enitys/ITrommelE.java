package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

/**
 * Created by derduke on 25.05.16.
 */
public interface ITrommelE {
    int getStart();

    void setStart(int start);

    String getLagerPlatz();

    void setLagerPlatz(String lagerPlatz);

    int getMaterialNummer();

    int getGesamtlaenge();

    void setGesamtlaenge(int gesamtlaenge);

    int getId();

    String getTrommelnummer();

    void setTrommelnummer(String trommelnummer);

    @Override
    String toString();
}
