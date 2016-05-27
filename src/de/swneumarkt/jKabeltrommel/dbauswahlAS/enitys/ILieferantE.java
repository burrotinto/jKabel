package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Created by derduke on 25.05.16.
 */
public interface ILieferantE extends Serializable {
    int getId();

    String getName();

    void setName(String name);
}
