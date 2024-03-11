import cocoapods.FirebaseMessaging.FIRMessaging
import data.gateway.service.IFireBaseMessageService
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
class FireBaseMessageService : IFireBaseMessageService {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getDeviceToken(): String {
        return FIRMessaging.messaging().FCMToken.toString()
    }
}

