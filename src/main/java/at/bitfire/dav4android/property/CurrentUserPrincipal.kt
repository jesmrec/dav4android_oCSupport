/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.bitfire.dav4android.property

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser

// see RFC 5397: WebDAV Current Principal Extension

data class CurrentUserPrincipal(
        val href: String?
): Property {

    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_WEBDAV, "current-user-principal")
    }


    class Factory: PropertyFactory {

        override fun getName() = NAME

        override fun create(parser: XmlPullParser): CurrentUserPrincipal {
            // <!ELEMENT current-user-principal (unauthenticated | href)>
            var href: String? = null
            XmlUtils.processTag(parser, XmlUtils.NS_WEBDAV, "href", {
                href = XmlUtils.readText(parser)
            })
            return CurrentUserPrincipal(href)
        }

    }

}
