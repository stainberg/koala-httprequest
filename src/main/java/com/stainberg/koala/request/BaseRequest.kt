package com.stainberg.koala.request


import com.stainberg.koala.koalahttp.KoalaRequestType
import okhttp3.RequestBody
import java.io.Serializable
import java.util.*

abstract class BaseRequest : Serializable {

    private val params = HashMap<String, String>()
    private val headers = HashMap<String, String>()
    private val extras = ArrayList<Request>()
    var tag: String? = null
    var maxStale = 0
    var url: String? = null
    var method: KoalaRequestType? = null

    constructor()
    constructor(url: String, method: KoalaRequestType) {
        this.url = url
        this.method = method
    }

    fun addExtra(body: Request): BaseRequest {
        extras.add(body)
        return this
    }

    fun addAllParams(pms: Map<String, String>): BaseRequest {
        params.putAll(pms)
        return this
    }

    fun addParams(key: String?, value: String?): BaseRequest {
        if (key != null && value != null) {
            params.put(key, value)
        }
        return this
    }

    fun removeParams(key: String): BaseRequest {
        params.remove(key)
        return this
    }

    fun addAllHeaders(hds: Map<String, String>): BaseRequest {
        headers.putAll(hds)
        return this
    }

    fun addHeaders(key: String, value: String): BaseRequest {
        headers.put(key, value)
        return this
    }

    fun removeHeaders(key: String): BaseRequest {
        headers.remove(key)
        return this
    }

    fun clearParams() {
        headers.clear()
    }

    fun clearHeaders() {
        headers.clear()
    }

    fun getParams(): Map<String, String> {
        return params
    }

    fun getExtras(): List<Request> {
        return extras
    }

    fun getHeaders(): Map<String, String> {
        return headers
    }

    class Request(var key: String, var filename: String, var body: RequestBody)

}
