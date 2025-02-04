//
//  GrayCustomPhoneNumberTextFiled.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 7/24/22.
//

import SwiftUI

struct GrayCustomPhoneNumberTextFiled: View {
    @Binding var text: String
    
    var placeholder: String
    
    var body: some View {
        HStack {
            Text("+44")
            TextField(placeholder, text: Binding(
                get: {
                    String(text.toUKPhoneNumberFormat)
                }, set: {
                    text = $0.toUKPhoneNumberFormat
                }))
                .disableAutocorrection(true)
        }
        .font(.poppinsSubheadline)
        .padding(.horizontal)
        .padding(.vertical, 10)
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(Color.gray.opacity(0.4), lineWidth: 0.5)
                .foregroundColor(.gray)
        )
        .background(.gray.opacity(0.1))
        .cornerRadius(8)
        .padding(.horizontal)
    }
}

struct GrayCustomPhoneNumberTextFiled_Previews: PreviewProvider {
    static var previews: some View {
        GrayCustomPhoneNumberTextFiled(text: .constant(""), placeholder: "00 0000 0000")
            .previewLayout(.sizeThatFits)
    }
}
