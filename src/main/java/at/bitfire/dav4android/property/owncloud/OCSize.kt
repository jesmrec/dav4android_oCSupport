package at.bitfire.dav4android.property.owncloud

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser

data class OCSize(
        val size: Long
) : Property {
    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_OWNCLOUD, "size")

        class Factory : PropertyFactory {
            override fun getName() = NAME

            override fun create(parser: XmlPullParser): OCSize? {
                XmlUtils.readText(parser)?.let {
                    return OCSize(it.toLong())
                }
                return null
            }
        }
    }
}