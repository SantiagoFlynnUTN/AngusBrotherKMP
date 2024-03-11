package com.angus.service_notification.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.gson.JsonParser
import io.ktor.server.application.*
import java.io.ByteArrayInputStream

fun Application.configureFirebaseApp() {
    val credentialsString = """{
        "type": "service_account",
        "project_id": "ktorproject-200f5",
        "private_key_id": "8ac5e34a1fb2404f95c8120043e67b782a26b107",
        "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC4kn+7F48T06KT\nJ6FAyO9kYDpmUq9waAPbIFCXqvomLRHWxe/k44a29isgCjMTzculiRMoVgZpRaK1\nesQwD4/L9vCNd10phdUyMOjcy7BgCNjKQtt82bDUj7SJBXdHmKCkBiWhsHlF2tk3\nFJ0T1ARnbVkEGXFCTyLKA35BGlsCg09UuQyXsySR2eMnIUTOR506TcqVgH1R8mP5\nxQmZtOJh/M6S5501Js2ZoVGBeYjPWaka9GOo0UD5Q38KvLVOV5F4j5JRvqMTtBZo\nhU00YP5cpFz+lGqHUxhxinKij2Wo9I7hS7lby2MYnc8QlcK3xk/8C43uwPvz1q8h\njiaFL95LAgMBAAECggEAGK/SO8uhNyKrGl7w7NcIsKFE+Q39fzMPs6MKhIQO5JpJ\n9N28Ah5YlLzaScpzcyZRzbicLQdMi49lKDwyZMdAEXoxnzT5lfsw59SuNhIHFjaO\nCzI5ZI/tXJDtXG0Ju4lzkFzDcxvv1zhireuo6CUHLcTsug/5G8hf5oCPG4ATRNjh\nQmniVLVxfzQhO6xZoiN2IQqJ187AdwcnLL1h6gl5EIDaPbuBl6Z1aX7MC94JgyC9\nkUgzyz2S6IWkTHxo5OYA5oB1VJ4mXBdyjXGWEqQXnySMhBZqp/kvZyNpffW4EmU2\nxMqlhtbEuHTfyMypOKCkxBbyDe3Mjbgg7WSIiZE/QQKBgQD6RMH+42cJaileRo3+\nJjyKdWNPAH4V+L7GD/vLdojYBef7OMITAuun43L2etCJqkFwDxNzqZoOeMDb//es\nL4H+74aIkm0ht8b7KvXSNZd4Pa1G1suqcTh067G/XltCcYGdoBPHsEulhVdwkA5g\nD9Dy01RXQu7WLZQtg6LOJaxsKwKBgQC8zJXUC9wq6CGWZWu+PumqNfzEs27myQ7B\nIK9HrNUYO5Gsj4zba4oJm7ImpuyYRVbRVt0tddDFgbYK64Tt+upQUCm5I8Tf9004\nYV8Nr0HwBUeG1B+9VuUQ6i4nm5GVGjnfD1qNXcQ6ojXcgtUvvlzORHp7lC3RNhWv\n66iuq6WmYQKBgQCuLC/+3fAVg0arD9s+Jwo3Kh8lGiilzemnGfSY0zPFc8bQ9d1L\nMdsjio21sB91z4GfJT7aXgyTScKe2XNa5+1GqRgt8An3T1kx7+IIm7eAvRvckxxG\n0GkEJgwcLOTCRxB3zLI3ZSAgX75M3lTC0f8PH7gwyqpBcJMRYe1qHpny5QKBgGRB\n+CZ8jdQzJPLqtMZ4PjlnJe2Go5PmnPEt3Cahvx95MqiCKniyqMDlbztbVTSfTbUo\n/ZcQg2b+tJ8j5plBr4AqsZ1hQ0Dz4IvaGVH5CXjeNNeCecBgDpBm60+ZKzwUizrK\nunlrm7LPRO7ugysxWbMnZiuNyq7Ym7ep2w+2kubBAoGAXqdEyXrfJvZLUiFKsGMn\ngufO5RRyZXHWo4TOSn2/42nMhbHW0hrui3Su2QQSfIMdThBH72NjngLYHdL2v6ia\nuRZ5TauIjaFUZW1GCbz442QwbqwTX2sqnjj5onnQotHZZNjdcvpcmavRSMKoIgJs\nKsCrbfUv3vyrA/y+n5HzLRg=\n-----END PRIVATE KEY-----\n",
        "client_email": "firebase-adminsdk-scabh@ktorproject-200f5.iam.gserviceaccount.com",
        "client_id": "111560701177549142995",
        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
        "token_uri": "https://oauth2.googleapis.com/token",
        "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
        "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-scabh%40ktorproject-200f5.iam.gserviceaccount.com",
        "universe_domain": "googleapis.com"
    }""".trimIndent()

    val credentialsJson = JsonParser.parseString(credentialsString).asJsonObject
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(credentialsJson.toString().toByteArray())))
        .build()
    FirebaseApp.initializeApp(options)
}