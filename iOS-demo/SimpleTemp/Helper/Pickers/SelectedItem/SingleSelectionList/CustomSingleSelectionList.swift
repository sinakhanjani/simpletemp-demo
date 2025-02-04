//
//  CustomSingleSelectionList.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/4/22.
//

import SwiftUI

struct CustomSingleSelectionList: View {
    @Binding var selectedItem: SelectedItemModel?
    var defaultSelectedItme: SelectedItemModel?
    
    @Environment(\.colorScheme) var colorScheme
    @Environment(\.dismiss) var dismiss
    
    var mock = Array(0...10).map {
        SelectedItemModel(item: "Item - \($0)")
    }
    
    var navigationTitle: String
    
    var body: some View {
        VStack {
            List(mock) { mock in
                if defaultSelectedItme?.item == mock.item {
                    Button {
                        withAnimation(.easeIn) {
                            selectedItem = mock
                            dismiss()
                        }
                    } label: {
                        HStack {
                            Text("\(mock.item) (Clinic Preferred Rate)")
                            
                            Spacer()
                        }
                    }
                    .foregroundColor(defaultSelectedItme?.item == mock.item ? (colorScheme == .light ? .sBlue : .sCyan) : .secondary.opacity(0.5))
                    .font(defaultSelectedItme?.item == mock.item ? .poppinsHeadline : .poppinsBody)
                } else {
                    Button {
                        withAnimation(.easeIn) {
                            selectedItem = mock
                            dismiss()
                        }
                    } label: {
                        HStack {
                            Text("\(mock.item)")
                            
                            Spacer()
                            
                            if selectedItem?.item == mock.item {
                               Image(systemName: "checkmark")
                            }

                        }
                        .foregroundColor(selectedItem?.item == mock.item ? .orange : .secondary.opacity(0.5))
                        .font(selectedItem?.item == mock.item ? .poppinsHeadline : .poppinsBody)
                    }
                }
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                NavigationLink(destination: FAQ(), label: {
                    Image(systemName: "questionmark.circle.fill")
                        .foregroundColor(colorScheme == .light ? .black : .white)
                })
            }
        }
        .navigationTitle(navigationTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct CustomSingleSelectionList_Previews: PreviewProvider {
    static var previews: some View {
        CustomSingleSelectionList(selectedItem: .constant(SelectedItemModel(item: "")), navigationTitle: "title")
    }
}
