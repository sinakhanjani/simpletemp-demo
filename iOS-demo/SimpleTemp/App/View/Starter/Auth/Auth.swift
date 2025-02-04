//
//  InterfaceView.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI

struct Auth: View {
    @EnvironmentObject private var networkModelData: NetworkModelData
    @EnvironmentObject private var accountAuthenticateModelData: AccountAuthenticateModelData
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @EnvironmentObject private var dentalTempModelData: DentalTempModelData
    @EnvironmentObject private var clinicModelData: ClinicModelData
    
    @State private var isLoggedOut: Bool = false
    
    var body: some View {
        ZStack {
            // Token not valid
            if accountAuthenticateModelData.isLogin {
                // Token validation
                if networkModelData.isAccountDisabled {
                    // Authentication Pipeline
                    ChooseAccountType()
                } else {
                    // AccountHost Pipeline
                    AccountHost()
                }
            } else {
                // Authentication Pipeline
                ChooseAccountType()
            }
            // All IAlert
            if networkModelData.isIAlertPresented {
                // Suspend code: 403
                if networkModelData.genericModel.code == 403 {
                    IAlert(title: "Account Suspended",
                           description: networkModelData.genericModel.message,
                           isPresented: $networkModelData.isIAlertPresented) {}
                } else if (networkModelData.genericModel.code == 641 || networkModelData.genericModel.code == 644) {
                    IAlert(title: "Oops!",
                           description: networkModelData.genericModel.message,
                           isPresented: $networkModelData.isIAlertPresented) {
                        BlueFilledButton(title: "Log Out") {
                            if accountTypeModelData.accountType == .clinic {
                                Task {
                                    isLoggedOut = try await clinicModelData.clinicLogOutRequest(networkModelData: networkModelData, accountAuthenticateModelData: accountAuthenticateModelData)
                                    
                                    if isLoggedOut {
                                        networkModelData.isIAlertPresented = false
                                    }
                                }
                            } else {
                                Task {
                                    isLoggedOut = try await dentalTempModelData.dentalTempLogOutRequest(networkModelData: networkModelData, accountAuthenticateModelData: accountAuthenticateModelData)
                                    
                                    if isLoggedOut {
                                        networkModelData.isIAlertPresented = false
                                    }
                                }
                            }
                        }
                    }
                } else {
                    IAlert(title: "Oops!",
                           description: networkModelData.genericModel.message,
                           isPresented: $networkModelData.isIAlertPresented) {
                        BlueFilledButton(title: "Ok") {
                            networkModelData.isIAlertPresented = false
                        }
                    }
                }
            }
        }
    }
}
