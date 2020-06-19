package at.bitfire.dav4jvm.property

import at.bitfire.dav4jvm.Property
import at.bitfire.dav4jvm.PropertyFactory
import at.bitfire.dav4jvm.XmlUtils
import org.xmlpull.v1.XmlPullParser

data class OCId(
        val id: String
) : Property {
    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_OWNCLOUD, "id")
    }

    class Factory : PropertyFactory {
        override fun getName() = NAME

        override fun create(parser: XmlPullParser): OCId? {
            XmlUtils.readText(parser)?.let {
                return OCId(it)
            }
            return null
        }
    }
}