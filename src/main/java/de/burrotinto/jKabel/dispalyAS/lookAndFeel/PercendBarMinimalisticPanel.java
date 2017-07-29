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

package de.burrotinto.jKabel.dispalyAS.lookAndFeel;

import java.awt.*;

/**
 * Created by Florian Klinger on 22.07.16.
 */
public class PercendBarMinimalisticPanel extends MinimalisticPanel {
    private double percent;
    private Color c1, c2;

    public PercendBarMinimalisticPanel(LayoutManager layout, double percent, Color c1, Color c2) {
        super(layout);
        this.percent = percent;
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(c1);
        graphics.fillRect(0, 0, (int) (this.getWidth() * percent), getHeight());
        graphics.setColor(c2);
        graphics.fillRect((int) (this.getWidth() * percent), 0, getWidth(), getHeight());
        super.paint(graphics);
    }
}
