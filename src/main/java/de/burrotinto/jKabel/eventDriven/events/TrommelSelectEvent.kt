package de.burrotinto.jKabel.eventDriven.events

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown=true)
class TrommelSelectEvent(val trommelId: Int)