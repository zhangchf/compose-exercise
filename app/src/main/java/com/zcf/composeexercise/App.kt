package com.zcf.composeexercise

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xbill.DNS.Lookup
import org.xbill.DNS.TXTRecord
import org.xbill.DNS.Type
import org.xbill.DNS.config.AndroidResolverConfigProvider
import java.lang.Exception

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        DNSResolver.lookup(this)
    }
}

object DNSResolver {

    const val DISCO_SERVER = "staging.disco.foodsworth.com"

    const val KEY_API = "api"
    const val KEY_IMAGES = "images"
    const val KEY_CMS = "cms"

    var apiServer: String? = null
        private set
    var imageServer: String? = null
        private set
    var cmsServer: String? = null
        private set

    fun lookup(applicationContext: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AndroidResolverConfigProvider.setContext(applicationContext)
                val lookup = Lookup(DISCO_SERVER, Type.TXT)
                val records = lookup.run()
                val config = (records.firstOrNull() as? TXTRecord)?.strings?.firstOrNull()
                val configMap = config?.split(" ")?.associate {
                    val (key, value) = it.split("=")
                    key to value
                }
                apiServer = configMap?.get(KEY_API)
                imageServer = configMap?.get(KEY_IMAGES)
                cmsServer = configMap?.get(KEY_CMS)

// api=staging.foodsworth.com, images=image-preprod.foodsworth.com, cms=cms-cdn-staging.foodsworth.com
                println("zcf1 records: $apiServer, $imageServer, $cmsServer")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}