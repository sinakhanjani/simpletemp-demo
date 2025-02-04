//
//  UploadDocumentButton.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/16/22.
//

import SwiftUI

struct UploadDocumentButton: View {
    var title: String
    var backgroundColor: Color
    public var action: (() -> Void)?
    
    var body: some View {
        Button {
            action?()
        } label: {
            HStack {
                Image(systemName: "tag.square.fill")
                    .foregroundColor(.white)
                    .imageScale(.large)
                
                Text(title)
                    .font(.poppinsCaption)
                    .fontWeight(.semibold)
                    .foregroundColor(.white)
                
                Spacer()
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 8)
        .padding(.vertical, 8)
        .background(backgroundColor)
        .cornerRadius(8)
    }
}

struct UploadDocumentButton_Previews: PreviewProvider {
    static var previews: some View {
        UploadDocumentButton(title: "DBS certificate", backgroundColor: .sCyan)
            .previewLayout(.sizeThatFits)
        
    }
}
