package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Repräsentiert eine Lieferung einer Trommel von einem bestimmten Lieferanten
 * Created by derduke on 25.05.16.
 */
public interface IGeliefertE extends Serializable {
    /**
     * Gibt den Lieferzeitpunkt zurück
     *
     * @return in ms seit 1970
     */
    long getDatum();

    /**
     * Jede Lieferung hat einen Lieferschein. Hier kann sie abgefragt werden
     * @return die Lieferscheinnummer oder <code>NULL</code>
     */
    String getLieferscheinNr();

    /**
     * Die Gelieferte Trommel
     * @return eine Trommel
     */
    ITrommelE getTrommel();

    /**
     * Von wem die Kabeltrommel geliefert wurde
     * @return der Lieferant
     */
    ILieferantE getLieferant();

    /**
     * Setter für den Lieferanten
     * @param lieferant der Lieferant
     */
    void setLieferantID(ILieferantE lieferant);
}
