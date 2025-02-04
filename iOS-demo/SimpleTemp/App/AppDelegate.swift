//
//  AppDelegate.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import UIKit
import RestfulAPI
import Firebase
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {
    static private(set) var instance: AppDelegate! = nil
    static var metaModel: MetaModel = .init()
    static var isNewNotifReceived: Bool = false
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        AppDelegate.instance = self    // << here !!
        
        RestfulAPIConfiguration().setup { () -> APIConfiguration in
            APIConfiguration(baseURL: "https://simpletemp.co.uk/api/v1",
                             timeoutIntervalForRequest: 10)
        }
        
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        application.applicationIconBadgeNumber = 0
        
        return true
    }
    
    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.portrait
    }
}

extension AppDelegate: MessagingDelegate {
    func requestForNotificationPrivacy(completion: @escaping (_ grand: Bool) -> Void) {
        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound],
            completionHandler: { grand, error in
                if grand {
                    UNUserNotificationCenter.current().delegate = self
                    completion(grand)
                }
            }
        )
        UIApplication.shared.registerForRemoteNotifications()
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let fcmToken = fcmToken {
            print("Firebase registration token: \(String(describing: fcmToken))")
        }
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    // for read a data from notification
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenParts = deviceToken.map { data -> String in
            return String.init(format: "%02.2hhx", data)
        }
        let token = tokenParts.joined()
        print("Push Notification Token: \(token)")
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        let state = application.applicationState
        print(userInfo,state)
        completionHandler(.newData)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo

        if let id = userInfo["_id"] as? String {
            AppDelegate.metaModel.id = id
        }
        
        if let type = userInfo["type"] as? String {
            AppDelegate.metaModel.type = NotifType.init(rawValue: type)
        }
        
        if let ref = userInfo["ref"] as? String {
            AppDelegate.metaModel.ref = ref
        }
        
        if let offerId = userInfo["offerId"] as? String {
            AppDelegate.metaModel.offerId = offerId
        }

        AppDelegate.isNewNotifReceived = true
        NotificationCenter.default.post(Notification(name: Notification.Name("newNotif")))
        completionHandler()
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) { }
    
    // forground
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.list, .banner, .badge, .sound])
    }
}
