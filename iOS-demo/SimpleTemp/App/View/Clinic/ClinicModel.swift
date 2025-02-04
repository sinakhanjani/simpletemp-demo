//
//  ClinicModel.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/8/22.
//

import SwiftUI

// MARK: - ClinicModel
struct ClinicModel: Codable, Equatable {
    internal init() {
        self.id = ""
        self.profile = ClinicProfileModel()
        self.fullname = ""
        self.email = ""
        self.userType = ""
        self.authenticationSteps = [""]
        self.token = ""
        self.photoURL = ""
        self.activeNotification = false
        self.distance = ""
        self.hasShift = nil
        self.badge = 0
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let profile = try? keyedCodingContainer.decode(ClinicProfileModel.self, forKey: CodingKeys.profile) {
            self.profile = profile
        } else {
            self.profile = ClinicProfileModel()
        }
        
        if let id = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.id) {
            self.id = id
        } else {
            self.id = ""
        }
        
        if let fullname = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.fullname) {
            self.fullname = fullname
        } else {
            self.fullname = ""
        }
        
        if let email = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.email) {
            self.email = email
        } else {
            self.email = ""
        }
        
        if let userType = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.userType) {
            self.userType = userType
        } else {
            self.userType = ""
        }
        
        if let token = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.token) {
            self.token = token
        } else {
            self.token = ""
        }
        
        if let authenticationSteps = try? keyedCodingContainer.decode([String].self, forKey: CodingKeys.authenticationSteps) {
            self.authenticationSteps = authenticationSteps
        } else {
            self.authenticationSteps = [""]
        }
        
        if let photoURL = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.photoURL) {
            self.photoURL = photoURL
        } else {
            self.photoURL = ""
        }
        
        if let activeNotification = try? keyedCodingContainer.decode(Bool.self, forKey: CodingKeys.activeNotification) {
            self.activeNotification = activeNotification
        } else {
            self.activeNotification = false
        }
        
        if let distance = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.distance) {
            self.distance = distance
        } else {
            self.distance = ""
        }
        
        if let hasShift = try? keyedCodingContainer.decode(Bool.self, forKey: CodingKeys.hasShift) {
            self.hasShift = hasShift
        } else {
            self.hasShift = nil
        }
        
        if let badge = try? keyedCodingContainer.decode(Int.self, forKey: CodingKeys.badge) {
            self.badge = badge
        } else {
            self.badge = 0
        }
    }
    
    var profile: ClinicProfileModel
    var id: String
    var fullname, email, userType, token: String
    var authenticationSteps: [String]
    var photoURL: String
    var activeNotification: Bool
    var distance: String
    var hasShift: Bool?
    var badge: Int
    
    enum CodingKeys: String, CodingKey {
        case profile
        case id = "_id"
        case fullname, email, userType, authenticationSteps, token, photoURL, activeNotification
        case distance
        case hasShift
        case badge
    }
}

// MARK: - Profile
struct ClinicProfileModel: Codable, Equatable {
    internal init() {
        self.bankInformation = ClinicBankInformation()
        self.percentage = 0
        self.profileDescription = ""
        self.docURL = ""
        self.accountInformation = ClinicAccountInformation()
        self.detailInformation = ClinicDetailInformation()
        self.isPendingIdentity = false
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let bankInformation = try? keyedCodingContainer.decode(ClinicBankInformation.self, forKey: CodingKeys.bankInformation) {
            self.bankInformation = bankInformation
        } else {
            self.bankInformation = ClinicBankInformation()
        }
        
        if let percentage = try? keyedCodingContainer.decode(Int.self, forKey: CodingKeys.percentage) {
            self.percentage = percentage
        } else {
            self.percentage = 0
        }
        
        if let profileDescription = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.profileDescription) {
            self.profileDescription = profileDescription
        } else {
            self.profileDescription = ""
        }
        
