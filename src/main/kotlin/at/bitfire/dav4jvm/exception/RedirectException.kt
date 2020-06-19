package at.bitfire.dav4jvm.exception

import okhttp3.Response

class RedirectException: HttpException {

    val redirectLocation : String

    constructor(redirectResponse: Response): super(redirectResponse) {
        redirectLocation = redirectResponse.header("location") ?: ""
    }

    constructor(code: Int, message: String?): super(code, message) {
        redirectLocation = ""
    }
}