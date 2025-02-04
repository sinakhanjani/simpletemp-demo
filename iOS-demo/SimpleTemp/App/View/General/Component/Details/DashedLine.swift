//
//  DashedLine.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/16/22.
//

import SwiftUI

struct DashedLine: View {
    var dash: CGFloat
    
    var body: some View {
        Line()
            .stroke(style: StrokeStyle(lineWidth: 1, dash: [dash]))
            .foregroundColor(.secondary)
            .frame(height: 0.5)
            .padding(.horizontal, 8)
    }
}

struct DashedLine_Previews: PreviewProvider {
    static var previews: some View {
        DashedLine(dash: 4)
    }
}
