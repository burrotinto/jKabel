package de.burrotinto.jKabel.eventDriven

import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS
import de.burrotinto.jKabel.eventDriven.EventDrivenConf
import org.springframework.beans.factory.InitializingBean
import reactor.bus.EventBus
import reactor.bus.selector.Selectors

class EventDrivenWire(private val streckenAAS: StreckenAAS, private val eventBus: EventBus) : InitializingBean {


    companion object {
        val TROMMEL_SELECTED_REGISTRATION = "trommelSelected"
    }

    override fun afterPropertiesSet() {
        eventBus.on(Selectors.`$`(TROMMEL_SELECTED_REGISTRATION), streckenAAS)
    }
}