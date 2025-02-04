//
//  NetworkView.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//  09125933044

import SwiftUI
import RestfulAPI
import XCTest

private let reachability = Reachability()

struct GenericModel<T: Codable>: Codable {
    init() {
        self.data = nil
        self.message = "Success"
        self.success = true
        self.code = 200
    }
    
    init(success: Bool, message: String, code: Int) {
        self.success = success
        self.message = message
        self.code = code
        self.data = nil
    }
    
    let data: T?
    let message: String
    let success: Bool
    let code: Int
}

final class NetworkModelData: ObservableObject {
    @Published public var genericModel: GenericModel<EMPTYMODEL> = .init()
    @Published public var isLoading: Bool = false
    @Published public var isIAlertPresented: Bool = false
    @Published public var isAccountDisabled: Bool = false
    @Published public var badConnection: Bool = false
    
    public var connetctionStatus: ReachabilityStatus {
        let status = reachability.connectionStatus()
        
        return status
    }
    
    @MainActor
    public func handleRequestBySwiftUI<S,R>(_ network: RestfulAPI<S,GenericModel<R>>, animated: Bool = false) async throws -> GenericModel<R> {
        self.isLoading = false
        
        if animated {
            withAnimation(.linear) {
                self.isLoading = true
            }
        }
        
        let result = try await network.sendURLSessionRequest()
        
        withAnimation(.linear) {
            self.isLoading = false
        }
        
        switch result {
        case .success(let object):
            if object.object.success {
                return object.0
            } else if object.object.code == 401 {
                DispatchQueue.main.async {
                    self.isAccountDisabled = true
                }
                throw ApiError.badData
            } else {
                DispatchQueue.main.async {
                    self.isIAlertPresented = true
                    self.genericModel = .init(success: object.0.success,
                                              message: object.0.message,
                                              code: object.0.code)
                }
                
                throw ApiError.badData
            }
        case .failure(let e):
            throw e
        }
    }
    
    public func monitorReachabilityChanged() {
        reachability.monitorReachabilityChanges()
    }
}

struct Network<Content>: View where Content: View {
    @StateObject public var networkModelData: NetworkModelData = NetworkModelData()
    
    private let content: Content
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }
    
    var body: some View {
        let publisher = NotificationCenter.default.publisher(for: .reachabilityStatusChangedNotification)
        
        ZStack {
            content
                .environmentObject(networkModelData)
                .overlay {
                    if networkModelData.isLoading {
                        Loading()
                            .transition(.scale)
                    }
                }
            
            if networkModelData.badConnection {
                BadConnection()
            }
        }
        .onAppear {
            networkModelData.monitorReachabilityChanged()
        }
        .onReceive(publisher) { (output) in
            if let status = output.userInfo?["Status"] as? ReachabilityStatus {
                switch status {
                case .offline:
                    withAnimation(.linear) {
                        networkModelData.badConnection = true
                    }
                case .online:
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                        networkModelData.badConnection = false
                        networkModelData.isLoading = false
                    }
                case .unknown:
                    break
                }
            }
        }
    }
}

