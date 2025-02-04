//
//  CustomTextEditor.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/25/22.
//

import SwiftUI

struct CustomTextEditor: View {
    @Binding var text: String
    @State var textEditorHeight : CGFloat = 0
    
    @Environment(\.colorScheme) var colorScheme
    
    public var action: (() -> Void)?
    
    var body: some View {
        HStack(alignment: .bottom, spacing: 16) {
            ZStack(alignment: .leading) {
                Text(text)
                    .foregroundColor(.clear)
                    .padding(8)
                    .background(
                        GeometryReader {
                            Color.clear.preference(key: ViewHeightKey.self,
                                                   value: $0.frame(in: .local).size.height)
                        })
                    .frame(minHeight: 44, maxHeight: 144)
                
                TextEditor(text: $text)
//                    .background(colorScheme == .light ? Color.secondary.opacity(0.1) : Color.secondary.opacity(0.2))
//                    .padding(8)
                    .frame(height: max(44,textEditorHeight))
                    .disableAutocorrection(true)
                    .menuIndicator(.visible)
            }
            .font(.poppinsBody)
            .onPreferenceChange(ViewHeightKey.self) { textEditorHeight = $0 }
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray.opacity(0.4), lineWidth: 3)
            )
            
            Circle()
                .frame(width: 44, height: 44)
                .foregroundColor(colorScheme == .light ? .sBlue : .sCyan)
                .overlay(
                    Button {
                        action?()
                    } label: {
                        Image(systemName: "location.fill")
                            .imageScale(.medium)
                            .frame(width: 32, height: 32)
                            .foregroundColor(.white)
                    }
                )
                .disabled(text == "")
                .opacity((text == "") ? 0.5 : 1)
        }
        .onPreferenceChange(ViewHeightKey.self) { textEditorHeight = $0 }
        .fixedSize(horizontal: false, vertical: true)
        .padding(.horizontal)
        .padding(.vertical,8)
        .background(.ultraThinMaterial)
    }
}

struct ViewHeightKey: PreferenceKey {
    static var defaultValue: CGFloat { 0 }
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value = value + nextValue()
    }
}

struct CustomTextEditor_Previews: PreviewProvider {
    static var previews: some View {
        CustomTextEditor(text: .constant(""))
    }
}
