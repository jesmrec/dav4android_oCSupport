package at.bitfire.dav4jvm.property

import at.bitfire.dav4jvm.Property
import at.bitfire.dav4jvm.PropertyFactory
import at.bitfire.dav4jvm.XmlUtils
import org.xmlpull.v1.XmlPullParser

data class OCPrivatelink(
        val link: String
) : Property {
    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_OWNCLOUD, "privatelink")
    }

    class Factory : PropertyFactory {
        override fun getName() = NAME

        override fun create(parser: XmlPullParser): OCPrivatelink? {
            XmlUtils.readText(parser)?.let {
                return OCPrivatelink(it)
            }
            return null
        }
    }
}