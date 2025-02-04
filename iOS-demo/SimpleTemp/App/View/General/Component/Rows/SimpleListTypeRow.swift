//
//  SimpleListTypeRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/16/22.
//

import SwiftUI

struct SimpleListTypeRow: View {
    @Environment(\.colorScheme) var colorScheme
    
    var title: String
    
    var body: some View {
        HStack(spacing: 16) {
            Text(title)
                .font(.poppinsCallout)
                .foregroundColor(.secondary)
                .lineLimit(1)
            
            Spacer()
            
            ChevronRight()
                .imageScale(.small)
        }
        .padding(.horizontal)
        .padding(.vertical, 12)
        .background(colorScheme == .light ? Color.secondary.opacity(0.1) : Color.secondary.opacity(0.2))
        .cornerRadius(8)
        .padding(.horizontal)
    }
}

struct SimpleListTypeRow_Previews: PreviewProvider {
    static var previews: some View {
        SimpleListTypeRow(title: "hello")
    }
}
