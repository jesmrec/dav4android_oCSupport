/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.bitfire.dav4android

import at.bitfire.dav4android.property.*
import at.bitfire.dav4android.property.address.AddressData
import at.bitfire.dav4android.property.address.AddressbookDescription
import at.bitfire.dav4android.property.address.AddressbookHomeSet
import at.bitfire.dav4android.property.address.SupportedAddressData
import at.bitfire.dav4android.property.calendar.*
import at.bitfire.dav4android.property.owncloud.OCId
import at.bitfire.dav4android.property.owncloud.OCPermissions
import at.bitfire.dav4android.property.owncloud.OCPrivatelink
import at.bitfire.dav4android.property.owncloud.OCSize
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.util.logging.Level

object PropertyRegistry {

    private val factories = mutableMapOf<Property.Name, PropertyFactory>()

    init {
        Constants.log.info("Registering DAV property factories")
        for (factory in getPropertyFactories()) {
            Constants.log.fine("Registering ${factory::class.java.name} for ${factory.getName()}")
            register(factory)
        }
    }

    private fun register(factory: PropertyFactory) {
        factories[factory.getName()] = factory
    }

    private fun getPropertyFactories() =
            arrayOf(
                    AddressbookDescription.Factory(),
                    AddressbookHomeSet.Factory(),
                    AddressData.Factory(),
                    CalendarColor.Factory(),
                    CalendarData.Factory(),
                    CalendarDescription.Factory(),
                    CalendarHomeSet.Factory(),
                    CalendarProxyReadFor.Factory(),
                    CalendarProxyWriteFor.Factory(),
                    CalendarTimezone.Factory(),
                    CalendarUserAddressSet.Factory(),
                    CreationDate.Factory(),
                    CurrentUserPrincipal.Factory(),
                    CurrentUserPrivilegeSet.Factory(),
                    DisplayName.Factory(),
                    GetContentLength.Factory(),
                    GetContentType.Factory(),
                    GetCTag.Factory(),
                    GetLastModified.Factory(),
                    GroupMembership.Factory(),
                    QuotaAvailableBytes.Factory(),
                    QuotaUsedBytes.Factory(),
                    Source.Factory(),
                    SupportedAddressData.Factory(),
                    SupportedCalendarComponentSet.Factory(),
                    SupportedReportSet.Factory(),
                    SyncToken.Factory(),
                    OCPermissions.Factory(),
                    OCId.Factory(),
                    OCSize.Factory(),
                    OCPrivatelink.Factory())

    fun create(name: Property.Name, parser: XmlPullParser) =
            try {
                factories[name]?.create(parser)
            } catch (e: XmlPullParserException) {
                Constants.log.log(Level.WARNING, "Couldn't parse $name", e)
                null
            }
}