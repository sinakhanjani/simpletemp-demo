//
//  AccountTypeRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/6/22.
//

import SwiftUI

struct AccountTypeRow: View {
    var backgroundColor: Color
    var title: String
    var image: String
    
    var body: some View {
        ZStack {
            ZStack {
                Color.white
                    .frame(height: 104)
                    .cornerRadius(8)
                    .padding(.top, 44)
                
                LinearGradient(colors: [
                    backgroundColor.opacity(0.8),
                    backgroundColor.opacity(0.9),
                    backgroundColor,
                    backgroundColor.opacity(0.9),
                    backgroundColor.opacity(0.8),
                    backgroundColor.opacity(0.7),
                    backgroundColor.opacity(0.6),
                    backgroundColor.opacity(0.5),
                ],
                               startPoint: .bottomLeading,
                               endPoint: .trailing)
                .frame(height: 104)
                .cornerRadius(8)
                .padding(.top, 44)
                .overlay(
                    Circle()
                        .trim(to: 0.5)
                        .fill(
                            RadialGradient(gradient: Gradient(colors: [
                                .white.opacity(0.2),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0.2),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0),
                                .white.opacity(0.2),
                                .white.opacity(0.2)
                            ]), center: .center, startRadius: 70, endRadius: 96)
                        )
                        .opacity(0.5)
                        .frame(width: 200 ,height: 200)
                        .offset(x: UIScreen.main.bounds.width / 2.4, y: -100)
//                        .clipped()
                        .padding(.top, 140)
                )
                .clipped()
            }
            
            HStack(spacing: 16) {
                Text(title)
                    .font(.poppinsBigHeadline)
                    .foregroundColor(.white)
                    .fontWeight(.semibold)
                
                Spacer()
                
                Image(image)
                    .offset(y: -44)
                    .padding(.bottom, -44)
            }
            .padding(.top, 44)
            .padding(.horizontal, 32)
        }
        .padding(.horizontal)
    }
}

struct CustomRow_Previews: PreviewProvider {
    static var previews: some View {
        AccountTypeRow(backgroundColor: Color.blue, title: "Dental Practice", image: "Clinic")
            .previewLayout(.sizeThatFits)
            .preferredColorScheme(.dark)
    }
}


