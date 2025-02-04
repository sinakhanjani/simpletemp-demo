//
//  Picker.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/17/22.
//
import SwiftUI



struct MultiSelectList: View {
    @State var sourceItems: [SelectedItemModel]
    @Binding var selectedItems: [SelectedItemModel]
    
    var navigationTitle: String
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        Form {
            List {
                ForEach(sourceItems) { sourceItem in
                    Button {
                        withAnimation {
                            if self.selectedItems.filter({ $0.item == sourceItem.item }).count > 0 {
                                self.selectedItems.removeAll(where: { $0.item == sourceItem.item })
                            } else {
                                self.selectedItems.append(sourceItem)
                            }
                        }
                    } label: {
                        HStack {
                            Text("\(sourceItem.item)")
                                .font((self.selectedItems.filter({ $0.item == sourceItem.item }).count > 0) ? .poppinsHeadline : .poppinsBody)
                            
                            Spacer()
                            
                            Image(systemName: "checkmark")
                                .opacity((self.selectedItems.filter({ $0.item == sourceItem.item }).count > 0) ? 1.0 : 0.0)

                        }
                    }
                    .foregroundColor((self.selectedItems.filter({ $0.item == sourceItem.item }).count > 0) ? (colorScheme == .light ? .sBlue : .sCyan) : .secondary)
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
        .listStyle(GroupedListStyle())
    }
}

struct MultiSelectList_Previews: PreviewProvider {
    @State static var selected: [SelectedItemModel] = [SelectedItemModel(item: "")]
    
    static var previews: some View {
        MultiSelectList(sourceItems: [
            SelectedItemModel(item: "Double"),
            SelectedItemModel(item: "String"),
            SelectedItemModel(item: "Int")
        ], selectedItems: $selected, navigationTitle: "title")
    }
}
