//
//  CustomHorizontalList.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/15/22.
//

import SwiftUI

struct CustomHorizontalList<Content: View, T: Identifiable>: View {
    var content: (T) -> Content
    var list: [T]
    var spacing: CGFloat
    var trailingSpace: CGFloat
    
    @Binding var index: Int
    @GestureState var offset: CGFloat = 0
    @State var currentIndex: Int = 0
    
    @Environment(\.colorScheme) var colorScheme
    
    init(spacing: CGFloat = 15, trailingSpace: CGFloat = 100, index: Binding<Int>, items: [T], @ViewBuilder content: @escaping (T) -> Content) {
        self.list = items
        self.spacing = spacing
        self.trailingSpace = trailingSpace
        self._index = index
        self.content = content
    }
    
    var body: some View {
        GeometryReader { proxy in
            let width = proxy.size.width - (trailingSpace - spacing)
            let adjustmentWidth = (trailingSpace / 2) - spacing
            
            HStack(spacing: spacing) {
                ForEach(list) {item in
                    content(item)
                        .id(item.id)
                        .frame(width: proxy.size.width - trailingSpace)
                        .background(getBackgroundColor(item: item))
                        .cornerRadius(16)
                        .offset(y: getOffset(item: item, width: width))
                        .onAppear {
                            currentIndex = 0
                        }
                        
                }
            }
            .padding(.horizontal, spacing)
            .offset(x: (CGFloat(currentIndex) * -width) + (currentIndex != 0 ? adjustmentWidth : 0) + offset)
            .gesture(
                DragGesture()
                    .updating($offset, body: { value, out, _ in
                        out = (value.translation.width / 1.5)
                    })
                    .onEnded({ value in
                        let offsetX = value.translation.width
                        let progress = -offsetX / width
                        let roundIndex = progress.rounded()
                        
                        currentIndex = max(min(currentIndex + Int(roundIndex), list.count - 1), 0)
                            // updating index
                        currentIndex = index
                    })
                    .onChanged({ value in
                        let offsetX = value.translation.width
                        let progress = -offsetX / width
                        let roundIndex = progress.rounded()
                        
                        index = max(min(currentIndex + Int(roundIndex), list.count - 1), 0)
                    })
            )
        }
        .animation(.easeInOut, value: offset == 0)
    }
    
    func getBackgroundColor(item: T) -> Color {
        if getIndex(item: item) == currentIndex {
            return (colorScheme == .light ? Color.sCyan.opacity(0.2) : Color.cyan.opacity(0.2))
        } else {
            return (colorScheme == .light ? Color.secondary.opacity(0.1) : Color.secondary.opacity(0.2))
        }
    }
    
    func getOffset(item: T, width: CGFloat) -> CGFloat {
        let progress  = ((offset < 0 ? offset : -offset) / width) * 44
        let topOffset = -progress < 44 ? progress : -(progress + 88)
        
        let previous = getIndex(item: item) - 1 == currentIndex ? (offset < 0 ? topOffset : -topOffset) : 0
        let next = getIndex(item: item) + 1 == currentIndex ? (offset < 0 ? -topOffset : topOffset) : 0
        
        let checkBetween = currentIndex >= 0 && currentIndex < list.count ? (getIndex(item: item) - 1 == currentIndex ? previous : next) : 0
        
        return getIndex(item: item) == currentIndex ? -44 - topOffset : checkBetween
    }
    
    func getIndex(item: T) -> Int {
        let index = list.firstIndex { currentItem in
            return currentItem.id == item.id
        } ?? 0
        
        return index
    }
}

