package org.deckfour.xes.model.impl

import org.deckfour.xes.extension.XExtension
import org.deckfour.xes.factory.XFactory
import org.deckfour.xes.factory.XFactoryRegistry
import org.deckfour.xes.id.XID
import org.deckfour.xes.model.XAttributeContainer
import org.deckfour.xes.model.XAttributeList
import org.deckfour.xes.model.XAttributeMap

class XEventFast : XEventImpl {
    constructor() : super()
    constructor(id: XID?) : super(id)
    constructor(attributes: XAttributeMap?) : super(attributes)
    constructor(id: XID?, attributes: XAttributeMap?) : super(id, attributes)

    private val factory: XFactory
        get() = XFactoryRegistry.instance().currentDefault()

    fun addAttribute(key: String, value: String, extension: XExtension) : XEventFast {
        attributes.put(key, factory.createAttributeLiteral(key, value, extension))
        return this;
    }

    fun addAttribute(key: String, value: String) : XEventFast {
        attributes.put(key, factory.createAttributeLiteral(key, value, null))
        return this;
    }

    fun addAttribute(key: String, value: Number, extension: XExtension) : XEventFast {
        attributes.put(key, factory.createAttributeDiscrete(key, value.toLong(), extension))
        return this;
    }

    fun addAttribute(key: String, value: Number) : XEventFast {
        attributes.put(key, factory.createAttributeDiscrete(key, value.toLong(), null))
        return this;
    }

    fun addAttribute(key: String, value: Double) : XEventFast {
        attributes.put(key, factory.createAttributeContinuous(key, value, null))
        return this;
    }

    fun addAttribute(key: String, value: Boolean) : XEventFast {
        attributes.put(key, factory.createAttributeBoolean(key, value, null))
        return this;
    }

    fun addAttribute(key: String, value: XAttributeContainer)  : XEventFast{
        attributes.put(key, factory.createAttributeContainer(key, null))
        return this;
    }

    fun addAttribute(key: String, value: XAttributeList) : XEventFast {
        attributes.put(key, factory.createAttributeList(key, null))
        return this;
    }

}