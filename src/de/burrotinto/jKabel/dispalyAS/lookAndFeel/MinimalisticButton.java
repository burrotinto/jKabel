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

import javax.swing.*;
import java.awt.*;

/**
 * Created by derduke on 31.05.16.
 */
public class MinimalisticButton extends JButton {
    private final Color defaultColor = Color.lightGray;
    private final Color selectedColor = Color.WHITE;

    public MinimalisticButton(String name) {
        super(name);
        setBackground(defaultColor);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(selectedColor);
        } else {
            setBackground(defaultColor);
        }
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((MinimalisticButton) obj).getText().equals(getText());
        } catch (Exception e) {
            return false;
        }
    }
}
