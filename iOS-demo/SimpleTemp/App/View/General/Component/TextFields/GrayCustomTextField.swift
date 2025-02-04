//
//  GrayCustomTextField.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/16/22.
//

import SwiftUI

struct GrayCustomTextField: View {
    @Binding var text: String
    
    var placeholder: String
    
    var body: some View {
        TextField(placeholder, text: $text)
            .font(.poppinsSubheadline)
            .disableAutocorrection(true)
            .padding(.horizontal)
            .padding(.vertical, 10)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray.opacity(0.4), lineWidth: 0.5)
                    .foregroundColor(.gray)
            )
            .background(.gray.opacity(0.1))
            .foregroundColor(.secondary)
            .cornerRadius(8)
            .padding(.horizontal)
    }
}

struct GrayCustomTextField_Previews: PreviewProvider {
    static var previews: some View {
        GrayCustomTextField(text: .constant(""), placeholder: "Example No 23, 34th st")
            .previewLayout(.sizeThatFits)
    }
}
