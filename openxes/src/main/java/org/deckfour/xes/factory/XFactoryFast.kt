package org.deckfour.xes.factory

import org.deckfour.xes.model.XEvent
import org.deckfour.xes.model.impl.XAttributeContainerFast
import org.deckfour.xes.model.impl.XEventFast

class XFactoryFast : XFactoryBufferedImpl {
    constructor() : super()

    fun createEventFast(): XEventFast {
        return XEventFast()
    }

    fun createContainerFast(key: String): XAttributeContainerFast {
        return XAttributeContainerFast(key)
    }
}