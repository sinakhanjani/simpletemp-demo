//
//  BundleExtension.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import UIKit

extension Bundle {
    /// Sending the app version to API.
    /// Checking available updates.
    /// Including the app version into a support email.
    var appVersion: String? {
        self.infoDictionary?["CFBundleShortVersionString"] as? String
    }
    /// Main Application App Version.
    static var mainAppVersion: String? {
        Bundle.main.appVersion
    }
    /// App version number.
    static var appVersion: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as! String
    }
    /// App build number.
    static var appBuild: String {
        Bundle.main.object(forInfoDictionaryKey: kCFBundleVersionKey as String) as! String
    }
    /// App unique device ID
    static var deviceID: String? {
        UIDevice.current.identifierForVendor?.uuidString
    }
    /// App device type
    static var deviceType: String {
        UIDevice().model
    }
}
