package de.swneumarkt.jKabeltrommel.entytis;

/**
 * Created by derduke on 19.05.2016.
 */
public class KabeltypE {
    private String name;
    private int nummer;

    @Override
    public String toString() {
        return "KabeltypE{" +
                "name='" + name + '\'' +
                ", nummer=" + nummer +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public KabeltypE(String name, int nummer) {
        this.name = name;
        this.nummer = nummer;

    }
}
