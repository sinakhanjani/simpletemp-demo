//
//  FontExtension.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI
// MARK: - Font
extension Font {
    enum PoppinsFamily: String {
        case thin = "Poppins-Thin"
        case light = "Poppins-Light"
        case regular = "Poppins-Regular"
        case medium = "Poppins-Medium"
        case semiBold = "Poppins-SemiBold"
        case bold = "Poppins-Bold"
        case black = "Poppins-Black"
    }

    static var poppinsLargeTitle = Font.poppins(family: .black, size: 32, relativeTo: .largeTitle)
    static var poppinsTitle = Font.poppins(family: .bold, size: 28, relativeTo: .title)
    static var poppinsTitle2 = Font.poppins(family: .semiBold, size: 22, relativeTo: .title2)
    static var poppinsTitle3 = Font.poppins(family: .medium, size: 20, relativeTo: .title3)
    static var poppinsBigHeadline = Font.poppins(family: .medium, size: 18, relativeTo: .headline)
    static var poppinsHeadline = Font.poppins(family: .medium, size: 16, relativeTo: .headline)
    static var poppinsSubheadline = Font.poppins(family: .regular, size: 14, relativeTo: .subheadline)
    static var poppinsBody = Font.poppins(family: .regular, size: 16, relativeTo: .body)
    static var poppinsCallout = Font.poppins(family: .regular, size: 15, relativeTo: .callout)
    static var poppinsFootnote = Font.poppins(family: .regular, size: 13, relativeTo: .footnote)
    static var poppinsCaption = Font.poppins(family: .regular, size: 12, relativeTo: .caption)
    static var poppinsCaption2 = Font.poppins(family: .light, size: 10, relativeTo: .caption2)
    
    /// Custom font: 'poppins' font
    static func poppins(family: PoppinsFamily, size: CGFloat, relativeTo: TextStyle) -> Font {
        custom(family.rawValue, size: size, relativeTo: relativeTo)
    }
}
