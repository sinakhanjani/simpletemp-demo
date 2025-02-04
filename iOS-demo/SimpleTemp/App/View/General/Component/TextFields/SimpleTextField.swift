//
//  SimpleTextField.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct SimpleTextField: View {
    @Binding var text: String
    
    @Environment(\.colorScheme) var colorScheme
    
    var SFImage: String
    var placeholder: String
    
    var body: some View {
        HStack(spacing: 16) {
            Image(systemName: SFImage)
                .foregroundColor(.secondary)
                .imageScale(.large)
                .frame(width: 16, height: 16)
            
            TextField(placeholder, text: $text)
                .font(.poppinsSubheadline)
                .disableAutocorrection(true)
                
        }
        .padding(.horizontal)
        .padding(.vertical, 12)
        .overlay(
            Capsule()
                .stroke(Color.secondary, lineWidth: colorScheme == .light ? 0.3 : 1)
        )
        .padding(.horizontal, 32)
    }
}
