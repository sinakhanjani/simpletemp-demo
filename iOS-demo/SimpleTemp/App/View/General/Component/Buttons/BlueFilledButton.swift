//
//  BlueFilledButton.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct BlueFilledButton: View {
    @Environment(\.colorScheme) var colorScheme

    var title: String
    var font: Font = .poppinsBigHeadline
    var verticalPadding: CGFloat = 10
    public var action: (() -> Void)?
    
    var body: some View {
        Button {
            action?()
        } label: {
            Text(title)
                .font(font)
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .padding(.horizontal, 8)
                .padding(.vertical, verticalPadding)
                .frame(maxWidth: .infinity)
        }
        .background(
            Capsule()
                .fill(colorScheme == .light ? Color.sBlue : Color.sCyan)
        )
        .padding(.horizontal)
    }
}

struct BlueFilledButton_Previews: PreviewProvider {
    static var previews: some View {
        BlueFilledButton(title: "Sing Up")
//            .previewLayout(.sizeThatFits)
    }
}
