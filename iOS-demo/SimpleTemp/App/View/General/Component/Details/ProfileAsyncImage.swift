//
//  ProfileAsyncImage.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/17/22.
//

import SwiftUI

struct ProfileAsyncImage: View {
    var photoURL: String
    var sizeRelativeToWidth: CGFloat
    var hasBorder: Bool = true
    
    let width = UIScreen.main.bounds.width
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        ZStack {
            if !photoURL.isEmpty {
                AsyncImage(url: URL(string: photoURL)) { phase in
                    if let image = phase.image {
                        image
                            .resizable()
                    } else if phase.error != nil {
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
                    } else {
                        Circle()
                            .fill(.ultraThinMaterial)
                            .frame(width: width / sizeRelativeToWidth, height: width / sizeRelativeToWidth, alignment: .center)
                            .overlay(ProgressView())
                    }
                }
            }
            else {
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
        .frame(width: width / sizeRelativeToWidth, height: width / sizeRelativeToWidth, alignment: .center)
        .overlay(
            Circle()
                .stroke(.white.opacity(0.8), lineWidth: hasBorder ? 1 : 0)
        )
        .shadow(radius: hasBorder ? 2 : 0)
    }
}

struct ProfileAsyncImage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileAsyncImage(photoURL: "", sizeRelativeToWidth: 3)
            .previewLayout(.sizeThatFits)
                    .preferredColorScheme(.dark)
    }
}
