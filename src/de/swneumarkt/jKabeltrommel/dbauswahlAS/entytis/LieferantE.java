package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 23.05.16.
 */
public class LieferantE {
    private int id;
    private String name;

    public LieferantE(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
