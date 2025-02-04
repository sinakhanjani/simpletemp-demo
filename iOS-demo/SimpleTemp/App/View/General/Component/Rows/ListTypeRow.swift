//
//  ListTypeRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/15/22.
//

import SwiftUI

struct ListTypeRow: View {
    var SFImage: String
    var title: String
    let textColor: Color
    let imgColor: Color
    let showChevronRight: Bool
    public var action: (() -> Void)?
    
    init(SFImage: String, title: String, textColor: Color = Color.secondary, imgColor: Color = Color.sCyan, showChevronRight: Bool = true, action: (() -> Void)? = nil) {
        self.SFImage = SFImage
        self.title = title
        self.textColor = textColor
        self.imgColor = imgColor
        self.showChevronRight = showChevronRight
        self.action = action
    }

    var body: some View {
            HStack {
                HStack(spacing: 16) {
                    Image(systemName: SFImage)
                        .foregroundColor(imgColor)
                        .imageScale(.medium)
                        .frame(width: 16, height: 16)
                    
                    Text(title)
                        .font(.poppinsCallout)
                        .foregroundColor(textColor)
                        .multilineTextAlignment(.leading)
                }
                
                Spacer()
                
                if showChevronRight {
                    ChevronRight()
                        .imageScale(.small)
                }
            }
            .padding(8)
            .padding(.horizontal)
        }
}

struct CustomListRow_Previews: PreviewProvider {
    static var previews: some View {
        ListTypeRow(SFImage: "person.circle", title: "Change Password")
            .previewLayout(.sizeThatFits)
        
        ListTypeRow(SFImage: "lock", title: "Change Password")
            .previewLayout(.sizeThatFits)
            .preferredColorScheme(.dark)
    }
}

