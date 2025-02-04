//
//  ColorHint.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/31/22.
//

import SwiftUI

struct ColorHint: View {
    var color: Color
    var hint: String
    
    var body: some View {
        HStack {
            Circle()
                .fill(color)
                .frame(width: 16, height: 16)
            
            Text(hint)
                .font(.poppinsCaption)
                .foregroundColor(.secondary)
        }
    }
}

struct ColorHint_Previews: PreviewProvider {
    static var previews: some View {
        ColorHint(color: .green, hint: "Confirmed shift")
            .previewLayout(.sizeThatFits)
    }
}
