package at.bitfire.dav4android

import at.bitfire.dav4android.exception.DavException
import at.bitfire.dav4android.exception.HttpException
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.OkHttpClient
import okhttp3.HttpUrl
import java.io.IOException
import java.util.logging.Logger

class DavOCResource(
        httpClient: OkHttpClient,
        location: HttpUrl,
        log: Logger) : DavResource(httpClient, location, log) {

    /**
     * Sends a PUT request to the resource.
     * @param body              new resource body to upload
     * @param ifMatchETag       value of "If-Match" header to set, or null to omit
     * @param ifNoneMatch       indicates whether "If-None-Match: *" ("don't overwrite anything existing") header shall be sent
     * @param contentType
     * @param ocTotalLength     total length of resource body
     * @param ocXOcMtimeHeader  modification time
     * @return                  true if the request was redirected successfully, i.e. #{@link #location} and maybe resource name may have changed
     * @throws IOException on I/O error
     * @throws HttpException on HTTP error
     */
    @Throws(IOException::class, HttpException::class)
    fun put(body: RequestBody,
            ifMatchETag: String?,
            contentType: String?,
            ocTotalLength: String?,
            ocXOcMtimeHeader: String?,
            callback: (respnse: Response) -> Unit
    ) {
        val requestBuilder = Request.Builder()
                .put(body)
        if (ifMatchETag != null)
        // only overwrite specific version
            requestBuilder.header(IF_MATCH_HEADER, QuotedStringUtils.asQuotedString(ifMatchETag))
        if(contentType != null) {
            requestBuilder.header(CONTENT_TYPE_HEADER, contentType)
        }
        if (ocTotalLength != null) {
            requestBuilder.header(OC_TOTAL_LENGTH_HEADER, ocTotalLength)
        }
        if (ocXOcMtimeHeader != null) {
            requestBuilder.header(OC_X_OC_MTIME_HEADER, ocXOcMtimeHeader)
        }

        followRedirects {
            requestBuilder
                    .url(location)
            val call = httpClient.newCall(requestBuilder.build())

            this.call = call
            call.execute()
        }.use {response ->
            this.response = response
            checkStatus(response)
            callback(response)
        }
    }

    /**
     * Sends a MOVE request to the resource
     * @param ocTotalLength     total length of resource body
     * @param ocXOcMtimeHeader  modification time
     */
    @Throws(IOException::class, HttpException::class, DavException::class)
    fun move(destination:String,
             forceOverride:Boolean,
             ocTotalLength: String?,
             ocXOcMtimeHeader: String?,
             callback: (respnse: Response) -> Unit) {
        val requestBuilder = Request.Builder()
                .method("MOVE", null)
                .header("Content-Length", "0")
                .header("Destination", destination)


        if(forceOverride)
            requestBuilder.header("Overwrite", "F")
        if(ocTotalLength != null)
            requestBuilder.header(OC_TOTAL_LENGTH_HEADER, ocTotalLength)
        if(ocXOcMtimeHeader != null)
            requestBuilder.header(OC_X_OC_MTIME_HEADER, ocXOcMtimeHeader)

        followRedirects {
            requestBuilder.url(location)
            val call = httpClient.newCall(requestBuilder.build())
            this.call = call
            call.execute()
        }.use { response->
            checkStatus(response)
            this.response = response
            callback(response)
        }
    }
}