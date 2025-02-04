//
//  OTPModelData.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/10/22.
//

import SwiftUI


enum OTPField {
    case field1
    case field2
    case field3
    case field4
    case field5
    case field6
}

class OTPModelData: ObservableObject {
    internal init() {
        self.OTPFields = Array(repeating: "", count: 6)
    }
    
    @Published var OTPFields: [String]
    
    public func activeStateForIndex(index: Int) -> OTPField {
        switch index {
        case 0: return .field1
        case 1: return .field2
        case 2: return .field3
        case 3: return .field4
        case 4: return .field5
        default: return .field6
        }
    }
    
    public func checkStates() -> Bool {
        for index in 0..<6 {
            if OTPFields[index].isEmpty { return true }
        }
        
        return false
    }
    
    public func getOTPText() -> String {
        
        return OTPFields.joined(separator: "")
    }
}
