/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.bitfire.dav4android.exception

import okhttp3.Response
import java.net.HttpURLConnection

class ConflictException: HttpException {

    constructor(response: Response): super(response)
    constructor(message: String?): super(HttpURLConnection.HTTP_CONFLICT, message)

}
