//
//  BlueFilledText.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/8/22.
//

import SwiftUI

struct BlueFilledText: View {
    @Environment(\.colorScheme) var colorScheme
    
    var title: String
    var font: Font = .poppinsBigHeadline
    var verticalPadding: CGFloat = 10
    
    var body: some View {
        Text(title)
            .font(font)
            .fontWeight(.semibold)
            .foregroundColor(.white)
            .padding(.horizontal, 8)
            .padding(.vertical, verticalPadding)
            .frame(maxWidth: .infinity)
            .background(
                Capsule()
                    .fill(colorScheme == .light ? Color.sBlue : Color.sCyan)
            )
            .padding(.horizontal)
    }
}

struct BlueFilledText_Previews: PreviewProvider {
    static var previews: some View {
        BlueFilledText(title: "Button")
    }
}
