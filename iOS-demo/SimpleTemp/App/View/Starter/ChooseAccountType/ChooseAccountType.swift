//
//  ChooseAccountType.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/7/22.
//

import SwiftUI

struct ChooseAccountType: View {
    
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @State private var isActive: Bool = false
    
    var body: some View {
        NavigationView {
            ScrollView(.vertical, showsIndicators: false) {
                VStack(alignment: .center) {
                    Spacer(minLength: UIScreen.main.bounds.width / 7)
                    Text("Choose Account Type")
                        .font(.poppinsTitle2)
                    
                    NavigationLink(destination: DecideRegistration(), isActive: self.$isActive) {
                        AccountTypeRow(backgroundColor: .blue, title: "Clinic", image: "Clinic")
                    }
                    .isDetailLink(false)
                    .simultaneousGesture(TapGesture().onEnded {
                        accountTypeModelData.change(accountType: .clinic)
                    })
                    
                    NavigationLink(destination: DecideRegistration(), isActive: self.$isActive) {
                        AccountTypeRow(backgroundColor: .purple, title: "Dental Hygienist", image: "DentalHygienist")
                    }
                    .isDetailLink(false)
                    .simultaneousGesture(TapGesture().onEnded {
                        accountTypeModelData.change(accountType: .dentalTemp(.hygienist))
                    })
                    
                    NavigationLink(destination: DecideRegistration(), isActive: self.$isActive) {
                        AccountTypeRow(backgroundColor: Color.sRed, title: "Dental Nurse", image: "DentalNurse")
                    }
                    .isDetailLink(false)
                    .simultaneousGesture(TapGesture().onEnded {
                        accountTypeModelData.change(accountType: .dentalTemp(.nurse))
                    })
                }
            }
        }
        .accentColor(.sCyan)
        .navigationViewStyle(StackNavigationViewStyle())
        .environment(\.rootPresentationMode, self.$isActive)
    }
}

struct ChooseAccountType_Previews: PreviewProvider {
    static var previews: some View {
        ChooseAccountType()
            .environmentObject(AccountTypeModelData())
            .preferredColorScheme(.dark)
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
        
        ChooseAccountType()
            .environmentObject(AccountTypeModelData())
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
    }
}
