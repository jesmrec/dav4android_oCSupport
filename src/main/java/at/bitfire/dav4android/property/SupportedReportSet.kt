/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package at.bitfire.dav4android.property

import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser

class SupportedReportSet: Property {

    companion object {
        val NAME = Property.Name(XmlUtils.NS_WEBDAV, "supported-report-set")

        val SYNC_COLLECTION = "DAV:sync-collection"    // collection synchronization (RFC 6578)
    }

    val reports = mutableSetOf<String>()

    override fun toString() = "[${reports.joinToString(", ")}]"


    class Factory: PropertyFactory {

        override fun getName() = NAME

        override fun create(parser: XmlPullParser): SupportedReportSet? {
            /* <!ELEMENT supported-report-set (supported-report*)>
               <!ELEMENT supported-report report>
               <!ELEMENT report ANY>
            */

            val supported = SupportedReportSet()
            XmlUtils.processTag(parser, XmlUtils.NS_WEBDAV, "supported-report", {
                XmlUtils.processTag(parser, XmlUtils.NS_WEBDAV, "report") {
                    parser.nextTag()
                    if (parser.eventType == XmlPullParser.TEXT)
                        supported.reports += parser.text
                    else if (parser.eventType == XmlPullParser.START_TAG)
                        supported.reports += "${parser.namespace}${parser.name}"
                }
            })
            return supported
        }

    }

}
