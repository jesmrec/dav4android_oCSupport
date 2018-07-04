package at.bitfire.dav4android.exception

import okhttp3.Response

open class RedirectException: HttpException {

    val redirectLocation : String

    constructor(redirectResponse: Response): super(redirectResponse) {
        redirectLocation = redirectResponse.header("location") ?: ""
    }

    constructor(message: String?): super(message) {
        redirectLocation = ""
    }
}