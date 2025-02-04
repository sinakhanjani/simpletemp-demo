//
//  NotificationModel.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/20/22.
//

import SwiftUI

struct NotificationModel: Codable, Identifiable {
    internal init() {
        self.notification = NotificationDetailsModel()
        self.data = DataClassModel()
        self.id = ""
        self.createdAt = ""
        self.isRead = true
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let notification = try? keyedCodingContainer.decode(NotificationDetailsModel.self, forKey: CodingKeys.notification) {
            self.notification = notification
        } else {
            self.notification = NotificationDetailsModel()
        }
        
        if let data = try? keyedCodingContainer.decode(DataClassModel.self, forKey: CodingKeys.data) {
            self.data = data
        } else {
            self.data = DataClassModel()
        }
        
        if let id = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.id) {
            self.id = id
        } else {
            self.id = ""
        }
        
        if let createdAt = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.createdAt) {
            self.createdAt = createdAt
        } else {
            self.createdAt = ""
        }
        
        if let isRead = try? keyedCodingContainer.decode(Bool.self, forKey: CodingKeys.isRead) {
            self.isRead = isRead
        } else {
            self.isRead = true
        }
    }
    
    let notification: NotificationDetailsModel
    var data: DataClassModel
    let id, createdAt: String
    var isRead: Bool

    enum CodingKeys: String, CodingKey {
        case notification, data
        case id = "_id"
        case createdAt
        case isRead
    }
}

struct DataClassModel: Codable {
    internal init() {
        self.meta = MetaModel()
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let meta = try? keyedCodingContainer.decode(MetaModel.self, forKey: CodingKeys.meta) {
            self.meta = meta
        } else {
            self.meta = MetaModel()
        }
    }
    
    var meta: MetaModel
    
    enum CodingKeys: String, CodingKey {
        case meta
    }
}

struct MetaModel: Codable, Equatable {
    internal init() {
        self.type = nil
        self.id = ""
        self.ref = ""
        self.offerId = nil
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let type = try? keyedCodingContainer.decode(NotifType.self, forKey: CodingKeys.type) {
            self.type = type
        } else {
            self.type = nil
        }
        
        if let id = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.id) {
            self.id = id
        } else {
            self.id = ""
        }
        
        if let ref = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.ref) {
            self.ref = ref
        } else {
            self.ref = ""
        }
        
        if let offerId = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.offerId) {
            self.offerId = offerId
        } else {
            self.offerId = nil
        }
    }
    
    var type: NotifType?
    var id, ref: String
    var offerId: String?

    enum CodingKeys: String, CodingKey {
        case type
        case id = "_id"
        case ref
        case offerId
    }
}

enum NotifType: String, Codable, CaseIterable {
    case profile = "Profile"
    case ticket = "Ticket"
    case offer = "Offer"
    case offerAccpeted = "OfferAccepted"
    case invoice = "Invoice"
    case resubmission = "Resubmission"
    case money = "Money"
    case rate = "Rate"
    case dispute = "Dispute"
    case none = "none"
}

struct NotificationDetailsModel: Codable, Identifiable {
    internal init() {
        self.title = ""
        self.body = ""
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let title = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.title) {
            self.title = title
        } else {
            self.title = ""
        }
        
        if let body = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.body) {
            self.body = body
        } else {
            self.body = ""
        }
    }
    
    let title, body: String
    var id = UUID().uuidString
    
    enum CodingKeys: String, CodingKey {
        case title, body
    }
}
