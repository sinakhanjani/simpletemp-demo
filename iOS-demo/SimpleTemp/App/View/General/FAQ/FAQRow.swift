//
//  FAQRow.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/26/22.
//

import SwiftUI

struct FAQRow: View {
    @State private var showAnswer: Bool = false
    
    var question: String = ""
    var answer: String = ""
    
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Button {
                withAnimation {
                    showAnswer.toggle()
                }
            } label: {
                HStack(spacing: 16) {
                    Text(question)
                        .font(.poppinsCallout)
                        .fontWeight(.medium)
                        .foregroundColor((colorScheme == .light) ? Color.black : Color.white)
                        .multilineTextAlignment(.leading)
                    
                    Spacer()
                    
                    ChevronRight()
                        .imageScale(.small)
                        .rotationEffect(Angle(degrees: showAnswer ? 90 : 0))
                        .foregroundColor(.secondary)
                }
            }
            
            if showAnswer {
                Text(answer)
                    .font(.poppinsCaption)
                    .foregroundColor(.secondary)
            }
        }
        .padding(.horizontal)
        .padding(.vertical, 8)
    }
}

struct FAQRow_Previews: PreviewProvider {
    static var previews: some View {
        FAQRow(question:"question", answer: "answerdjsaldasldjsadljsdljasdlsajdlsajdasldjsaldjsldjasldjasl")
//            .preferredColorScheme(.dark)
    }
}
