//
//  ProfileImage.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/29/22.
//

import SwiftUI

struct ProfileImage: View {
    var photoURL: String
    var sizeRelativeToWidth: CGFloat
    var hasBorder: Bool = true
    
    let width = UIScreen.main.bounds.width
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        ZStack {
            if !photoURL.isEmpty {
                Image(uiImage: photoURL.loadImage())
                    .resizable()
            } else {
                ZStack {
                    Circle()
                        .fill(Color.white)
                        .frame(width: width / sizeRelativeToWidth - 1, height: width / sizeRelativeToWidth - 1, alignment: .center)
                    
                    Circle()
                        .fill(Color.black.opacity(0.5))
                        .frame(width: width / sizeRelativeToWidth - 1, height: width / sizeRelativeToWidth - 1, alignment: .center)
                    
                    Image("emptyProfilePicture")
                        .resizable()
                        .frame(width: width / sizeRelativeToWidth, height: width / sizeRelativeToWidth, alignment: .center)
                        .opacity(0.9)
                }
            }
        }
        .clipShape(Circle())
        .scaledToFill()
        .frame(width: width / sizeRelativeToWidth, height: width / sizeRelativeToWidth)
        .overlay(
            Circle()
                .stroke(.white.opacity(0.8), lineWidth: hasBorder ? 1 : 0)
        )
        .shadow(radius: hasBorder ? 2 : 0)
    }
}

struct ProfileImage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileImage(photoURL: "", sizeRelativeToWidth: 3)
    }
}
