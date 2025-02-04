//
//  BadConnection.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/14/22.
//

import SwiftUI

struct BadConnection: View {
    @State private var animateGradient = false
    
    @Environment(\.colorScheme) var colorScheme
    @Environment(\.dismiss) var dismiss
    
    var width =  UIScreen.main.bounds.width
    
    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [.sBlue, .sCyan, .sCyan, .sBlue, .sBlue]), startPoint: animateGradient ? .topTrailing : .bottomLeading, endPoint: animateGradient ? .bottom : .bottomLeading)
                .onAppear {
                    withAnimation(.linear(duration: 6).repeatForever(autoreverses: true)) {
                        animateGradient.toggle()
                    }
                }
                .blur(radius: 32, opaque: true)
                .ignoresSafeArea()
            
            VStack(spacing: 16) {
                Spacer()
                
                Image(systemName: "waveform.path")
                    .resizable()
                    .frame(width: width / 5, height: width / 6)
        
                Text("Connection lost")
                    .font(.poppinsTitle)

                Spacer()
                
                Text("Please check your internet connection...")
                    .font(.poppinsBigHeadline)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                
                Spacer()
            }
            .foregroundColor(.white)
        }
    }
}

struct BadConnection_Previews: PreviewProvider {
    static var previews: some View {
        BadConnection()
            .preferredColorScheme(.dark)
    }
}
