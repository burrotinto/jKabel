package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Created by derduke on 25.05.16.
 */
public interface ITrommelE extends Serializable {
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

    boolean isFreigemeldet();

    @Override
    String toString();

    void setFreimeldung(boolean freigemeldet);
}