        if let docURL = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.docURL) {
            self.docURL = docURL
        } else {
            self.docURL = ""
        }
        
        if let accountInformation = try? keyedCodingContainer.decode(ClinicAccountInformation.self, forKey: CodingKeys.accountInformation) {
            self.accountInformation = accountInformation
        } else {
            self.accountInformation = ClinicAccountInformation()
        }
        
        if let detailInformation = try? keyedCodingContainer.decode(ClinicDetailInformation.self, forKey: CodingKeys.detailInformation) {
            self.detailInformation = detailInformation
        } else {
            self.detailInformation = ClinicDetailInformation()
        }
        
        if let isPendingIdentity = try? keyedCodingContainer.decode(Bool.self, forKey: CodingKeys.isPendingIdentity) {
            self.isPendingIdentity = isPendingIdentity
        } else {
            self.isPendingIdentity = false
        }
    }
    
    var bankInformation: ClinicBankInformation
    var percentage: Int
    var profileDescription: String
    var docURL: String
    var accountInformation: ClinicAccountInformation
    var detailInformation: ClinicDetailInformation
    var isPendingIdentity: Bool

    enum CodingKeys: String, CodingKey {
        case bankInformation, percentage
        case profileDescription = "description"
        case docURL, accountInformation, detailInformation
        case isPendingIdentity
    }
}

// MARK: - AccountInformation
struct ClinicAccountInformation: Codable, Equatable {
    internal init() {
        self.coordinate = ClinicCoordinate()
        self.city = ""
        self.postalcode = ""
        self.addressLine1 = ""
        self.addressLine2 = ""
        self.phone = ""
        self.primaryDentistName = ""
        self.primaryDentistCertificationNumber = ""
    }
    
    internal init(coordinate: ClinicCoordinate, city: String, postalcode: String, addressLine1: String, addressLine2: String, phone: String, primaryDentistName: String, primaryDentistCertificationNumber: String) {
        self.coordinate = coordinate
        self.city = city
        self.postalcode = postalcode
        self.addressLine1 = addressLine1
        self.addressLine2 = addressLine2
        self.phone = phone
        self.primaryDentistName = primaryDentistName
        self.primaryDentistCertificationNumber = primaryDentistCertificationNumber
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let coordinate = try? keyedCodingContainer.decode(ClinicCoordinate.self, forKey: CodingKeys.coordinate) {
            self.coordinate = coordinate
        } else {
            self.coordinate = ClinicCoordinate()
        }
        
        if let city = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.city) {
            self.city = city
        } else {
            self.city = ""
        }
        
        if let postalcode = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.postalcode) {
            self.postalcode = postalcode
        } else {
            self.postalcode = ""
        }
        
        if let addressLine1 = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.addressLine1) {
            self.addressLine1 = addressLine1
        } else {
            self.addressLine1 = ""
        }
        
        if let addressLine2 = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.addressLine2) {
            self.addressLine2 = addressLine2
        } else {
            self.addressLine2 = ""
        }
        
        if let phone = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.phone) {
            self.phone = phone
        } else {
            self.phone = ""
        }
        
        if let primaryDentistName = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.primaryDentistName) {
            self.primaryDentistName = primaryDentistName
        } else {
            self.primaryDentistName = ""
        }
        
        if let primaryDentistCertificationNumber = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.primaryDentistCertificationNumber) {
            self.primaryDentistCertificationNumber = primaryDentistCertificationNumber
        } else {
            self.primaryDentistCertificationNumber = ""
        }
    }
    
    var coordinate: ClinicCoordinate
    var city, postalcode, addressLine1, addressLine2: String
    var phone, primaryDentistName, primaryDentistCertificationNumber: String
    
    enum CodingKeys: String, CodingKey {
        case coordinate
        case city, postalcode, addressLine1, addressLine2, phone, primaryDentistName, primaryDentistCertificationNumber
    }
}

// MARK: - Coordinate
struct ClinicCoordinate: Codable, Equatable {
    internal init() {
        self.longitude = ""
        self.latitude = ""
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let longitude = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.longitude) {
            self.longitude = longitude
        } else {
            self.longitude = ""
        }
        
