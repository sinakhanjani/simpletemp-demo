//
//  PrivacyPolicy.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/7/22.
//

import SwiftUI

struct PrivacyPolicy: View {
    var body: some View {
        VStack(alignment: .center, spacing: 8) {
            Text("SimpleTemp Privacy Policy")
                .font(.poppinsTitle3)
            
            ScrollView(.vertical, showsIndicators: true) {
                ZStack(alignment: .leading) {
                    Text("""
                    Hello
                    
                    Welcome to the SimpleTemp privacy policy.
                    Last updated: December 13, 2021
                    
                    We take your privacy seriously.
                    
                    This Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your information when You use the Service and tells You about Your privacy rights and how the law protects You.
                    
                    We use Your Personal data to provide and improve the Service. By using the Service, You agree to the collection and use of information in accordance with this Privacy Policy. This Privacy Policy has been created with the help of the Privacy Policy Generator.
                    
                    Interpretation and Definitions
                    Interpretation
                    The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.  Definitions
                    For the purposes of this Privacy Policy:
                    
                    Account means a unique account created for You to access our Service or parts of our Service.
                    
                    Affiliate means an entity that controls, is controlled by or is under common control with a party, where "control" means ownership of 50% or more of the shares, equity interest or other securities entitled to vote for election of directors or other managing authority.
                    """)
                    .font(.poppinsSubheadline)
                    .foregroundColor(.secondary)
                }
            }
            .padding(.horizontal)
            .padding(.top)
        }
    }
}

struct PrivacyPolicy_Previews: PreviewProvider {
    static var previews: some View {
        PrivacyPolicy()
    }
}
