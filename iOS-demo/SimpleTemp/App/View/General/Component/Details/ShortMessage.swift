//
//  ShortMessage.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/20/22.
//

import SwiftUI

struct ShortMessage: View {
    var message: String = "successfully uploaded"
    var sfImage: String = "checkmark.circle"
    var color: Color = .sGreen
    
    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: sfImage)
            Text(message)
        }
        .font(.poppinsCaption)
        .foregroundColor(color)
        .padding(.horizontal, 8)
    }
}

struct ShortMessage_Previews: PreviewProvider {
    static var previews: some View {
        ShortMessage()
    }
}