        if let latitude = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.latitude) {
            self.latitude = latitude
        } else {
            self.latitude = ""
        }
    }
    
    var longitude, latitude: String
    
    enum CodingKeys: String, CodingKey {
        case longitude, latitude
    }
}

// MARK: - BankInformation
struct ClinicBankInformation: Codable, Equatable {
    internal init() {
        self.intents = [ClinicIntent()]
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let intents = try? keyedCodingContainer.decode([ClinicIntent].self, forKey: CodingKeys.intents) {
            self.intents = intents
        } else {
            self.intents = [ClinicIntent()]
        }
    }
    
    var intents: [ClinicIntent]
    
    enum CodingKeys: String, CodingKey {
        case intents
    }
}

// MARK: - Intent
struct ClinicIntent: Codable, Equatable {
    internal init() {
        self.card = ClinicCard()
        self.setupIntentID = ""
        self.paymentMethodID = ""
        self.status = ""
        self.isDefault = false
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let card = try? keyedCodingContainer.decode(ClinicCard.self, forKey: CodingKeys.card) {
            self.card = card
        } else {
            self.card = ClinicCard()
        }
        
        if let setupIntentID = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.setupIntentID) {
            self.setupIntentID = setupIntentID
        } else {
            self.setupIntentID = ""
        }
        
        if let paymentMethodID = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.paymentMethodID) {
            self.paymentMethodID = paymentMethodID
        } else {
            self.paymentMethodID = ""
        }
        
        if let status = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.status) {
            self.status = status
        } else {
            self.status = ""
        }
        
        if let isDefault = try? keyedCodingContainer.decode(Bool.self, forKey: CodingKeys.isDefault) {
            self.isDefault = isDefault
        } else {
            self.isDefault = false
        }
    }
    
    var card: ClinicCard
    var setupIntentID, paymentMethodID, status: String
    var isDefault: Bool

    enum CodingKeys: String, CodingKey {
        case card
        case setupIntentID = "setupIntentId"
        case paymentMethodID = "paymentMethodId"
        case status
        case isDefault
    }
}

// MARK: - Card
struct ClinicCard: Codable, Equatable {
    internal init() {
        self.brand = ""
        self.expMonth = ""
        self.expYear = ""
        self.last4 = ""
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let brand = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.brand) {
            self.brand = brand
        } else {
            self.brand = ""
        }
        
        if let expMonth = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.expMonth) {
            self.expMonth = expMonth
        } else {
            self.expMonth = ""
        }
        
        if let expYear = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.expYear) {
            self.expYear = expYear
        } else {
            self.expYear = ""
        }
        
        if let last4 = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.last4) {
            self.last4 = last4
        } else {
            self.last4 = ""
        }
    }
    
    var brand, expMonth, expYear, last4: String
    
    enum CodingKeys: String, CodingKey {
        case brand, expMonth, expYear, last4
    }
}

// MARK: - DetailInformation
struct ClinicDetailInformation: Codable, Equatable {
    internal init() {
        self.managerName = ""
        self.parking = ""
        self.radiography = ""
        self.ultrasonic = ""
        self.charting = ""
        self.software = ""
    }
    
    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let managerName = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.managerName) {
            self.managerName = managerName
        } else {
            self.managerName = ""
        }
        
        if let parking = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.parking) {
            self.parking = parking
        } else {
            self.parking = ""
        }
        
        if let radiography = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.radiography) {
            self.radiography = radiography
        } else {
            self.radiography = ""
        }
        
        if let ultrasonic = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.ultrasonic) {
            self.ultrasonic = ultrasonic
        } else {
            self.ultrasonic = ""
        }
        
        if let charting = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.charting) {
            self.charting = charting
        } else {
            self.charting = ""
        }
        
        if let software = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.software) {
            self.software = software
        } else {
            self.software = ""
        }
    }
    
    var managerName, parking, radiography, ultrasonic: String
    var charting, software: String
    
    enum CodingKeys: String, CodingKey {
        case managerName, parking, radiography, ultrasonic, charting, software
    }
}

