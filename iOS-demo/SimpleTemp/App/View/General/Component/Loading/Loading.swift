//
//  Loading.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import SwiftUI

struct Loading: View {
//    @Binding public var isAnimating: Bool
    @State private var isRotated = true
    
    @Environment(\.colorScheme) var colorScheme
    
    let width = UIScreen.main.bounds.width
    
    var body: some View {
        ZStack {
            Color.white
                .opacity(0)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .ignoresSafeArea()
            
            VStack(spacing: 24) {
                Image("Logo")
                    .resizable()
                    .frame(width: width / 8, height: width / 8)
                    .rotationEffect(Angle.degrees(isRotated ? -360 : 0))
                    .onAppear {
                        withAnimation(
                            .linear
                                .repeatForever(autoreverses: false)
                                .speed(0.3)
                        ) {
                            isRotated.toggle()
                        }
                    }
            }
            .padding()
            .background(
                Circle()
                    .fill(colorScheme == .light ? Color.white : Color.black)
                    .frame(width: width / 7, height: width / 7)
            )
            .cornerRadius(8)
        }
        .zIndex(1)
    }
}

struct Loading_Previews: PreviewProvider {
    static var previews: some View {
        Loading()
            .previewLayout(.sizeThatFits)
            .preferredColorScheme(.dark)
    }
}
