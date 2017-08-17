package de.burrotinto.jKabel

import org.springframework.stereotype.Component
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JFrame
import javax.swing.JLabel
import java.io.IOException
import java.io.FilterOutputStream
import java.lang.reflect.AccessibleObject.setAccessible



/**
 * Created by Florian Klinger on 17.08.17, 07:18.
 */

interface SplashScreen {
    fun dispose()
}

@Component
class SwingSplashScreen() : SplashScreen {
    val frame = JFrame()

    init {
        frame.name = "Starting ${JKabelS.PROGRAMMNAME}"
        frame.add(JLabel("Application is starting up"))
        frame.pack()

        frame.isVisible = true
    }

    override fun dispose() {
        frame.dispose()
    }
}
