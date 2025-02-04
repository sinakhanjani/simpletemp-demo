//
//  VersionControl.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI
import RestfulAPI

// MARK: - Version
struct Version: Codable {
    let iOS, android: OSCompatibleVersion?
    let userType, versionescription: String?

    enum CodingKeys: String, CodingKey {
        case iOS, android, userType
        case versionescription = "description"
    }
}

// MARK: - OSCompatibleVersion
struct OSCompatibleVersion: Codable {
    let build: Int?
    let version: String?
    let appURL: String?
}


final class VersionControlModelData: ObservableObject {    
    public func versionControlRequest(networkModelData: NetworkModelData, accountType: AccountTypeModel) async throws -> [Version]? {
        let object =  try await networkModelData.handleRequestBySwiftUI(
            RestfulAPI<EMPTYMODEL,GenericModel<[Version]>>
                .init(path: "/common/version")
                .with(queries: ["userType": accountType.value])
        )
        
        return object.data
    }
}

struct VersionControl<Content>: View where Content: View {
    @EnvironmentObject private var networkModelData: NetworkModelData
    @EnvironmentObject private var accountAuthenticateModelData: AccountAuthenticateModelData
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @StateObject private var versionControlModelData: VersionControlModelData = .init()
    
    @State private var isUpdateNedded: Bool = false
    @State private var description: String = ""
    @State private var appURL: String = ""

    
    private let content: Content
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }
    
    var body: some View {
        ZStack {
            content
            if isUpdateNedded {
                IAlert(title: "Please Update Application!",
                       description: description,
                       isPresented: .constant(true)) {
                    BlueFilledButton(title: "Update") {
                        if let url = URL(string: appURL) {
                            UIApplication.shared.open(url)
                        }
                    }
                }
            }
        }
        .onAppear {
            Task {
                switch accountTypeModelData.accountType {
                case .dentalTemp(_), .clinic:
                    let version = try? await versionControlModelData.versionControlRequest(networkModelData: networkModelData, accountType: accountTypeModelData.accountType)?.first
                    if let version = version {
                        if let currentBuild = Int(Bundle.appBuild) {
                            if let serverBuild = version.iOS?.build {
                                
                                if currentBuild < serverBuild {
                                    // update application
                                    self.isUpdateNedded = true
                                    if let description = version.versionescription {
                                        self.description = description
                                    }
                                    if let appURL = version.iOS?.appURL {
                                        self.appURL = appURL
                                    }
                                }
                            }
                        }
                    }
                    break
                case .none:
                    break
                }
            }
        }
    }
}
