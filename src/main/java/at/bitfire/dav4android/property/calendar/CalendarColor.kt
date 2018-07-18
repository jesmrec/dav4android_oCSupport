/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package at.bitfire.dav4android.property.calendar

import at.bitfire.dav4android.Constants
import at.bitfire.dav4android.Property
import at.bitfire.dav4android.PropertyFactory
import at.bitfire.dav4android.XmlUtils
import org.xmlpull.v1.XmlPullParser
import java.util.logging.Level
import java.util.regex.Pattern

data class CalendarColor(
        val color: Int
): Property {

    companion object {
        @JvmField
        val NAME = Property.Name(XmlUtils.NS_APPLE_ICAL, "calendar-color")

        private val PATTERN = Pattern.compile("#?(\\p{XDigit}{6})(\\p{XDigit}{2})?")!!

        /**
         * Converts a WebDAV color from one of these formats:
         *   #RRGGBB     (alpha = 0xFF)
         *   RRGGBB      (alpha = 0xFF)
         *   #RRGGBBAA
         *   RRGGBBAA
         *  to an [Int] with alpha.
         */
        @Throws(IllegalArgumentException::class)
        fun parseARGBColor(davColor: String): Int {
            val m = PATTERN.matcher(davColor)
            if (m.find()) {
                val color_rgb = Integer.parseInt(m.group(1), 16)
                val color_alpha = m.group(2)?.let { Integer.parseInt(m.group(2), 16) and 0xFF } ?: 0xFF
                return (color_alpha shl 24) or color_rgb
            } else
                throw IllegalArgumentException("Couldn't parse color value: $davColor")
        }
    }


    class Factory: PropertyFactory {

        override fun getName() = NAME

        override fun create(parser: XmlPullParser): CalendarColor? {
            XmlUtils.readText(parser)?.let {
                try {
                    return CalendarColor(parseARGBColor(it))
                } catch (e: IllegalArgumentException) {
                    Constants.log.log(Level.WARNING, "Couldn't parse color, ignoring", e)
                }
            }
            return null
        }

    }

}
