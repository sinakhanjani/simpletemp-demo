//
//  ClinicEditProfileRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/22/22.
//

import SwiftUI

struct EditProfileRow<Content>: View where Content: View {
    var sfImage: String
    var title: String
    private let content: Content
    
    @Environment(\.colorScheme) var colorScheme
    
    init(sfImage: String, title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.sfImage = sfImage
        self.content = content()
    }
    
    var body: some View {
        HStack(spacing: 8) {
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Image(systemName: sfImage)
                        .renderingMode(.template)
                        .foregroundColor(.sCyan)
                    
                    Text(title)
                        .font(.poppinsSubheadline)
                        .foregroundColor(colorScheme == .light ? .black : .white)
                }
                
                VStack(alignment: .leading) {
                    content
                        .font(.poppinsCaption)
                        .foregroundColor(.secondary)
                }
            }
          
            Spacer()
            
            ChevronRight()
        }
    }
}

struct ClinicEditProfileRow_Previews: PreviewProvider {
    static var previews: some View {
        EditProfileRow(sfImage: "trash", title: "Account Information") {
            VStack(alignment: .leading) {
                Text("description1")
                Text("description2")
            }
        }
        .previewLayout(.sizeThatFits)
    }
}

