//
//  GreenFilledButton.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/23/22.
//

import SwiftUI

struct GreenFilledButton: View {
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
                .fill(Color.sGreen)
        )
        .padding(.horizontal)
    }
}

struct GreenFilledButton_Previews: PreviewProvider {
    static var previews: some View {
        GreenFilledButton(title: "done")
    }
}
