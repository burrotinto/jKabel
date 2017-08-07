package de.burrotinto.jKabel.eventDriven

import de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS.StreckenAAS

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.Environment
import reactor.bus.EventBus
import reactor.bus.selector.Selectors.`$`

@Service
class EventDrivenConf{

    @Bean
    internal fun env(): Environment {
        return Environment.initializeIfEmpty().assignErrorJournal()
    }


    @Bean
    internal fun createEventBus(env: Environment): EventBus {
        return EventBus.create(env, Environment.THREAD_POOL)
    }
}
