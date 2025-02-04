//
//  Starter.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import SwiftUI
import RestfulAPI

struct Splash: View {
    @EnvironmentObject private var networkModelData: NetworkModelData
    @StateObject private var dentalTempModelData: DentalTempModelData = .init()
    @StateObject private var clinicModelData: ClinicModelData = .init()
    
    @State private var isEnded: Bool = false
    
    var body: some View {
        if isEnded {
            Auth()
                .environmentObject(clinicModelData)
                .environmentObject(dentalTempModelData)
        } else {
            SplashScreen()
                .onAppear {
                     DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                        isEnded = true
                    }
                }
        }
    }
}

struct Splash_Previews: PreviewProvider {
    static var previews: some View {
        Splash()
            .environmentObject(NetworkModelData())
            .environmentObject(AccountAuthenticateModelData(auth: .auth1))
    }
}
