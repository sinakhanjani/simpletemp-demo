//
//  SelectedItemModel.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/22/22.
//

import SwiftUI

struct SelectedItemModel: Identifiable, Equatable {
    var id: String
    var item: String
    
    init(item: String) {
        self.item = item
        self.id = UUID().uuidString
    }

    init(id: String, item: String) {
        self.item = item
        self.id = id
    }
}
