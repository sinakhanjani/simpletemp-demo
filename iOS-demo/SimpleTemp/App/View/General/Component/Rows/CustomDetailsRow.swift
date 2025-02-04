//
//  CustomDetailsRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/4/22.
//

import SwiftUI

struct CustomDetailsRow: View {
    var leftText: String
    var rightText: String
    var rightTextFont: Font = .poppinsSubheadline
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        HStack {
            Text(leftText)
                .foregroundColor(colorScheme == .light ? .black : .white)
                .font(.poppinsSubheadline)
            
            Spacer()
            
            Text(rightText)
                .foregroundColor(.secondary)
                .font(rightTextFont)     
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
    }
}

struct OfficeDetailsRow_Previews: PreviewProvider {
    static var previews: some View {
        CustomDetailsRow(leftText: "left", rightText: "right")
    }
}
