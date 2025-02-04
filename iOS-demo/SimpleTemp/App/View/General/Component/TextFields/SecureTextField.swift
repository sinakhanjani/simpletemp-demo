//
//  SecureTextField.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct SecureTextField: View {
    enum Field: Hashable {
        case textField
        case secureField
    }
    
    @State var showPassword: Bool = false
    @Binding var text: String
    @FocusState private var focusedField: Field?
    
    @Environment(\.colorScheme) var colorScheme
    
    var SFImage: String = "lock"
    var placeholder: String
    
    var body: some View {
        HStack(spacing: 16) {
            Image(systemName: SFImage)
                .foregroundColor(.secondary)
                .imageScale(.large)
                .frame(width: 16, height: 16)
            
            ZStack(alignment: .trailing) {
                SecureField(placeholder, text: $text)
                    .font(.poppinsSubheadline)
                    .focused($focusedField, equals: .secureField)
                    .opacity(showPassword ? 0 : 1)
                
                TextField(placeholder, text: $text)
                    .font(.poppinsSubheadline)
                    .focused($focusedField, equals: .textField)
                    .opacity(showPassword ? 1 : 0)
            }
            .disableAutocorrection(true)
            .autocapitalization(.none)
            .textContentType(.oneTimeCode)
            
            Button(action: {
                showPassword.toggle()
                if showPassword { focusedField = .textField } else { focusedField = .secureField }
            }, label: {
                Image(systemName: self.showPassword ? "eye.slash.fill" : "eye.fill")
                    .font(.poppinsBody)
                    .foregroundColor(.secondary)
            })
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

struct SecureTextField_Previews: PreviewProvider {
    static var previews: some View {
        SecureTextField(text: .constant(""), placeholder: "")
            .previewLayout(.sizeThatFits)
    }
}
