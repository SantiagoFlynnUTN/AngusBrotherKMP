package com.angus.service_location.util

class MultiErrorException(val errorCodes: List<Int>) : Throwable(errorCodes.toString())

const val INVALID_LOCATION = 2003
