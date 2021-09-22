package org.deckfour.xes.model.impl

import org.deckfour.xes.extension.XExtension
import org.deckfour.xes.factory.XFactory
import org.deckfour.xes.factory.XFactoryRegistry
import org.deckfour.xes.model.XAttributeContainer
import org.deckfour.xes.model.XAttributeList

class XAttributeContainerFast : XAttributeContainerImpl {
    constructor(key: String?) : super(key)
    constructor(key: String?, extension: XExtension?) : super(key, extension)

    private val factory: XFactory
        get() = XFactoryRegistry.instance().currentDefault()

    fun addAttribute(key: String, value: String, extension: XExtension): XAttributeContainerFast {
        addToCollection(factory.createAttributeLiteral(key, value, extension))
        return this
    }

    fun addAttribute(key: String, value: String): XAttributeContainerFast {
        addToCollection(factory.createAttributeLiteral(key, value, null))
        return this
    }

    fun addAttribute(key: String, value: Number, extension: XExtension): XAttributeContainerFast {
        addToCollection(factory.createAttributeDiscrete(key, value.toLong(), extension))
        return this
    }

    fun addAttribute(key: String, value: Number): XAttributeContainerFast {
        addToCollection(factory.createAttributeDiscrete(key, value.toLong(), null))
        return this
    }

    fun addAttribute(key: String, value: Double): XAttributeContainerFast {
        addToCollection(factory.createAttributeContinuous(key, value, null))
        return this
    }

    fun addAttribute(key: String, value: Boolean): XAttributeContainerFast {
        addToCollection(factory.createAttributeBoolean(key, value, null))
        return this
    }

    fun addAttribute(key: String, value: XAttributeContainer): XAttributeContainerFast {
        addToCollection(factory.createAttributeContainer(key, null))
        return this
    }

    fun addAttribute(key: String, value: XAttributeList): XAttributeContainerFast {
        addToCollection(factory.createAttributeList(key, null))
        return this
    }

}