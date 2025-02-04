//
//  ConfettiAnimation.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/7/22.
//

import SwiftUI

struct ConfettiAnimation: View {
    var body: some View {
        ZStack {
            Circle()
                .fill(Color.sCyan)
                .frame(width: 88, height: 88)
                .overlay(
                    Image(systemName: "checkmark")
                        .font(.poppinsLargeTitle)
                        .foregroundColor(.white)
                )
            
            Circle()
                .fill(Color.sBlue)
                .frame(width: 12, height: 12)
                .modifier(ParticlesModifier())
                .offset(x: 80, y: 0)
            
            Circle()
                .fill(Color.sCyan)
                .frame(width: 16, height: 16)
                .modifier(ParticlesModifier())
                .offset(x: -80, y: 32)
            
            Circle()
                .fill(Color.sBlue)
                .frame(width: 12, height: 12)
                .modifier(ParticlesModifier())
                .offset(x: -80, y: -32)
            
            Circle()
                .fill(Color.sBlue)
                .frame(width: 6, height: 6)
                .modifier(ParticlesModifier())
                .offset(x: -56, y: -16)
            
            Circle()
                .fill(Color.sCyan)
                .frame(width: 16, height: 16)
                .modifier(ParticlesModifier())
                .offset(x: 32, y: -80)
            
            Circle()
                .fill(Color.sBlue)
                .frame(width: 16, height: 16)
                .modifier(ParticlesModifier())
                .offset(x: 44, y: 80)
            
            Circle()
                .fill(Color.sCyan)
                .frame(width: 8, height: 8)
                .modifier(ParticlesModifier())
                .offset(x: 52, y: 32)
        }
        .padding(44)
    }
}

struct FireworkParticlesGeometryEffect : GeometryEffect {
    var time : Double
    var speed = Double.random(in: 20 ... 100)
    var direction = Double.random(in: -Double.pi ...  Double.pi)
    
    var animatableData: Double {
        get { time }
        set { time = newValue }
    }
    
    func effectValue(size: CGSize) -> ProjectionTransform {
        let xTranslation = speed * cos(direction) * time
        let yTranslation = speed * sin(direction) * time
        let affineTranslation =  CGAffineTransform(translationX: xTranslation, y: yTranslation)
        return ProjectionTransform(affineTranslation)
    }
}

struct ParticlesModifier: ViewModifier {
    @State var time = 0.0
    @State var scale = 0.5
    let duration = 3.0
    
    func body(content: Content) -> some View {
        ZStack {
            ForEach(0..<80, id: \.self) { index in
                content
                    .hueRotation(Angle(degrees: time ))
                    .scaleEffect(scale)
                    .modifier(FireworkParticlesGeometryEffect(time: time))
                    .opacity(((duration-time) / duration))
            }
        }
        .onAppear {
            withAnimation (.easeOut(duration: duration)) {
                self.time = duration
            }
        }
    }
}

struct ConfettiAnimation_Previews: PreviewProvider {
    static var previews: some View {
        ConfettiAnimation()
    }
}
