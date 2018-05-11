package at.bitfire.dav4android.property.owncloud

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser


class OCPrivatelink (
        link : String
): Property {
    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_OWNCLOUD, "privatelink")

        class Factory: PropertyFactory {
            override fun getName() = NAME

            override fun create(parser: XmlPullParser) : OCPrivatelink? {
                XmlUtils.readText(parser)?.let {
                    return OCPrivatelink(it)
                }
                return null
            }
        }
    }
}