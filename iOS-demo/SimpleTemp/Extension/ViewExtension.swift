//
//  ViewExtension.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI
// MARK: - View
extension View {
    func border(width: CGFloat, edges: [Edge], color: Color) -> some View {
        overlay(EdgeBorder(width: width, edges: edges)
            .foregroundColor(color))
    }
}

#if canImport(UIKit)
extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
#endif
