//
//  ChevronRight.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/15/22.
//

import SwiftUI

struct ChevronRight: View {
    var body: some View {
        Image(systemName: "chevron.right")
            .font(.poppinsHeadline)
            .foregroundColor(.secondary)
    }
}

struct ChevronRight_Previews: PreviewProvider {
    static var previews: some View {
        ChevronRight()
            .previewLayout(.sizeThatFits)
    }
}

