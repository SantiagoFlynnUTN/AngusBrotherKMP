package com.angus.common.data.gateway.local.model

import io.realm.kotlin.types.RealmObject

class ConfigurationCollection : RealmObject {
    var id: Int = 0
    var username: String = ""
    var languageCode: String = ""
    var accessToken: String = ""
    var refreshToken: String = ""
    var isDarkMode: Boolean = false
}