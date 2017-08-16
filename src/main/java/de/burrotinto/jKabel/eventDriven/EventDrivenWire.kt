package de.burrotinto.jKabel.eventDriven

import de.burrotinto.jKabel.dispalyAS.bearbeiten.kabelTypAuswahlAS.KabelTypAuswahlAAS
import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS
import de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS.TrommelAuswahlAAS
import de.burrotinto.jKabel.eventDriven.EventDrivenConf
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import reactor.bus.EventBus
import reactor.bus.selector.Selectors

@Service
class EventDrivenWire(private val streckenAAS: StreckenAAS,
                      private val kabelTypAuswahlAAS: KabelTypAuswahlAAS,
                      private val trommelAuswahlAAS: TrommelAuswahlAAS,
                      private val eventBus: EventBus) :
        InitializingBean {


    companion object {
        val TROMMEL_SELECTED_REGISTRATION = "trommelSelected"
        val TYPE_SELECTED = "typSelected"
    }

    override fun afterPropertiesSet() {
        eventBus.on(Selectors.`$`(TROMMEL_SELECTED_REGISTRATION), streckenAAS)
        eventBus.on(Selectors.`$`(TROMMEL_SELECTED_REGISTRATION), kabelTypAuswahlAAS)
        eventBus.on(Selectors.`$`(TROMMEL_SELECTED_REGISTRATION), trommelAuswahlAAS)
    }
}