//
//  Navigate.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/25/1401 AP.
//

import SwiftUI

struct Navigate<Content>: View where Content: View {
    @EnvironmentObject private var clinicTabViewModelData: ClinicTabViewModelData
    @EnvironmentObject private var accountTypeModelData: AccountTypeModelData
    @EnvironmentObject private var dentalTempTabViewModelData: DentalTempTabViewModelData
    
    @Binding private var isActive: Bool
    @State private var isNavigateToFAQ: Bool = false
    
    @Environment(\.colorScheme) var colorScheme
    
    private let content: Content
    
    init(isActive: Binding<Bool> = .constant(false), @ViewBuilder content: () -> Content) {
        self._isActive = isActive
        self.content = content()
    }
    
    var body: some View {
        NavigationView {
            ZStack {
                content
            }
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink(destination: FAQ(), isActive: $isNavigateToFAQ, label: {
                        Image(systemName: "questionmark.circle.fill")
                            .foregroundColor(colorScheme == .light ? .black : .white)
                    })
                }
            }
            .onReceive(clinicTabViewModelData.$selectedTab) { selection in
                isNavigateToFAQ = false
                isActive = false
            }
            .onReceive(dentalTempTabViewModelData.$selectedTab) { selection in
                isNavigateToFAQ = false
                isActive = false
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .environment(\.rootPresentationMode, self.$isActive)
    }
}
