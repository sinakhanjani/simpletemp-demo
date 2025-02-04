//
//  FAQModelData.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/26/22.
//

import SwiftUI
import RestfulAPI

final class FAQModelData: ObservableObject {
    @Published public var faqModel: [FAQModel] = .init()
    @Published public var page: Int = 0
    
    public func faqReadRequest(networkModelData: NetworkModelData, accountTypeModelData: AccountTypeModelData) async throws {
        let object = try await networkModelData.handleRequestBySwiftUI(
            RestfulAPI<EMPTYMODEL,GenericModel<[FAQModel]>>
                .init(path: "/common/faq")
                .with(method: .GET)
                .with(queries:
                        [
                            "limit" : "10",
                            "page": "\(self.page)",
                            "userType": accountTypeModelData.accountType.value
                        ]
                     )
        )
        
        DispatchQueue.main.async {
            if let data = object.data {
                if self.page == 0 {
                    self.faqModel = data
                } else {
                    for faqModel in data {
                        if !(self.faqModel.contains(where: { $0.id == faqModel.id })) {
                            self.faqModel += data
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
