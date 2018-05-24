package at.bitfire.dav4android

import at.bitfire.dav4android.exception.HttpException
import at.bitfire.dav4android.property.GetETag
import okhttp3.*
import java.io.IOException

class DavOCResource(
        httpClient: OkHttpClient,
        location: HttpUrl) : DavResource(httpClient, location) {

    /**
     * Sends a PUT request to the resource.
     * @param body              new resource body to upload
     * @param ifMatchETag       value of "If-Match" header to set, or null to omit
     * @param ifNoneMatch       indicates whether "If-None-Match: *" ("don't overwrite anything existing") header shall be sent
     * @param contentType       TODO
     * @param ocTotalLength     TODO
     * @param ocXOcMtimeHeader  TODO
     * @return                  true if the request was redirected successfully, i.e. #{@link #location} and maybe resource name may have changed
     * @throws IOException on I/O error
     * @throws HttpException on HTTP error
     */
    @Throws(IOException::class, HttpException::class)
    fun put(body: RequestBody,
            ifMatchETag: String?,
            ifNoneMatch: Boolean,
            contentType: String?,
            ocTotalLength: String?,
            ocXOcMtimeHeader: String?
    ): Boolean {
        var redirected = false
        var response: Response? = null
        for (attempt in 1..MAX_REDIRECTS) {
            val builder = Request.Builder()
                    .put(body)
                    .url(location)

            if (ifMatchETag != null)
            // only overwrite specific version
                builder.header(IF_MATCH_HEADER, QuotedStringUtils.asQuotedString(ifMatchETag))
            if (ifNoneMatch)
            // don't overwrite anything existing
                builder.header(IF_NONE_MATCH_HEADER, "*")
            if(contentType != null) {
                builder.header(CONTENT_TYPE_HEADER, contentType)
            }
            if (ocTotalLength != null) {
                builder.header(OC_TOTAL_LENGTH_HEADER, ocTotalLength)
            }
            if (ocXOcMtimeHeader != null) {
                builder.header(OC_X_OC_MTIME_HEADER, ocXOcMtimeHeader)
            }

            val request = builder.build()
            response = httpClient.newCall(request).execute()
            this.request = request
            this.response = response
            if (response.isRedirect) {
                processRedirect(response)
                redirected = true
            } else
                break
        }

        checkStatus(response!!, true)

        val eTag = response.header("ETag")
        if (eTag.isNullOrEmpty())
            properties -= GetETag.NAME
        else
            properties[GetETag.NAME] = GetETag(eTag)

        return redirected
    }
}