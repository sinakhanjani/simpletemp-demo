//
//  ClinicProfileHead.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/20/22.
//

import SwiftUI

struct ClinicProfileHead: View {
    @EnvironmentObject private var clinicModelData: ClinicModelData
    
    @Environment(\.colorScheme) var colorScheme
    
    public var action: (() -> Void)?
    let width = UIScreen.main.bounds.width
    
    var body: some View {
        HStack(spacing: 16) {
            ProfileAsyncImage(photoURL: clinicModelData.clinicModel.photoURL, sizeRelativeToWidth: 3.5, hasBorder: false)
                .overlay(
                    ZStack {
                        Circle()
                            .trim(from: 0.1, to: 0.75)
                            .stroke(LinearGradient(colors: [
                                .sBlue,
                                .sBlue,
                                .sCyan,
                                .sCyan,
                                .sCyan,
                                .sCyan,
                                .sBlue
                            ],
                                                   startPoint: .topLeading,
                                                   endPoint: .bottomTrailing)
                                    
                                    , style: StrokeStyle(lineWidth: 4, lineCap: .round, lineJoin: .round))
                            .padding(-12)
                        
                        Image("addPhoto")
                            .resizable()
                            .padding(8)
                            .frame(width: width / 10, height: width / 10)
                            .background(colorScheme == .light ? .white : .black)
                            .clipShape(Circle())
                            .shadow(color: colorScheme == .light ? .black.opacity(0.5) : .secondary.opacity(0.5), radius: 2)
                            .offset(x: 54 , y: 32 )
                        
                    }
                )
                .padding(12)
                .onTapGesture {
                    action?()
                }
            
            VStack(alignment: .leading, spacing: 8) {
                Text(clinicModelData.clinicModel.fullname)
                    .font(.poppinsSubheadline)
                    .fontWeight(.semibold)
                    .lineLimit(2)
                
                Text(clinicModelData.clinicModel.email)
                    .font(.poppinsCaption)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
                
                VStack(alignment: .leading, spacing: 0) {
                    HStack {
                        ZStack(alignment: .leading) {
                            let width = UIScreen.main.bounds.width / 2.5
                            let multiplier = width / 100
                            let completeWidth = (Double(clinicModelData.clinicModel.profile.percentage)) * multiplier
                            
                            RoundedRectangle(cornerRadius: 16, style: .continuous)
                                .frame(width: width, height: 4)
                                .foregroundColor(colorScheme == .light ? .sWhite : .sWhite.opacity(0.2))
                            
                            RoundedRectangle(cornerRadius: 16, style: .continuous)
                                .frame(width: completeWidth, height: 6)
                                .background(
                                    LinearGradient(colors: [
                                        .sBlue,
                                        .sBlue,
                                        .sCyan
                                    ],
                                                   startPoint: .leading,
                                                   endPoint: .trailing)
                                    .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                                )
                                .foregroundColor(.clear)
                        }
                        
                        Image(systemName: "checkmark.seal.fill")
                            .foregroundColor(clinicModelData.clinicModel.profile.percentage == 100 ? .sCyan : .secondary)
                    }
                    
                    Text("\(clinicModelData.clinicModel.profile.percentage)% completed")
                        .font(.poppinsCaption)
                        .foregroundColor(clinicModelData.clinicModel.profile.percentage == 100 ? (colorScheme == .light ? .sBlue : .sCyan) : .secondary)
                }
            }
        }
        .padding()
    }
}

struct ClinicProfileHead_Previews: PreviewProvider {
    static var previews: some View {
        ClinicProfileHead()
            .environmentObject(NetworkModelData())
            .environmentObject(ClinicModelData())
            .previewLayout(.sizeThatFits)
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
            .previewInterfaceOrientation(.portrait)
        
        
        ClinicProfileHead()
            .environmentObject(NetworkModelData())
            .environmentObject(ClinicModelData())
            .previewLayout(.sizeThatFits)
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)
    }
}
