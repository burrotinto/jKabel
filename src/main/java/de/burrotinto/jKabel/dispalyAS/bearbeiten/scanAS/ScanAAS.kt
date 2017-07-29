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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.scanAS


import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*


/**
 * Created by derduke on 31.05.16.
 */
class ScanAAS(trommelE: ITrommelE, strecke: IStreckeE) : JDialog(), ActionListener,
        AutoCloseable {

    init {
        layout = FlowLayout(FlowLayout.CENTER)

        add(JLabel(ImageIcon(getImageFromUrl(sanizizeString("http://barcode.tec-it.com/barcode.ashx?data=${strecke
                .ba}")))))

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel("Buchen: ${
        if (strecke.ende == -1)
            trommelE.gesamtlaenge - trommelE.strecken
                    .filter { it.ende != -1 }
                    .sumBy { it.meter }
        else strecke.meter} m"))
        if (trommelE.strecken.all { it.ende != -1 }) {
            panel.add(JLabel("Rückbuchen: ${trommelE.gesamtlaenge - trommelE.strecken.sumBy { it.meter }} m"))
        }
        add(panel)

        add(JLabel())
        if (trommelE.lagerPlatz == null || trommelE.lagerPlatz == "") {
            add(JLabel(ImageIcon(getImageFromUrl(sanizizeString("http://barcode.tec-it.com/barcode" +
                    ".ashx?data=${trommelE.materialNummer}")))))
        } else {
            add(JLabel(ImageIcon(getImageFromUrl(sanizizeString("http://barcode.tec-it.com/barcode" +
                    ".ashx?data=${trommelE.lagerPlatz + " " + trommelE.materialNummer}")))))
        }
        pack()

    }

    override fun actionPerformed(e: ActionEvent) {
        isVisible = !isVisible
    }

    @Throws(Exception::class)
    override fun close() {
        dispose()
    }

}

fun getImageFromUrl(url: String): BufferedImage = ImageIO.read(URL(url).openStream())

fun sanizizeString(s: String): String = s.replace(" ", "%20")