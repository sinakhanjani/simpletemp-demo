//
//  NotificationNameExtension.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/15/22.
//

import Foundation

extension Notification.Name {
    static let reachabilityStatusChangedNotification =  NSNotification.Name(rawValue: "ReachabilityStatusChangedNotification")
    static let profileChangedNotification =  NSNotification.Name(rawValue: "gatewayChangedNotification")
    static let fileReviewTimeEnded =  NSNotification.Name(rawValue: "fileReviewTimeEnded")
}
