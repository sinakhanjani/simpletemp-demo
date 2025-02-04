//
//  AccountView.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI

struct AccountHost: View {
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @EnvironmentObject private var dentalTempModelData: DentalTempModelData
    @EnvironmentObject private var clinicModelData: ClinicModelData
    @StateObject private var clinicTabViewModelData: ClinicTabViewModelData = .init()
    @StateObject private var dentalTempTabViewModelData: DentalTempTabViewModelData = .init()
    @StateObject var preferredPriceModelData: PreferredPriceModelData = .init()
 
    var body: some View {
        if accountTypeModelData.accountType == .clinic {
            ClinicTabView()
                .environmentObject(preferredPriceModelData)
                .environmentObject(dentalTempTabViewModelData)
                .environmentObject(clinicTabViewModelData)
        } else if case .dentalTemp(_) = accountTypeModelData.accountType {
            DentalTempTabView()
                .environmentObject(preferredPriceModelData)
                .environmentObject(dentalTempTabViewModelData)
                .environmentObject(clinicTabViewModelData)
        } else {
            Text("Please Login!")
                .font(.poppinsTitle2)
        }
    }
}

struct AccountHost_Previews: PreviewProvider {
    static var previews: some View {
        AccountHost()
            .environmentObject(AccountTypeModelData())
    }
}
