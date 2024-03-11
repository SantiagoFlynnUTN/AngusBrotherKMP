package com.angus.designSystem


//@OptIn(ExperimentalResourceApi::class)
//@Composable
//
//actual fun fontResources(font: String): Font {
//
//     val cache: MutableMap<String, Font> = mutableMapOf()
//    return cache.getOrPut(font) {
//        val byteArray = runBlocking {
//            resource("font/$font").readBytes()
//        }
//        Font(font, byteArray)
//    }
//}