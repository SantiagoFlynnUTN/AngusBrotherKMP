import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @ObservedObject var notificationManager = NotificationManager.shared

    var body: some View {

        ComposeView()
                .ignoresSafeArea(.all, edges: .bottom) // Compose has own keyboard handler
        
        if let message = notificationManager.message {
                       VStack {
                           Spacer()
                           Text(message)
                               .foregroundColor(.white)
                               .padding()
                               .background(Color.black.opacity(0.7))
                               .cornerRadius(8)
                               .padding()
                               .onAppear {
                                   DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                                       notificationManager.message = nil
                                   }
                               }
                       }
                   }
    }
}



