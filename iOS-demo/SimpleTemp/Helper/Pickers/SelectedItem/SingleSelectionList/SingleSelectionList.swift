//
//  SingleSelectionList.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/18/22.
//

import SwiftUI

struct SingleSelectionList: View {
    @Binding var selectedItem: SelectedItemModel?

    var mock: [SelectedItemModel]
    var navigationTitle: String
    
    @Environment(\.colorScheme) var colorScheme
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        VStack {
            List(mock) { mock in
                Button {
                    withAnimation(.easeIn) {
                        selectedItem = mock
                        dismiss()
                    }
                } label: {
                    HStack {
                        Text(mock.item)
                        
                        Spacer()
                        
                        if selectedItem?.item == mock.item {
                           Image(systemName: "checkmark")
                        }

                        
                    }
                    .foregroundColor(selectedItem?.item == mock.item ? (colorScheme == .light ? .sBlue : .sCyan) : .secondary.opacity(0.5))
                    .font(selectedItem?.item == mock.item ? .poppinsHeadline : .poppinsBody)
                    
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

struct SingleSelectionListView_Previews: PreviewProvider {
    static var previews: some View {
        SingleSelectionList(selectedItem: .constant(SelectedItemModel(item: "")), mock: [
            SelectedItemModel(item: "Hossein"),
            SelectedItemModel(item: "salam")
        ], navigationTitle: "title")
    }
}
