//
//  ScaledButtonStyle.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 9/14/22.
//

import SwiftUI

struct ScaledButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.94 : 1)
            .animation(.easeInOut, value: configuration.isPressed)
    }
}
