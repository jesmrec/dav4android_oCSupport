package at.bitfire.dav4android.property

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser
import kotlin.jvm.JvmField

data class GetContentLength(
        val contentLength: Long
) : Property {
    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_OWNCLOUD, "getcontentlength")

        class Factory : PropertyFactory {
            override fun getName() = NAME

            override fun create(parser: XmlPullParser): GetContentLength? {
                XmlUtils.readText(parser)?.let {
                    return GetContentLength(it.toLong())
                }
                return null
            }
        }
    }
}