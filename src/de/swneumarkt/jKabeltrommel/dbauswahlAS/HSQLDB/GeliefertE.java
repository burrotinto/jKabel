package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IGeliefertE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ILieferantE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;

/**
 * Created by derduke on 23.05.16.
 */
class GeliefertE implements IGeliefertE {
    private ILieferantE lieferant;
    private  long datum;
    private String lieferscheinNr;
    private ITrommelE trommel;

    GeliefertE(long datum, String lieferscheinNr, ILieferantE lieferant, ITrommelE trommel) {
        this.lieferant = lieferant;
        this.trommel = trommel;
        this.lieferscheinNr = lieferscheinNr;
        this.datum = datum;
    }

    @Override
    public long getDatum() {
        return datum;
    }

    @Override
    public String getLieferscheinNr() {
        return lieferscheinNr;
    }

    @Override
    public ITrommelE getTrommel() {
        return trommel;
    }

    @Override
    public ILieferantE getLieferant() {
        return lieferant;
    }

    public void setLieferantID(ILieferantE lieferant) {
        this.lieferant = lieferant;
    }
}
