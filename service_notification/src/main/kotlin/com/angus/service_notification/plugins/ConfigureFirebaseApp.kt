package com.angus.service_notification.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.gson.JsonParser
import io.ktor.server.application.*
import java.io.ByteArrayInputStream

fun Application.configureFirebaseApp() {
    val credentialsString = // System.getenv("FIREBASE_CREDENTIALS").trimIndent()
        """{
  "type": "service_account",
  "project_id": "angusbrother-9ccdd",
  "private_key_id": "e2b03f746450d19dec12084efba5d375a3dc89e5",
  "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC7/qQld6Asvfl8\nmKRS8qpZTFbum6zBDrvsvab22Sx3y2/hzMoWWNcCZiMgmgUENZSUuuBOdW7sN48E\n1wtl9qgimlePUiqnPn4I/RgC2lpVCmJmuaDTTVGdIPsiAhdNFVDS0psHiRGcNI62\n2hiMYukL83K+tiwL3W5ZpXIb1R+3HmMDXTOVOFnEe0VY5CCtCJKptUOJEKAAVJQS\nm5WKenVj4LfiViBMvd7s7LlllVPn1wIWb5RzqkdIaAeqPerwg0wva2m7M6n3apms\nDh2EhwXCqftN6s4NDDVx+CrPuf7pwalRvX/GMh91PpdnvP7KolX5qmQYqRDMZ84a\nONDcZkf7AgMBAAECggEABUsmBcaBuBDOfJNIawhnrXOzH58BkZQYsDxCNyUXlbwu\n6u9iPnvYeoE0FwOIIhLum4KhmKBsAh1UYgSsXP3sb085YknrSoyhNR95+6ojV83J\njW6dAvuAw1BofI/w2CM0BJV8WtRag/qqN6dTDqJCJOSBS0A88gzOUEUg2pjHN60k\nqCVxon/8w56MQkTqLov6hPVphCFln1VVFEEUPHOfu6DB848FBgSABLg50sjMDz/a\nELMPsDbU3nVg+cAoxP3G8DH4Kp5Ppi7iOq5cA4SrA66d/uUJFnnXyXL2pQNhZacw\nHIwnKci7Nsp+TtBntHSb7twCWA4gOc+uuWG/wgdVUQKBgQD1n2r+s4IEn5pqmQXZ\nqX1oz1hTfC4RMhvbxTeVkcqjnH/xDQhQddA3jTClJ/KGVPHCqnYWb/VYEI1WCjpw\nIoDV45XyO84/ZeDUDJXG7uUyA5hNfURYPR9MiUvGpkcKjJswttUig8OCV+3XUWEq\nrCfSWOXk1buId5bnfzzH1XQgywKBgQDD7++GLJNUa5OgCmSbklCS/dEJs1+s0KnI\n2D85Umgiltdf9Mpn1G+MblYQij+4wENML1uSA6J4/CcxpkSvWkLPHUYdVIaahYiy\njA1DLCFdMlCuah79pQnX86TlFgwcWa2XG2/78g5a1v327O1+d/FiqYnPzBtrQRNx\n6AbAZ7F/kQKBgDMJxz/ZqSCw0XNpDLAn6elHfIEMgr4L/YHzPxOTUdohgOxj2uBt\nOrDwY7VN0bVcHUhoB1WuOfrspZHFWlNGuCK5wcSt7LN7xfYKD21g4rs7yAACEZuM\nR96tgNNsZcKpaO38b9lWK9//yWo+fMRTNYsd8ddVGjzHLNI5vDWCAzl1AoGAA61n\nI2+GvFjghmprhPf0By44VCRHsWihJyHX3e2MAsot9KEVg2lbRplziA/wLjl9idqB\nr3XeAMNKwQXL2DardlOoUziY5iGHdUJnpVEYtzUQipV3S29jhegOujX1uDp+pY1g\nRqw+VLogPhgoRO1fXGiuK3Y7NYIdKVAfgFDx5eECgYAGjZ8MNh8qkRv9ncrKSWro\n6ylpLfSElcJVacFR3cHYPgOJhSL4PU8PoWeKKQuTANJggnZluRku8mMd8zI+h8he\n/b+ErIO4fSDzQPZ/r/GKpFOJRahhsiR6mDLVfJLU+Rwere1Qk/t4VSalQI51GcUJ\nmlK4DJjbkyHCXvgQ0amNrA==\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-zkh96@angusbrother-9ccdd.iam.gserviceaccount.com",
  "client_id": "112707205384089849800",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-zkh96%40angusbrother-9ccdd.iam.gserviceaccount.com",
  "universe_domain": "googleapis.com"
}""".trimIndent()

    val credentialsJson = JsonParser.parseString(credentialsString).asJsonObject
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(credentialsJson.toString().toByteArray())))
        .build()
    FirebaseApp.initializeApp(options)
}
