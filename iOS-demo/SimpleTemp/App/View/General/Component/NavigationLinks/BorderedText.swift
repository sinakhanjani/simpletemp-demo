//
//  BorderedText.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/8/22.
//

import SwiftUI

struct BorderedText: View {
    @Environment(\.colorScheme) var colorScheme
    
    var title: String
    
    var body: some View {
        Text(title)
            .font(.poppinsBigHeadline)
            .padding(.horizontal)
            .padding(.vertical, 10)
            .clipShape(Capsule())
            .frame(maxWidth: .infinity)
            .overlay(
                Capsule()
                    .stroke(.secondary, lineWidth: colorScheme == .light ? 1 : 2)
            )
            .foregroundColor(.secondary)
            .padding(.horizontal)
    }
}

struct BorderedText_Previews: PreviewProvider {
    static var previews: some View {
        BorderedText(title: "s")
    }
}
