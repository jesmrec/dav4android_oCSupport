/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package at.bitfire.dav4android

import at.bitfire.dav4android.exception.DavException
import at.bitfire.dav4android.exception.HttpException
import at.bitfire.dav4android.property.SyncToken
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.StringWriter
import java.util.logging.Logger

open class DavCollection @JvmOverloads constructor(
        httpClient: OkHttpClient,
        location: HttpUrl,
        log: Logger = Constants.log
): DavResource(httpClient, location, log) {

    /**
     * Sends a REPORT sync-collection request. If a sync-token is returned, it will be made
     * available in [properties].
     *
     * @param syncToken     sync-token to be sent with the request
     * @param infiniteDepth sync-level to be sent with the request: false = "1", true = "infinite"
     * @param limit         maximum number of results (may cause truncation)
     * @param properties    WebDAV properties to be requested
     * @throws java.io.IOException on I/O error
     * @throws HttpException on HTTP error
     * @throws DavException on DAV error

     */
    fun reportChanges(syncToken: String?, infiniteDepth: Boolean, limit: Int?, vararg properties: Property.Name): DavResponse {
        /* <!ELEMENT sync-collection (sync-token, sync-level, limit?, prop)>

           <!ELEMENT sync-token CDATA>       <!-- Text MUST be a URI -->
           <!ELEMENT sync-level CDATA>       <!-- Text MUST be either "1" or "infinite" -->

           <!ELEMENT limit (nresults) >
           <!ELEMENT nresults (#PCDATA)> <!-- only digits -->

           <!-- DAV:prop defined in RFC 4918, Section 14.18 -->
        */
        val serializer = XmlUtils.newSerializer()
        val writer = StringWriter()
        serializer.setOutput(writer)
        serializer.startDocument("UTF-8", null)
        serializer.setPrefix("", XmlUtils.NS_WEBDAV)
        serializer.startTag(XmlUtils.NS_WEBDAV, "sync-collection")
            serializer.startTag(SyncToken.NAME.namespace, SyncToken.NAME.name)
                syncToken?.let { serializer.text(it) }
            serializer.endTag(SyncToken.NAME.namespace, SyncToken.NAME.name)
            serializer.startTag(XmlUtils.NS_WEBDAV, "sync-level")
                serializer.text(if (infiniteDepth) "infinite" else "1")
            serializer.endTag(XmlUtils.NS_WEBDAV, "sync-level")
            limit?.let { nresults ->
                serializer.startTag(XmlUtils.NS_WEBDAV, "limit")
                    serializer.startTag(XmlUtils.NS_WEBDAV, "nresults")
                    serializer.text(nresults.toString())
                    serializer.endTag(XmlUtils.NS_WEBDAV, "nresults")
                serializer.endTag(XmlUtils.NS_WEBDAV, "limit")
            }
            serializer.startTag(XmlUtils.NS_WEBDAV, "prop")
            properties.forEach {
                serializer.startTag(it.namespace, it.name)
                serializer.endTag(it.namespace, it.name)
            }
            serializer.endTag(XmlUtils.NS_WEBDAV, "prop")
        serializer.endTag(XmlUtils.NS_WEBDAV, "sync-collection")
        serializer.endDocument()

        val response = httpClient.newCall(Request.Builder()
                .url(location)
                .method("REPORT", RequestBody.create(MIME_XML, writer.toString()))
                .header("Depth", "0")
                .build()).execute()

        checkStatus(response)
        assertMultiStatus(response)

        return processMultiStatus(response.body()?.charStream()!!)
    }

}