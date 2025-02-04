//
//  TimePicker.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/30/22.
//

import SwiftUI

struct TimePicker: View {
    @Binding var arrivalTimeCurrentDate: Date
    @Binding var endTimeCurrentDate: Date
    
    @Environment(\.colorScheme) var colorScheme

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            VStack(alignment: .leading) {
                Text("Shift Arrival Time")
                    .font(.poppinsCallout)
                    .foregroundColor(.secondary)
                
                HStack {
                    Spacer()
                    
                    DatePicker("", selection: $arrivalTimeCurrentDate, displayedComponents: .hourAndMinute)
//                        .environment(\.locale, .init(identifier: "en_UK"))
                        .labelsHidden()
                        .transformEffect(.init(scaleX: 1.1, y: 1.1))
                        .offset(x: -16 , y: -8)
                }
            }
            
            Divider()
            
            Text("Shift End Time")
                .font(.poppinsCallout)
                .foregroundColor(.secondary)
            
            HStack {
                Spacer()
                
                DatePicker("", selection: $endTimeCurrentDate, displayedComponents: .hourAndMinute)
//                    .environment(\.locale, .init(identifier: "en_UK"))
                    .labelsHidden()
                    .transformEffect(.init(scaleX: 1.1, y: 1.1))
                    .offset(x: -16 , y: -8)
            }
        }
    }
}

struct TimePicker_Previews: PreviewProvider {
    static var previews: some View {
        TimePicker(arrivalTimeCurrentDate: .constant(Date()), endTimeCurrentDate: .constant(Date.now))
    }
}
