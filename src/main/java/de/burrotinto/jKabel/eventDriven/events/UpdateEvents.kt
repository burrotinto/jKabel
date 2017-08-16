package de.burrotinto.jKabel.eventDriven.events

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE

/**
 * Created by Florian Klinger on 16.08.17, 15:01.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
class UpdateEvent(val trommel: ITrommelE)