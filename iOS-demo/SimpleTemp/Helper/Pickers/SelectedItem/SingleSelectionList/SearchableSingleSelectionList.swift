//
//  SearchableSingleSelectionList.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/7/22.
//

import SwiftUI

struct SearchableSingleSelectionList: View {
    @Binding var selectedItem: SelectedItemModel?
    @State private var searchText: String = ""
    
    var mock: [SelectedItemModel]
    var prompt: String
    var navigationTitle: String
    
    var results: [SelectedItemModel] {
        if searchText.isEmpty {
            return mock
        } else {
            return mock.filter { $0.item.localizedCaseInsensitiveContains(searchText)}
        }
    }

    @Environment(\.colorScheme) var colorScheme
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        VStack {
            List(results, id: \.id) { result in
                Button {
                    withAnimation(.easeIn) {
                        selectedItem = result
                        dismiss()
                    }
                } label: {
                    HStack {
                        Text(result.item)
                        
                        Spacer()
                        
                        if selectedItem?.id == result.id {
                           Image(systemName: "checkmark")
                        }

                    }
                    .id(result.id)
                    .foregroundColor(selectedItem?.id == result.id ? (colorScheme == .light ? .sBlue : .sCyan) : .secondary.opacity(0.5))
                    .font(selectedItem?.id == result.id ? .poppinsHeadline : .poppinsBody)
                }
            }
            .searchable(text: $searchText, placement: .navigationBarDrawer(displayMode: .always), prompt: prompt)
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

struct SearchableSingleSelectionList_Previews: PreviewProvider {
    static var previews: some View {
        SearchableSingleSelectionList(selectedItem: .constant(SelectedItemModel(item: "")), mock: [SelectedItemModel](), prompt: "", navigationTitle: "")
    }
}
