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

package de.burrotinto.jKabel.config

import de.burrotinto.jKabel.JKabelS
import de.burrotinto.jKabel.config.trommelSort.AbstractTrommelSort
import de.burrotinto.jKabel.config.trommelSort.Richtung
import de.burrotinto.jKabel.config.trommelSort.TrommelIntelligentSort
import de.burrotinto.jKabel.config.typSort.AbstractTypeSort
import de.burrotinto.jKabel.config.typSort.TypNameSort
import de.burrotinto.jKabel.config.typSort.TypeFrequenzSort
import de.burrotinto.jKabel.config.typSort.TypeMatNrSort
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*

/**
 * Einfacher Propertysreader. ISt ein Singelton
 * Created by derduke on 01.06.16.
 */
@Service
class ConfigReader(val allTrommelSort: List<AbstractTrommelSort>, val allTypeSort:  List<AbstractTypeSort> ) {
    private val propFile = File(System.getProperty("user.home") + File.separator + "jKabel.prop")

    private var prop: Properties? = null

    private var alleTypSortierer: HashMap<String, AbstractTypeSort>? = null

    /**
     * Gibt den Pfad zur DB zurück

     * @return Pfad zu der zu benutzenden DB ODER NULL  wenn nicht gestetzt
     */
    val path: String?
        get() {
            var path: String? = null
            try {
                val prop = properties
                if (prop.getProperty(DBPFADPROP) != null) {
                    path = prop.getProperty(DBPFADPROP)
                }
            } catch (e: IOException) {
                return null
            }

            return path
        }

    private // lazy implementation
    val properties: Properties
        @Throws(IOException::class)
        get() {
            if (prop == null) {
                prop = Properties()
                if (!propFile.exists()) propFile.createNewFile()
                prop!!.load(FileReader(propFile))
            }
            return prop!!
        }

    /**
     * Zum Speichern des Pfades

     * @param path wo die DB liegt
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun savePath(path: String) {
        prop!!.setProperty(DBPFADPROP, path)
        save()
    }

    @Throws(IOException::class)
    private fun save() {
        prop!!.store(FileWriter(propFile), "Konfigurationsdatei des JKabel")
        log.info("save:")
        for ((key, value) in properties) {
            log.info(key.toString() + " " + value)
        }
    }

    @Throws(IOException::class)
    fun setTypSort(typSort: String) {
        prop!!.setProperty(SORTTYP, typSort)
        save()
    }

    @Throws(IOException::class)
    fun setTypeInOrder(inOrder: Boolean) {
        prop!!.setProperty(SORTTYPINORDER, java.lang.Boolean.toString(inOrder))
        save()
    }

    //Default Wert speichern
    val kabeltypSort: Comparator<in IKabeltypE>
        get() {
            try {
                if (properties.getProperty(SORTTYP) == null) {
                    try {
                        setTypSort("matnr")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

                var c: AbstractTypeSort? = alleTypSortierer!![properties.getProperty(SORTTYP)]
                if (c == null) c = TypeMatNrSort()
                c.isInOrder = isTypeInOrder!!
                return c

            } catch (e: Exception) {
                return TypeMatNrSort()
            }

        }

    val isTypeInOrder: Boolean?
        get() {
            try {
                return properties.getProperty(SORTTYPINORDER) == null || java.lang.Boolean.parseBoolean(properties.getProperty(SORTTYPINORDER))
            } catch (e: IOException) {
                e.printStackTrace()
                return true
            }

        }

//    //Default Wert speichern
//    val trommelSort: Comparator<ITrommelE>
//        get() {
//            var c: AbstractTrommelSort
//            try {
//                if (properties.getProperty(SORTTROMMEL) == null) {
//                    try {
//                        setTrommelSort("TrommelIntelligentSort.class")
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//
//                }
//
//                c = context.getBean<*>(Class.forName(prop!!.getProperty(SORTTROMMEL)))
//
//            } catch (e: Exception) {
//                c = context.getBean(TrommelIntelligentSort::class.java)
//            }
//
//            context.getBean(Richtung::class.java).isAufsteigend = isTrommelInOrder
//            return c
//        }

    @Throws(IOException::class)
    fun setTrommelSort(typSort: String) {
        prop!!.setProperty(SORTTROMMEL, typSort)
        save()
    }

    private var isTrommelInOrder: Boolean
        get() {
            try {
                return properties.getProperty(SORTTROMMELINORDER) == null || java.lang.Boolean.parseBoolean(properties.getProperty(SORTTROMMELINORDER))
            } catch (e: IOException) {
                e.printStackTrace()
                return true
            }

        }
        @Throws(IOException::class)
        set(inOrder) {
            prop!!.setProperty(SORTTROMMELINORDER, java.lang.Boolean.toString(inOrder))
            save()
        }

    val allTypSort: Collection<AbstractTypeSort>
        get() {
            if (alleTypSortierer == null) {
                alleTypSortierer = HashMap<String, AbstractTypeSort>()
                alleTypSortierer!!.put(TypeFrequenzSort::class.java.name, TypeFrequenzSort())
                alleTypSortierer!!.put(TypeMatNrSort::class.java.name, TypeMatNrSort())
                alleTypSortierer!!.put(TypNameSort::class.java.name, TypNameSort())
            }
            return alleTypSortierer!!.values
        }

    var isZeigeAlle: Boolean
        get() {
            try {
                return properties.getProperty(ZEIGEALLE) != null && java.lang.Boolean.parseBoolean(properties.getProperty(ZEIGEALLE))
            } catch (e: IOException) {
                return false
            }

        }
        @Throws(IOException::class)
        set(zeigeAlle) {
            prop!!.setProperty(ZEIGEALLE, java.lang.Boolean.toString(zeigeAlle))
            save()
        }



    companion object {
        val SWNPFAD = "O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\"
        private val DBPFADPROP = "DB.PFAD"
        private val SORTTYP = "SORT.TYP"
        private val SORTTYPINORDER = "SORT.TYP.INORDER"
        private val SORTTROMMELINORDER = "SORT.TROMMEL.INORDER"
        private val SORTTROMMEL = "SORT.TROMMEL"
        private val ZEIGEALLE = "SORT.TROMMEL.ZEIGEALLE"

        private val log = Logger.getLogger(ConfigReader::class.java)
    }
}
