//
//  FAQModel.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/26/22.
//

import SwiftUI

struct FAQModel: Codable, Identifiable, Equatable {
    internal init() {
        self.id = ""
        self.question = ""
        self.answer = ""
        self.userType = ""
        self.createdAt = ""
        self.updatedAt = ""
    }
    
    let id, question, answer: String
    let userType: String
    let createdAt, updatedAt: String

    init(from decoder: Decoder) throws {
        let keyedCodingContainer = try decoder.container(keyedBy: CodingKeys.self)

        if let id = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.id) {
            self.id = id
        } else {
            self.id = ""
        }
        
        if let question = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.question) {
            self.question = question
        } else {
            self.question = ""
        }
        
        if let answer = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.answer) {
            self.answer = answer
        } else {
            self.answer = ""
        }
        
        if let userType = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.userType) {
            self.userType = userType
        } else {
            self.userType = ""
        }
        
        if let createdAt = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.createdAt) {
            self.createdAt = createdAt
        } else {
            self.createdAt = ""
        }
        
        if let updatedAt = try? keyedCodingContainer.decode(String.self, forKey: CodingKeys.updatedAt) {
            self.updatedAt = updatedAt
        } else {
            self.updatedAt = ""
        }
    }
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case question, answer, userType, createdAt, updatedAt
    }
}
