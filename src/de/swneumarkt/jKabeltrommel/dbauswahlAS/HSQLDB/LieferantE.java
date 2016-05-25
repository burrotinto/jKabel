package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ILieferantE;

/**
 * Created by derduke on 23.05.16.
 */
class LieferantE implements ILieferantE {
    private int id;
    private String name;

    LieferantE(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        try{
            return id == ((LieferantE)obj).id;
        } catch (Exception e){
            return false;
        }
    }
}
