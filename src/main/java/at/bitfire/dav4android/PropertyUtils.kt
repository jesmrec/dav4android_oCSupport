/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package at.bitfire.dav4android

import at.bitfire.dav4android.property.*
import at.bitfire.dav4android.property.owncloud.OCId
import at.bitfire.dav4android.property.owncloud.OCPermissions
import at.bitfire.dav4android.property.owncloud.OCPrivatelink
import at.bitfire.dav4android.property.owncloud.OCSize

object PropertyUtils {
    fun getAllPropSet(): Array<Property.Name>{
        return arrayOf(
                DisplayName.NAME,
                GetContentType.NAME,
                ResourceType.NAME,
                GetContentLength.NAME,
                GetLastModified.NAME,
                CreationDate.NAME,
                GetETag.NAME,
                QuotaUsedBytes.NAME,
                QuotaAvailableBytes.NAME,
                OCPermissions.NAME,
                OCId.NAME,
                OCSize.NAME,
                OCPrivatelink.NAME
        );
    }

    fun getQuotaPropset(): Array<Property.Name>{
        return arrayOf(
                QuotaUsedBytes.NAME,
                QuotaAvailableBytes.NAME
        )
    }
}