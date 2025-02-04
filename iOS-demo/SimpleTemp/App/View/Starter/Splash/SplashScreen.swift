//
//  SplashScreen.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct SplashScreen: View {
    @Environment(\.colorScheme) var colorScheme
    
    let width = UIScreen.main.bounds.width
    let height = UIScreen.main.bounds.height
    
    var body: some View {
        ZStack {
            LinearGradient(colors: [
                (colorScheme == .light ? .secondary.opacity(0.2) : .secondary.opacity(0.1)),
                (colorScheme == .light ? .gray.opacity(0.2) : .sBlue.opacity(0.2)),
                (colorScheme == .light ? .blue.opacity(0.2) : .sBlue.opacity(0.5))
            ],
                           startPoint: .topLeading,
                           endPoint: .bottom)
            
            VStack {
                Spacer()
                Spacer()
                
                VStack(alignment: .center, spacing: 16) {
                    Image("Logo")
                        .resizable()
                        .frame(width: width / 5, height: width / 5)
                        
                    HStack(spacing: 2) {
                        Text("Simple")
                            .font(.poppinsLargeTitle)
                            .foregroundColor(.sCyan)
                        
                        Text("Temp")
                            .font(.poppinsLargeTitle)
                            .foregroundColor(.sBlue)
                    }
                    
                    Text("Version \(Bundle.appVersion)")
                        .font(.poppinsSubheadline)
                        .foregroundColor(.secondary)
                }
                
                Spacer()
                Spacer()
                
                ZStack(alignment: .bottom) {
                    Image("SplashBackground")
                        .resizable()
                        .frame(width: width - 32, height: width - 16)
                    
                    Image("Splash")
                        .resizable()
                        .frame(width: width - 128, height: width - 128)
                }
                
                Spacer()
            }
        }
        .ignoresSafeArea()
    }
}

struct SplashScreen_Previews: PreviewProvider {
    static var previews: some View {
        SplashScreen()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
            .previewInterfaceOrientation(.portrait)
            
        
        SplashScreen()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)
    }
}
