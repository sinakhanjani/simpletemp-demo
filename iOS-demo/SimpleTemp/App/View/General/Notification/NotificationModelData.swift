//
//  NotificationModelData.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/20/22.
//

import SwiftUI
import RestfulAPI

final class NotificationModelData: ObservableObject {
    @Published public var notificationsModel: [NotificationModel] = .init()
    @Published public var page: Int = 0
    
    public func dentalTempNotifRequest(networkModelData: NetworkModelData) async throws {
        let object = try await networkModelData.handleRequestBySwiftUI(
            RestfulAPI<EMPTYMODEL,GenericModel<[NotificationModel]>>
                .init(path: "/dentaltemp/notification")
                .with(method: .GET)
                .with(auth: .auth1)
                .with(queries:
                        [
                            "limit" : "10",
                            "page": "\(self.page)"
                        ]
                     )
        )
        
        DispatchQueue.main.async {
            if let data = object.data {
                if self.page == 0 {
                    self.notificationsModel = data
                } else {
                    for notif in data {
                        if !(self.notificationsModel.contains(where: { $0.id == notif.id })) {
                            self.notificationsModel += data
                        }
                    }
                }
                
                if self.page != 0 && data.count == 0 {
                    self.page -= 1
                }
            }
        }
    }
    
    public func clinicNotifRequest(networkModelData: NetworkModelData) async throws {
        let object = try await networkModelData.handleRequestBySwiftUI(
            RestfulAPI<EMPTYMODEL,GenericModel<[NotificationModel]>>
                .init(path: "/clinic/notification")
                .with(method: .GET)
                .with(auth: .auth2)
                .with(queries:
                        [
                            "limit" : "10",
                            "page": "\(self.page)"
                        ]
                     )
        )
        
        DispatchQueue.main.async {
            if let data = object.data {
                if self.page == 0 {
                    self.notificationsModel = data
                } else {
                    for notif in data {
                        if !(self.notificationsModel.contains(where: { $0.id == notif.id })) {
                            self.notificationsModel += data
                        }
                    }
                }
                
                if self.page != 0 && data.count == 0 {
                    self.page -= 1
                }
            }
        }
    }
}
