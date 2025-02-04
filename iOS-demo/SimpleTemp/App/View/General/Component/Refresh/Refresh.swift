//
//  Refresh.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 6/26/22.
//

import SwiftUI

struct Refresh: View {
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        VStack(spacing: 16) {
            HStack(alignment: .bottom, spacing: 0) {
                Image(systemName: "chevron.down.circle")
                    .imageScale(.large)
            }
            
            Text("Please pull down to refresh.")
                .font(.poppinsBody)
        }
    }
}

struct Refresh_Previews: PreviewProvider {
    static var previews: some View {
        Refresh()
    }
}
