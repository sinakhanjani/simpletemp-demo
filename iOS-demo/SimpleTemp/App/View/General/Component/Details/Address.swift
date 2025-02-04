//
//  Address.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/1/22.
//

import SwiftUI

struct Address: View {
    var textColor: Color
    var imageColor: Color
    var streetName: String
    var city: String
    var distance: String
    
    var body: some View {
        HStack {
            Image("address")
                .renderingMode(.template)
                .foregroundColor(imageColor)
            
            HStack(spacing: 4) {
                Text("\(streetName), \(city)")
                .font(.poppinsSubheadline)
                .lineLimit(2)

            }
            
            if !distance.isEmpty {
            Circle()
                .frame(width: 4, height: 4, alignment: .center)
            }
            
            Text(distance)
                .font(.poppinsSubheadline)
                .lineLimit(1)
        }
        .foregroundColor(textColor)
    }}

struct address_Previews: PreviewProvider {
    static var previews: some View {
        Address(textColor: .green, imageColor: .secondary, streetName: "streetName", city: "city", distance: "distance")
            .previewLayout(.sizeThatFits)
    }
}
