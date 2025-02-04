//
//  Stars.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/21/22.
//

import SwiftUI

struct Stars: View {
    init(count: Int, max: Int = 5, showEmptyStars: Bool, spaceBetweenStars: CGFloat = 8, isLeftToright: Bool = false) {
        self.count = count
        self.max = max
        self.showEmptyStars = showEmptyStars
        self.spaceBetweenStars = spaceBetweenStars
        self.isLeftToright = isLeftToright
    }
    
    var count: Int
    var max: Int = 5
    var showEmptyStars: Bool
    var spaceBetweenStars: CGFloat = 8
    var isLeftToright: Bool = false
    
    var body: some View {
        ZStack {
            if isLeftToright {
                HStack(spacing: spaceBetweenStars) {
                    HStack(spacing: spaceBetweenStars) {
                        ForEach(0..<count, id: \.self) { _ in
                            Image(systemName: "star.fill")
                        }
                        .foregroundColor(.yellow)
                    }
                    
                    HStack(spacing: spaceBetweenStars) {
                        ForEach(0..<(max - count), id: \.self) { _ in
                            Image(systemName: "star")
                        }
                        .foregroundColor(showEmptyStars ? .yellow : .clear)
                    }
                }
            } else {
                HStack(spacing: spaceBetweenStars) {
                    HStack(spacing: spaceBetweenStars) {
                        ForEach(0..<(max - count) , id: \.self) { _ in
                            Image(systemName: "star")
                        }
                        .foregroundColor(showEmptyStars ? .yellow : .clear)
                    }
                    
                    HStack(spacing: spaceBetweenStars) {
                        ForEach(0..<count , id: \.self) { _ in
                            Image(systemName: "star.fill")
                        }
                        .foregroundColor(.yellow)
                    }
                }
            }
        }
    }
}

struct Stars_Previews: PreviewProvider {
    static var previews: some View {
        Stars(count: 2, showEmptyStars: true, isLeftToright: true)
    }
}
