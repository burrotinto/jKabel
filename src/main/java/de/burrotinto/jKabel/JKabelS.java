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

package de.burrotinto.jKabel;


import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.HSQLDBServer;
import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;
import org.hsqldb.server.ServerAcl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Schnittstelle zum start von jKabel
 */
@SpringBootApplication
public class JKabelS implements InitializingBean {
    public static final String PROGRAMMNAME = "jKabel";

    @Autowired
    SplashScreen splashScreen;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, OnlyOneUserExeption, IOException, ServerAcl.AclFormatException, InterruptedException {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(JKabelS.class)
                .headless(false).web(false).run(args);

        if (args.length != 0) {
            System.out.println(JKabelS.getGPL("Florian Klinger"));
            System.out.println();
            if (args.length != 2 || !(args[0]).equals("server") && new File(args[1]).isDirectory()) {
                System.out.println("-->  To start as sever you must call with \"server path/for/db\" arguments  <--");
            } else {
                HSQLDBServer server = new HSQLDBServer(args[1]);
                System.out.println("Start Server on ip: " + server.getIP().getHostAddress());
                server.start();
            }
        }
    }

    public static String getGPL(String name) {
        return PROGRAMMNAME + " Copyright (C) " + (new Date().getYear() + 1900) + " " + name + "\nThis program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.\nThis is free software, and you are welcome to redistribute it\nunder certain conditions; type `show c' for details.";
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

