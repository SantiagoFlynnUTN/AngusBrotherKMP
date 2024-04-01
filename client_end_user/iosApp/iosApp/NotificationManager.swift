//
//  NotificationManager.swift
//  iosApp
//
//  Created by Santiago on 3/30/24.
//  Copyright © 2024 orgName. All rights reserved.
//


import SwiftUI
import Combine

class NotificationManager: ObservableObject {
    static let shared = NotificationManager()
    @Published var message: String?
}
