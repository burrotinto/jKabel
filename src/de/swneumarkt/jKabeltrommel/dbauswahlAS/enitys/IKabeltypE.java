package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Created by derduke on 25.05.16.
 */
public interface IKabeltypE extends Serializable {
    String getTyp();

    void setTyp(String typ);

    int getMaterialNummer();
}
