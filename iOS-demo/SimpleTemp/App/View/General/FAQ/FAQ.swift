//
//  FAQ.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/26/22.
//

import SwiftUI

struct FAQ: View {
    @EnvironmentObject private var networkModelData : NetworkModelData
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @StateObject private var faqModelData: FAQModelData = .init()
    
    @State private var showAnswer: Bool = false
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        ZStack {
            VStack(spacing: 4) {
                ScrollView(.vertical, showsIndicators: false) {
                    PullToRefresh(coordinateSpaceName: "pullToRefresh") {
                        Task {
                            try await faqModelData.faqReadRequest(networkModelData: networkModelData, accountTypeModelData: accountTypeModelData)
                        }
                    }
                    
                    LazyVStack(spacing: 0) {
                        ForEach(faqModelData.faqModel, id: \.id) { faq in
                            FAQRow(question: faq.question, answer: faq.answer)
                                .id(faq.id)
                                .onAppear {
                                    if faq.id == faqModelData.faqModel.last?.id && faqModelData.faqModel.count > ((10 * (faqModelData.page + 1)) - 1) {
                                        faqModelData.page += 1
                                        
                                        Task {
                                            try await faqModelData.faqReadRequest(networkModelData: networkModelData, accountTypeModelData: accountTypeModelData)
                                        }
                                    }
                                }
                            
                            if faq.id != faqModelData.faqModel.last?.id {
                                Divider()
                            }
                        }
                    }
                    .padding(4)
                    .background(
                        (colorScheme == .light) ?
                        Color.secondary.opacity(0.1) : Color.secondary.opacity(0.2)
                    )
                    .cornerRadius(16)
                    .padding(.top, 12)
                    .padding(.horizontal)
                    .opacity(faqModelData.faqModel.isEmpty ? 0 : 1)
                }
                .coordinateSpace(name: "pullToRefresh")
                
                NavigationLink(destination: TicketHost()) {
                    Text("Need more help?")
                        .font(.poppinsHeadline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(12)
                        .background(Color.sCyan)
                        .cornerRadius(16)
                        .padding(.horizontal)
                        .padding(.bottom)
                }
            }
        }
        .onAppear {
            Task {
                try await faqModelData.faqReadRequest(networkModelData: networkModelData, accountTypeModelData: accountTypeModelData)
            }
        }
        .navigationTitle("FAQ")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct FAQ_Previews: PreviewProvider {
    static var previews: some View {
        FAQ()
            .environmentObject(NetworkModelData())
            .environmentObject(AccountTypeModelData())
    }
}
