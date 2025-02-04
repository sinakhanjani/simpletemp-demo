//
//  OrangeFilledButton.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/8/22.
//

import SwiftUI

struct OrangeFilledButton: View {
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
                .foregroundColor(.white)
                .padding(.horizontal, 8)
                .padding(.vertical, 10)
                .frame(maxWidth: .infinity)
        }
        .background(
            Capsule()
                .fill(Color.orange)
        )
        .padding(.horizontal)
    }
}

struct OrangeFilledButton_Previews: PreviewProvider {
    static var previews: some View {
        OrangeFilledButton(title: "orange")
    }
}
