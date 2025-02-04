//
//  FilePicker.swift
//  SimpleTemp
//
//  Created by Hossein Hajimirza on 5/18/22.
//

import SwiftUI

struct DocumentPicker: UIViewControllerRepresentable {
    @Binding var fileURL: URL?
    @Binding var fileName: String?

    func makeCoordinator() -> DocumentPickerCoordinator {
        return DocumentPickerCoordinator(fileURL: $fileURL, fileName: $fileName)
    }
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<DocumentPicker>) -> UIDocumentPickerViewController {
        let controller: UIDocumentPickerViewController
        controller = UIDocumentPickerViewController(forOpeningContentTypes: [.image, .pdf, .png], asCopy: true)
        controller.delegate = context.coordinator
        
        return controller
    }
    
    func updateUIViewController(_ uiViewController:UIDocumentPickerViewController, context: UIViewControllerRepresentableContext<DocumentPicker>) { }
}

class DocumentPickerCoordinator: NSObject, UIDocumentPickerDelegate, UINavigationControllerDelegate {
    @Binding var fileURL: URL?
    @Binding var fileName: String?

    init(fileURL: Binding<URL?>, fileName: Binding<String?>) {
        _fileURL = fileURL
        _fileName = fileName
    }
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        let fileURL = urls[0]
        let fileName = fileURL.deletingPathExtension().lastPathComponent

        self.fileURL = fileURL
        self.fileName = fileName
    }
}
