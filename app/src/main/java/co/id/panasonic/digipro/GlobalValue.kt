package co.id.panasonic.digipro

import android.app.Application

class GlobalValue : Application() {
    companion object {
        var token  = "not_login"
        var server = "http://158.118.35.60/api/"
        var depart = "department"
    }
}