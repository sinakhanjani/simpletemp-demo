//
//  CustomHeader.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/16/22.
//

import SwiftUI

struct CustomHeader: View {
    var title: String
    
    var body: some View {
        HStack {
            RoundedRectangle(cornerRadius: 8)
                .frame(width: 2, height: 16)
                .foregroundColor(.sCyan)
            
            Text(title)
                .font(.poppinsSubheadline)
                .fontWeight(.medium)
                .foregroundColor(.secondary)
            
            Spacer()
        }
        .padding(8)
        .padding(.leading)
    }
}

struct CustomHeader_Previews: PreviewProvider {
    static var previews: some View {
        CustomHeader(title: "Address Line 1")
            .previewLayout(.sizeThatFits)
    }
}
