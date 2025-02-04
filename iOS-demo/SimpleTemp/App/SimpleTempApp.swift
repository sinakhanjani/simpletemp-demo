//
//  SimpleTempApp.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/12/1401 AP.
//

import SwiftUI

@main
struct SimpleTempApp: App {
    init() {
        let backButtonAppearance = UIBarButtonItemAppearance()
        backButtonAppearance.normal.titleTextAttributes = [.font: UIFont(name: "Poppins-SemiBold", size: 16)!]
        
        let appearance = UINavigationBarAppearance()
        appearance.largeTitleTextAttributes = [.font: UIFont(name: "Poppins-Bold", size: 32)!]
        appearance.titleTextAttributes = [.font: UIFont(name: "Poppins-SemiBold", size: 18)!]
        appearance.backButtonAppearance = backButtonAppearance
        
        UINavigationBar.appearance().standardAppearance = appearance
        UINavigationBar.appearance().tintColor = UIColor(named:"sCyan")
        
        UITextField.appearance().tintColor = UIColor(named:"sCyan")
        UITextView.appearance().tintColor = UIColor(named:"sCyan")
        UITextView.appearance().backgroundColor = .clear
        
        UISegmentedControl.appearance().setTitleTextAttributes([.font: UIFont(name: "Poppins-Regular", size: 12)!], for: .normal)
        UISegmentedControl.appearance().setTitleTextAttributes([.font: UIFont(name: "Poppins-Regular", size: 14)!], for: .selected)
    }
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            Root()
                .environment(\.managedObjectContext,
                              persistenceController.container.viewContext)
        }
    }
}

extension UINavigationController {
    // Remove back button text
    open override func viewWillLayoutSubviews() {
        navigationBar.topItem?.backButtonDisplayMode = .minimal
    }
}
