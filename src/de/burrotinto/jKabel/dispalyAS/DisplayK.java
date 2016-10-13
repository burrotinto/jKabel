/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.jKabel.dispalyAS;

import de.burrotinto.jKabel.config.ConfigReader;
import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS;
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS;
import de.burrotinto.jKabel.dispalyAS.search.trommelByNummer.SearchTrommelNrAAS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Created by derduke on 29.09.16.
 */
@Configuration
public class DisplayK {

    @Autowired
    IDBWrapper db;

    @Bean
    public StreckenAAS getStreckenAAS(IDBWrapper db) {
        return new StreckenAAS(db, updateSet());
    }

    @Bean
    public HashSet<JPanel> updateSet() {
        return new HashSet<JPanel>();
    }

    @Bean
    public TrommelAuswahlAAS getTrommelAuswahlAAS(IDBWrapper db) {
        TrommelAuswahlAAS t = new TrommelAuswahlAAS(db);
        updateSet().add(t);
        return t;
    }

    @Bean
    public KabelTypAuswahlAAS getKabelTypAuswahlAAS(IDBWrapper db) {
        KabelTypAuswahlAAS k = new KabelTypAuswahlAAS(db);
        updateSet().add(k);
        return k;
    }

    @Bean(name = "bearbeitenPanel")
    public JPanel getBearbeitenPanel(KabelTypAuswahlAAS kabelTypAuswahlAAS, TrommelAuswahlAAS tommelAAs, StreckenAAS streckenAAS, Color background) {

        kabelTypAuswahlAAS.addKabelTypListner(tommelAAs);
        kabelTypAuswahlAAS.addKabelTypListner(streckenAAS);

        tommelAAs.addTrommelListner(streckenAAS);

        JPanel l = new JPanel(new GridLayout(1, 2));
        JPanel all = new JPanel(new GridLayout(1, 2));

        JScrollPane kSP = new JScrollPane(kabelTypAuswahlAAS);
        l.add(kSP);
        l.add(new JScrollPane(tommelAAs));
        all.add(l);
        JScrollPane sc = new JScrollPane(streckenAAS);
        sc.setPreferredSize(new Dimension(740, 740));
        all.add(sc);

        kSP.setOpaque(false);
        sc.setOpaque(false);

        l.setBackground(background);
        kabelTypAuswahlAAS.setBackground(background);
        tommelAAs.setBackground(background);
        streckenAAS.setBackground(background);

        try {
            streckenAAS.setLogo(ImageIO.read(new File(ConfigReader.getInstance().getPath() + "logo.jpg")));
        } catch (IOException e) {
            streckenAAS.setLogo(null);
        }
        all.setBackground(background);
        return all;
    }

    @Bean
    public Color background() {
        return Color.WHITE;
    }

    @Bean(name = "trommeln")
    public JMenu allTrommel(IDBWrapper db) {
        JMenu m = new JMenu("Trommeln");

        db.getAllKabeltypen().forEach(new Consumer<IKabeltypE>() {
            @Override
            public void accept(IKabeltypE iKabeltypE) {
                iKabeltypE.getTrommeln().forEach(new Consumer<ITrommelE>() {
                    @Override
                    public void accept(ITrommelE iTrommelE) {
                        m.add(iTrommelE.getId() + " " + iTrommelE.getTrommelnummer());
                    }
                });
            }
        });
        return m;
    }

    @Bean
    public SearchTrommelNrAAS searchTrommelNrAAS(StreckenAAS aas, IDBWrapper db) {
        return new SearchTrommelNrAAS(aas, db);
    }

}
