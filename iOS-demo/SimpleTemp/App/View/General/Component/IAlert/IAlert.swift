//
//  IAlert.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import SwiftUI

struct IAlert<Content: View>: View {
    @Binding public var isPresented: Bool
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.colorScheme) var colorScheme
    
    private var title: String
    private var description: String
    private let content: Content
    
    internal init(title: String, description: String, isPresented: Binding<Bool>, @ViewBuilder content: () -> Content) {
        self.title = title
        self.description = description
        self._isPresented = isPresented
        self.content = content()
    }
    
    var body: some View {
        if isPresented {
            ZStack {
                VStack {

                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(.background)
                .opacity(0.8)

                VStack {
                    VStack(spacing: 16) {
                        VStack(spacing: 16) {
                            Text(title)
                                .font(.poppinsBody)
                                .fontWeight(.semibold)
                            
                            if !description.isEmpty {
                                Text(description)
                                    .font(.poppinsSubheadline)
                                    .foregroundColor(.secondary)
                            }
                        }
                        .multilineTextAlignment(.center)
                        
                        Divider()
                            .opacity(0.3)
                        
                        HStack(spacing: -16) {
                            content
                        }
                        .padding(.horizontal, -16)
                    }
                    .padding()
                }
                .background(.thickMaterial)
                .cornerRadius(8)
                .shadow(radius: 1)
                .padding(32)
            }
//            .transition(.asymmetric(insertion: .scale, removal: .move(edge: .top)))
//            .zIndex(1)
        }
    }
}

struct IAlert_Previews: PreviewProvider {
    static var previews: some View {
        IAlert(title: "Implemented by SwiftUI", description: "As per our policy, cancelling shifts are not permitted. By cancelling you are deleting your posted shift.", isPresented: .constant(true), content: {
                BlueFilledButton(title: "Yes")
                BorderedButton(title: "No")
        })
        .preferredColorScheme(.dark)
    }
}
