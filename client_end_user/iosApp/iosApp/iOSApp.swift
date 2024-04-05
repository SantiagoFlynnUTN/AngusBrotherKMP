import SwiftUI
import shared
import Firebase
import FirebaseMessaging

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init(){
        HelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ZStack {
                ContentView().edgesIgnoringSafeArea([.top])
            }
        }
    }
}


class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    var firebaseToken: String = ""
    
    override init() {
            super.init()
        }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        registerForPushNotifications()

        FirebaseApp.configure()

            application.registerForRemoteNotifications()


        
            Messaging.messaging().delegate = self
            UNUserNotificationCenter.current().delegate = self

        
            let token = Messaging.messaging().fcmToken
            print("FCM token: \(token ?? "No token available")")
            if let token = token {
                NotificationManager.shared.message = "FCM token: \(token)"
            } else {
                NotificationManager.shared.message = "No token available"
            }
        
            return true
        }


        func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
            self.firebaseToken = fcmToken!
    
        
        if let fcmToken = fcmToken {
            NotificationManager.shared.message = "FCM2 token: \(fcmToken)"
        } else {
            NotificationManager.shared.message = "N222 token available2"
        }
            print("Firebase token Aaaaaaa: \(fcmToken)")
        }

        // MARK: - Notification Functions

        func registerForPushNotifications() {
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) {(granted, error) in
                print("Permission granted: \(granted)")
            }
        }

        func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
            Messaging.messaging().apnsToken = deviceToken
            print("APNs token: \(deviceToken)")
        }
}

