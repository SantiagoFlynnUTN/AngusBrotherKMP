package com.angus.api_gateway.data.service

import aws.sdk.kotlin.runtime.auth.credentials.EnvironmentCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.ObjectCannedAcl
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.Url
import org.koin.core.annotation.Single
import com.angus.api_gateway.util.EnvConfig

@Single
class ImageService {
    suspend fun uploadImage(
        image: ByteArray,
        name: String,
        oldImageUrl: String? = null,
    ): String {
        val fileName = "${name.replace(" ", "_")}${Math.random()}.png"
        val bucketName = System.getenv("AWS_BUCKET_NAME") ?: EnvConfig.AWS_BUCKET_NAME
        val bucketRegion = System.getenv("AWS_BUCKET_REGION") ?: EnvConfig.AWS_BUCKET_REGION
        val imageUrl = "https://${bucketName}.s3.${bucketRegion}.amazonaws.com"

        val s3Client = S3Client.fromEnvironment {
            endpointUrl = Url.parse("https://s3.${bucketRegion}.amazonaws.com")
            region = bucketRegion
            credentialsProvider = EnvironmentCredentialsProvider()
        }

        if (!oldImageUrl.isNullOrEmpty()) {
            val deleteObjectRequest = DeleteObjectRequest {
                bucket = bucketName
                key = oldImageUrl.replace("$imageUrl/", "")
            }
            val deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest)
        }

        val putObjectRequest = PutObjectRequest {
            bucket = bucketName
            key = fileName
            acl = ObjectCannedAcl.fromValue("public-read")
            body = ByteStream.fromBytes(image)
        }

        val putObjectResponse = s3Client.putObject(putObjectRequest)
        println("Tag information is ${putObjectResponse.eTag}")

        return "$imageUrl/$fileName"
    }
}
