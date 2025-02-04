//
//  CompleteProfileStepsRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/16/22.
//

import SwiftUI



struct CompleteProfileStepsRow: View {
    var step: Int
    
    @Environment(\.colorScheme) var colorScheme
    
    var progressBarWidth: Double {
        switch step {
        case 2:
            return ((UIScreen.main.bounds.width - 88) / 2)
        case 3:
            return (UIScreen.main.bounds.width - 88)
        default:
            return 0.0
        }
    }
    
    var titles: [String]
    let width = UIScreen.main.bounds.width
    
    var body: some View {
        VStack(alignment: .center) {
            ZStack(alignment: .center) {
                ZStack(alignment: .leading) {
                    
                    RoundedRectangle(cornerRadius: 16, style: .continuous)
                        .frame(height: 4)
                        .foregroundColor(.sWhite)
                    
                    RoundedRectangle(cornerRadius: 16, style: .continuous)
                        .frame(width: progressBarWidth, height: 6)
                        .background(
                            (colorScheme == .light ? Color.sBlue : Color.sCyan)
                                .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                        )
                        .foregroundColor(.clear)
                }
                
                HStack {
                    Circle()
                        .fill(colorScheme == .light ? Color.sBlue : Color.sCyan)
                        .frame(width: width / 9, height: width / 9)
                        .overlay(
                            Text("1")
                                .font(.poppinsBody)
                                .fontWeight(.semibold)
                                .foregroundColor(.white)
                        )
                    
                    Spacer()
                    
                    Circle()
                        .fill(
                            ((step == 2 || step == 3) ?
                             (colorScheme == .light ? Color.sBlue : Color.sCyan) : Color.sWhite)
                        )
                        .frame(width: width / 9, height: width / 9)
                        .overlay(
                            Text("2")
                                .font(.poppinsBody)
                                .fontWeight(.semibold)
                                .foregroundColor(
                                    ((step == 2 || step == 3) ? .white : .gray.opacity(0.7))
                                )
                        )
                    
                    Spacer()
                    
                    Circle()
                        .fill(
                            (step == 3 ?
                             (colorScheme == .light ? Color.sBlue : Color.sCyan) : Color.sWhite)
                        )
                        .frame(width: width / 9, height: width / 9)
                        .overlay(
                            Text("3")
                                .font(.poppinsBody)
                                .fontWeight(.semibold)
                                .foregroundColor(
                                    (step == 3 ? .white : .gray.opacity(0.7))
                                )
                        )
                }
            }
            .padding(.horizontal, 44)
            
            HStack {
                Text(titles[0])
                    .fontWeight(.medium)
                    .foregroundColor((colorScheme == .light ? Color.sBlue : Color.sCyan))
                
                Spacer()
                
                Text(titles[1])
                    .fontWeight(.medium)
                    .foregroundColor(
                        ((step == 2 || step == 3) ?
                         (colorScheme == .light ? Color.sBlue : Color.sCyan) : Color.secondary)
                    )
                
                Spacer()
                
                Text(titles[2])
                    .fontWeight(.medium)
                    .foregroundColor(
                        (step == 3 ?
                         (colorScheme == .light ? Color.sBlue : Color.sCyan) : Color.secondary)
                    )
            }
            .padding(.horizontal, 24)
            .font(.poppinsSubheadline)
        }
        .padding(.vertical)
    }
}

struct CompleteProfileStepsRow_Previews: PreviewProvider {
    static var previews: some View {
        CompleteProfileStepsRow(step: 1, titles: ["Account Info", "Personal Info", "Banking Info"])
            .previewLayout(.sizeThatFits)
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
        
        CompleteProfileStepsRow(step: 1, titles: ["Clinic Info", "Clinic Details", "Payment Method"])
            .previewLayout(.sizeThatFits)
            .preferredColorScheme(.dark)
    }
}
