//
//  RootView.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI
import RestfulAPI

struct Root: View {
    var body: some View {
        Network {
            Authenticate {
                VersionControl {
                    Splash()
                }
            }
        }
    }
}

struct Root_Previews: PreviewProvider {
    static var previews: some View {
        Root()
            .environmentObject(AccountAuthenticateModelData(auth: .auth1))
    }
}
