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

package de.burrotinto.jKabel.dispalyAS

import de.burrotinto.jKabel.config.ConfigReader
import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE
import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS
import de.burrotinto.jKabel.dispalyAS.search.trommelByNummer.SearchTrommelNrAAS
import org.springframework.context.annotation.Bean
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.io.File
import java.io.IOException
import java.util.function.Consumer
import javax.imageio.ImageIO
import javax.swing.JMenu
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * Created by derduke on 29.09.16.
 */
@org.springframework.stereotype.Component
class DisplayK(var db: IDBWrapper,
               var updateSet: UpdateSet,
               var configReader: ConfigReader,
               val kabelTypAuswahlAAS: KabelTypAuswahlAAS,
               val tommelAAs: TrommelAuswahlAAS) {

    fun getBearbeitenPanel(): JPanel {
        val streckenAAS = StreckenAAS(db, updateSet)
        kabelTypAuswahlAAS.addKabelTypListner(tommelAAs)
        kabelTypAuswahlAAS.addKabelTypListner(streckenAAS)

        tommelAAs.addTrommelListner(streckenAAS)

        val l = JPanel(GridLayout(1, 2))
        val all = JPanel(GridLayout(1, 2))

        val kSP = JScrollPane(kabelTypAuswahlAAS)
        l.add(kSP)
        l.add(JScrollPane(tommelAAs))
        all.add(l)
        val sc = JScrollPane(streckenAAS)
        sc.preferredSize = Dimension(740, 740)
        all.add(sc)

        kSP.isOpaque = false
        sc.isOpaque = false

        l.background = background()
        kabelTypAuswahlAAS.background = background()
        tommelAAs.background = background()
        streckenAAS.background = background()

        try {
            streckenAAS.setLogo(ImageIO.read(File(configReader.path + "logo.jpg")))
        } catch (e: IOException) {
            streckenAAS.setLogo(null)
        }

        all.background = background()
        return all
    }

    @Bean
    fun background(): Color {
        return Color.WHITE
    }

    @Bean(name = arrayOf("trommeln"))
    fun allTrommel(db: IDBWrapper): JMenu {
        val m = JMenu("Trommeln")

        db.allKabeltypen.forEach(Consumer<IKabeltypE> { iKabeltypE -> iKabeltypE.trommeln.forEach(Consumer<ITrommelE> { iTrommelE -> m.add(iTrommelE.id.toString() + " " + iTrommelE.trommelnummer) }) })
        return m
    }

    @Bean
    fun searchTrommelNrAAS(db: IDBWrapper, updateOnChange: UpdateSet): SearchTrommelNrAAS {
        return SearchTrommelNrAAS(StreckenAAS(db, updateOnChange), db)
    }

}
