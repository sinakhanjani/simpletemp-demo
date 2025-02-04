//
//  AccountType.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import Foundation

enum AccountTypeModel: Equatable {
    static let key = "account_type"
    
    enum DentalTemp: String {
        case nurse, hygienist
    }
    
    init() {
        let value = UserDefaults.standard.string(forKey: AccountTypeModel.key)
        if let value = value {
            switch value {
            case "clinic": self = .clinic
            case "nurse": self = .dentalTemp(.nurse)
            case "hygienist": self = .dentalTemp(.hygienist)
            default: self = .none
            }
        } else {
            self = .none
        }
    }
    
    public var value: String {
        switch self {
        case .dentalTemp(let dentalTemp):
            return dentalTemp.rawValue
        case .clinic:
            return "clinic"
        case .none:
            return "none"
        }
    }
    
    case dentalTemp(DentalTemp)
    case clinic
    case none
}
