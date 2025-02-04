//
//  CyanListTypeRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/4/22.
//

import SwiftUI

struct CyanListTypeRow: View {
    var title: String
    
    var body: some View {
        HStack {
            Text(title)
                .font(.poppinsCallout)
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .imageScale(.small)
        }
        .foregroundColor(.white)
        .padding(.horizontal)
        .padding(.vertical, 10)
        .frame(maxWidth: .infinity)
        .background(Color.sCyan)
        .cornerRadius(8)
    }
}

struct CyanListTypeRow_Previews: PreviewProvider {
    static var previews: some View {
        CyanListTypeRow(title: "Hello")
            .previewLayout(.sizeThatFits)
    }
}
