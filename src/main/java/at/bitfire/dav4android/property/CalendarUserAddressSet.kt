/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.bitfire.dav4android.property

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser

class CalendarUserAddressSet: HrefListProperty() {

    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_CALDAV, "calendar-user-address-set")
    }


    class Factory : HrefListProperty.Factory() {

        override fun getName() = NAME

        override fun create(parser: XmlPullParser) =
                create(parser, CalendarUserAddressSet())

    }

}
