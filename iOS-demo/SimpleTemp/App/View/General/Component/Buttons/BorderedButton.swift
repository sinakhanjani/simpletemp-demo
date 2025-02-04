//
//  BorderedButton.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct BorderedButton: View {
    @Environment(\.colorScheme) var colorScheme
    
    var title: String
    public var action: (() -> Void)?
    
    var body: some View {
        Button {
            action?()
        } label: {
            Text(title)
                .font(.poppinsBigHeadline)
                .fontWeight(.semibold)
                .foregroundColor(.secondary)
                .padding(.horizontal, 8)
                .padding(.vertical, 10)
                .frame(maxWidth: .infinity)
        }
        .clipShape(Capsule())
        .overlay(
            Capsule()
                .stroke(.secondary, lineWidth: colorScheme == .light ? 1 : 2)
        )
        .padding(.horizontal)
    }
}

struct BorderedButton_Previews: PreviewProvider {
    static var previews: some View {
        BorderedButton(title: "Have an account? Log in")
            .previewLayout(.sizeThatFits)
        
    }
}
