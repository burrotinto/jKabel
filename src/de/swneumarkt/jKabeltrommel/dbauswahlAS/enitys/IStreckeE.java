package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

/**
 * Created by derduke on 25.05.16.
 */
public interface IStreckeE {
    @Override
    String toString();

    int getBa();

    void setBa(int ba);

    int getEnde();

    void setEnde(int ende);

    int getStart();

    void setStart(int start);

    long getVerlegedatum();

    void setVerlegedatum(long verlegedatum);

    String getOrt();

    void setOrt(String ort);

    int getTrommelID();

    int getMeter();

    int getId();
}
