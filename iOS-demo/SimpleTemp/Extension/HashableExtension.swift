//
//  HashableExtension.swift
//  SimpleTemp
//
//  Created by Sina khanjani on 2/14/1401 AP.
//

import UIKit
import SwiftUI

// MARK: String Extentions
extension String {
    var isValidPhone: Bool {
        let phoneRegex = "^[0-9+]{0,1}+[0-9]{5,16}$"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", phoneRegex)
        
        return phoneTest.evaluate(with: self)
    }
    /// IS Email
    var isValidEmail: Bool { NSPredicate(format: "SELF MATCHES %@", "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}").evaluate(with: self) }
    
    /*
     /// No special characters (e.g. @,#,$,%,&,*,(,),^,<,>,!,±)
     Pattern details:
     ^ - start of the string (can be replaced with \A to ensure start of string only matches)
     \w{7,18} - 7 to 18 word characters (i.e. any Unicode letters, digits or underscores, if you only allow ASCII letters and digits, use [a-zA-Z0-9] or [a-zA-Z0-9_] instead)
     $ - end of string (for validation, I'd rather use \z instead to ensure end of string only matches).
     Swift code
     
     Note that if you use it with NSPredicate and MATCHES, you do not need the start/end of string anchors, as the match will be anchored by default:
     */
    var isValidUserID: Bool { range(of: "\\A\\w{7,18}\\z", options: .regularExpression) != nil }
    
    /// In 99% of the cases when I trim String in Swift, I want to remove spaces and other similar symbols
    var trimmed: String {trimmingCharacters(in: .whitespacesAndNewlines) }
    
    /// In 99% of the cases when I trim String in Swift, I want to remove spaces and other similar symbols
    mutating func trim() {
        self = self.trimmed
    }
    
    /// iOS and macOS use the URL type to handle links. It’s more flexible, it allows to get components, and it handles different types of URLs. At the same time, we usually enter it or get it from API String.
    var asURL: URL {
        return URL(string: self)!
    }
    
    /// Like the previous extension, this one checks the content of String. It returns true if the string is not empty and contains only alphanumeric characters. An inverted version of this extension can be useful to confirm that passwords have non-alphanumeric characters.
    var isAlphanumeric: Bool {
        !isEmpty && range(of: "[^a-zA-Z0-9]", options: .regularExpression) == nil
    }
    
    /// Getting the Date from String and formatting the Date to display it or send to API are common tasks. The standard way to convert takes three lines of code. Let’s see how to make it shorter:
    func toDate(format: String) -> Date? {
        let df = DateFormatter()
        df.dateFormat = format
        return df.date(from: self)
    }
    /// JSON is a popular format to exchange or store structured data. Most APIs prefer to use JSON. JSON is a JavaScript structure. Swift has exactly the same data type — dictionary.
    /// let json = "{\"hello\": \"world\"}"
    /// let dictFromJson = json.asDict
    var asDict: [String: Any]? {
        guard let data = self.data(using: .utf8) else { return nil }
        return try? JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any]
    }
    /// This extension is similar to a previous one, but it converts the JSON array into a Swift array:
    /// let json2 = "[1, 2, 3]"
    /// let arrFromJson2 = json2.asArray
    var asArray: [Any]? {
        guard let data = self.data(using: .utf8) else { return nil }
        return try? JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [Any]
    }
    /// Only allows you to initialize a String that obeys some common best practices for choosing a password
    init?(passwordSafeString: String) {
        guard passwordSafeString.rangeOfCharacter(from: .uppercaseLetters) != nil &&
                passwordSafeString.rangeOfCharacter(from: .lowercaseLetters) != nil &&
                passwordSafeString.rangeOfCharacter(from: .punctuationCharacters) != nil &&
                passwordSafeString.rangeOfCharacter(from: .decimalDigits) != nil  else {
                    return nil
                }
        
        self = passwordSafeString
    }
    func toNonQuotes() -> String {
        let userInput: String = self
        return userInput.folding(options: .diacriticInsensitive, locale: .current)
    }
}
// MARK: StringProtocol Extentions
extension StringProtocol {
    var firstUppercased: String { return prefix(1).uppercased() + dropFirst() }
    var firstCapitalized: String { return prefix(1).capitalized + dropFirst() }
}
// MARK: Int Extentions
extension Int {
    /// you can convert it with Double(a) , where a is an
    ///integer variable. But if a is optional, you can’t do it. Usage:
    ///Let’s add extensions to Int and Double :
    func toDouble() -> Double {
        Double(self)
    }
    /**
     One of the most useful features of Java is toString() method. It’s a method of absolutely all classes and types. Swift allows to do something similar using string interpolation: "\(someVar)" . But there’s one difference — your variable is optional. Swift will add the word optional to the output. Java will just crash, but Kotlin will handle optionals beautifully: someVar?.toString() will return an optional
     String, which is null ( nil ) if someVar is null ( nil ) or String containing value of var otherwise.
     */
    func toString() -> String {
        "\(self)"
    }
}
// MARK: Double Extentions
extension Double {
    /// As in the previous example, converting Double to String can be very useful. But in this case we’ll limit the output with two
    /// fractional digits. I can’t say this extension will be useful for all cases, but for most uses it will work well:
    func toString() -> String {
        String(format: "%.02f", self)
    }
    /// you can convert it with Double(a) , where a is an
    /// integer variable. But if a is optional, you can’t do it. Usage:
    /// Let’s add extensions to Int and Double :
    func toInt() -> Int {
        Int(self)
    }
}
extension String {
    func toInt() -> Int? {
        Int(self)
    }
    func toDouble() -> Double? {
        Double(self)
    }
    func toIntUnpaidBreakTime() -> Int? {
        self == "unpaid" ? 0 : Int(self)
    }
}
extension String {
    private enum RegularExpressions: String {
        case phone = "^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)\\s*$"
    }
    private func isValid(regex: RegularExpressions) -> Bool {
        return isValid(regex: regex.rawValue)
    }
    private func isValid(regex: String) -> Bool {
        let matches = range(of: regex, options: .regularExpression)
        return matches != nil
    }
        private func onlyDigits() -> String {
        let filtredUnicodeScalars = unicodeScalars.filter{CharacterSet.decimalDigits.contains($0)}
        return String(String.UnicodeScalarView(filtredUnicodeScalars))
    }
    
    public func makeACall() {
        if isValid(regex: .phone) {
            if let url = URL(string: "tel://\(self)"), UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url)
            }
        }
    }
}
extension NSMutableAttributedString {
    public func setAsLink(textToFind:String, linkURL:String) -> Bool {
        let foundRange = self.mutableString.range(of: textToFind)
        if foundRange.location != NSNotFound {
            self.addAttribute(.link, value: linkURL, range: foundRange)
            
            return true
        }
        
        return false
    }
}
extension Sequence where Element: Hashable {
    func uniqued() -> [Element] {
        var set = Set<Element>()
        return filter { set.insert($0).inserted }
    }
}
extension Array where Element == String {
    func includes() -> String {
        joined(separator: ",")
    }
    
    func completedAllFields() -> Bool {
        
        return self.contains("")
    }
}

extension UIApplication {
    func endEditing() {
        sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

extension UIImage {
    func getRotationAngle() -> Angle {
        switch imageOrientation {
        case .left:
            return Angle(degrees: 270)
        case .leftMirrored:
            return Angle(degrees: 270)
         case .right:
            return Angle(degrees: 90)
        case .rightMirrored:
            return Angle(degrees: 90)
        case .down:
            return Angle(degrees: 180)
        case .downMirrored:
            return Angle(degrees: 180)
        default:
            return Angle(degrees: 0)
        }
    }
}

extension UIImage {
    func updateImageOrientionUpSide() -> UIImage? {
        if self.imageOrientation == .up {
            return self
        }

        UIGraphicsBeginImageContextWithOptions(self.size, false, self.scale)
        self.draw(in: CGRect(x: 0, y: 0, width: self.size.width, height: self.size.height))
        if let normalizedImage:UIImage = UIGraphicsGetImageFromCurrentImageContext() {
            UIGraphicsEndImageContext()
            return normalizedImage
        }
        UIGraphicsEndImageContext()
        return nil
    }
}

extension Date {
    var nowUK: Date {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        
        return Date.now
    }
    var getYearAndMonthAndDay: String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        return dateFormatter.string(from: self)
    }
    
    var getTicketCustomDate: String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "yyyy, MM, dd"
        
        return dateFormatter.string(from: self)
    }
    
    func getAllDates() -> [Date] {
        let calendar = Calendar.current
        let startDate = calendar.date(from: Calendar.current.dateComponents([.year,.month], from: self))!
        let range = calendar.range(of: .day, in: .month, for: startDate)!
        
        return range.compactMap { day -> Date in
            return calendar.date(byAdding: .day, value: day - 1, to: startDate)!
        }
    }
    
    public func get24HoursTime() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "HH:mm"
        
        return dateFormatter.string(from: self)
    }
    
    public func get12HoursTime() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "hh:mm a"
        
        return dateFormatter.string(from: self)
    }
    
    public func getMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "MM"
        
        return dateFormatter.string(from: self)
    }
    
    public func getDay() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "dd"
        
        return dateFormatter.string(from: self)
    }
    
    public func getYearAndMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "yyyy-MM"
        
        return dateFormatter.string(from: self)
    }
    
    public func getCustomDate() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "EEEE, MMM d, yyyy"
        
        return dateFormatter.string(from: self)
    }
    
    public func getCustomShortDate() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "MMM d, yyyy"
        
        return dateFormatter.string(from: self)
    }
    
    public func getNextMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "yyyy-MM"
        
        let nextMonth = Calendar.current.date(byAdding: .month, value: 1, to: self)!
        
        return dateFormatter.string(from: nextMonth)
    }
    
    public func getDateOfNextMonth() -> Date {
        let nextMonth = Calendar.current.date(byAdding: .month, value: 1, to: self)!
        let nextMonth_plus = Calendar.current.date(byAdding: .day, value: 1, to: nextMonth)!
        
        return nextMonth_plus
    }
    
    public func getDayOfWeek() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "E"
        
        return dateFormatter.string(from: self)
    }
    
    public func getDayAndMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "MMM d"
        
        return dateFormatter.string(from: self)
    }
    
    public func getdMonthAndYear() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "MMM yyyy"
        
        return dateFormatter.string(from: self)
    }
}


extension String {
    func to(date with: String) -> String {
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ" //2021-04-29T20:37:07.830Z
            guard let date = dateFormatter.date(from: self) else { return "" }
            dateFormatter.dateFormat = with
            
            return dateFormatter.string(from: date)
        }
}


extension String {
    var convertToDate: Date {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd" //2021-04-29
        guard let date = dateFormatter.date(from: self) else { return Date() }
        
        return date
    }

    var convertToColor: Color {
        switch self {
        case "red":
            return .red
        case "blue":
            return .cyan
        case "orange":
            return .orange
        case "green":
            return .green
        case "gray":
            return .gray
        default:
            return .clear
        }
    }
    
    var convertToTempType: String {
        switch self {
        case "Dental Nurse":
            return "nurse"
        case "Dental Hygienist":
            return "hygienist"
        default:
            return ""
        }
    }
    
    var convertToDentalTempType: String {
        switch self {
        case "nurse":
            return "Dental Nurse"
        case "hygienist":
            return "Dental Hygienist"
        default:
            return ""
        }
    }
    
    var convertToMinutes: String {
        guard let hours = Double(self) else { return "unpaid" }

        return "\(Int(hours * 60))"
    }
}


extension Animation {
    static func ripple(index: Int) -> Animation {
        Animation.spring(dampingFraction: 0.5)
            .speed(2)
            .delay(0.03 * Double(index))
    }
}


extension Int {
    var toSelectedItem: SelectedItemModel? {
        return SelectedItemModel(item: String(self))
    }
    
    var toCustomSelectedItem: SelectedItemModel? {
        return SelectedItemModel(item: String("£\(self) /hr"))
    }
}

extension String {
    var digits: String {
        return components(separatedBy: CharacterSet.decimalDigits.inverted).joined()
    }
    
    func localized(_ comment: String = "", bundle: Bundle = .main) -> String {
        NSLocalizedString(self, bundle: .main, comment: comment)
    }
}

extension String {
    var toDate: Date {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        guard let utcDate = dateFormatter.date(from: self) else { return Date() }

        let xdateFormmater = DateFormatter()
        xdateFormmater.timeZone = TimeZone(abbreviation: "UTC")
        xdateFormmater.dateFormat = "yyyy-MM-dd-HH:mm"
        let x = xdateFormmater.string(from: utcDate)
        xdateFormmater.timeZone = .current
                
        guard let date = xdateFormmater.date(from: x) else { return Date() }
        return date
    }
    
    var toUTCDate: Date {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        guard let utcDate = dateFormatter.date(from: self) else { return Date() }

        return utcDate
    }
    
    var to24HoursDate: Date {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm"
        guard let utcDate = dateFormatter.date(from: self) else { return Date() }

        let xdateFormmater = DateFormatter()
        xdateFormmater.timeZone = TimeZone(abbreviation: "UTC")
        xdateFormmater.dateFormat = "HH:mm"
        let x = xdateFormmater.string(from: utcDate)
        xdateFormmater.timeZone = .current
                
        guard let date = xdateFormmater.date(from: x) else { return Date() }
        return date
    }
}

extension Double {
    var toCurrency: String {
        let numberFormatter = NumberFormatter()
        numberFormatter.numberStyle = .currency
        numberFormatter.currencySymbol = ""
        
        guard let currency = numberFormatter.string(from: NSNumber(value: self)) else { return "" }
        
        return currency
    }
}


extension DateComponents {
    var getTimeRange: String {
        let minutes = self.minute!
        let hours = self.hour!
        
        var onlyHours: String {
            if minutes == 0 {
                if hours < 2 {
                    return "\(hours) hour"
                } else {
                    return "\(hours) hours"
                }
            }
            
            return ""
        }
        
        var onlyMinutes: String {
            if hours == 0 {
                if minutes < 2 {
                    return "\(minutes) minute"
                } else {
                    return "\(minutes) minutes"
                }
            }
            
            return ""
        }

        if (hours == 0 && minutes == 0) || (hours < 0 || minutes < 0) {
            return "00:00"
        }
        
        if hours == 0 {
            return onlyMinutes
        }
        
        if minutes == 0 {
            return onlyHours
        }
        
        if hours < 10 && minutes < 10 {
            return "0\(hours):0\(minutes)"
        } else if hours < 10 {
            return "0\(hours):\(minutes)"
        } else {
            return "\(hours):0\(minutes)"
        }
    }
}

extension Date: Strideable {
    public func distance(to other: Date) -> TimeInterval {
        return other.timeIntervalSinceReferenceDate - self.timeIntervalSinceReferenceDate
    }

    public func advanced(by n: TimeInterval) -> Date {
        return self + n
    }
    
    public func timeRange(to: Date) -> String {
        return Calendar.current.dateComponents([.hour, .minute], from: self, to: to).getTimeRange
    }
    
    public func getBillabeTime(to: Date, unpaidBreakTime: Int) -> String {
        let totalTime = Calendar.current.dateComponents([.hour, .minute], from: self, to: to)
        
        if totalTime.getTimeRange != "00:00" {
            let total = Calendar.current.date(from: totalTime)!
            
            let date = Calendar.current.date(byAdding: .minute, value: -unpaidBreakTime , to: total)!
            
            return date.get24HoursTime()
        }
        
        return "00:00"
    }
    
    public func getCost(to: Date, hourlyRate: Int, unpaidBreakTime: Int) -> String {
        let diffComponents = Calendar.current.dateComponents([.minute], from: self, to: to)
        let minutes = diffComponents.minute!
        let hours = Double(round(Double(minutes - unpaidBreakTime) / 60 * 10000) / 10000)
        
        let cost = hours * hourlyRate.toDouble()
        
        if cost < 0 {
            return "£0.00"
        }
        
        return "£\(cost.toCurrency)"
    }
}


// Specify the decimal place to round to using an enum
public enum RoundingPrecision {
    case ones
    case tenths
    case hundredths
}

// Round to the specific decimal place
public func preciseRound(
    _ value: Double,
    precision: RoundingPrecision = .ones) -> Double
{
    switch precision {
    case .ones:
        return round(value)
    case .tenths:
        return round(value * 10) / 10.0
    case .hundredths:
        return round(value * 100) / 100.0
    }
}

extension Double {
    var toRoundedStr: String {
        return String(format: "%.1f", self)
    }
    
    var toLimitedDecimalNumber: NSDecimalNumber {
        let num = NSDecimalNumber.init(string: self.toString())
        let behaviour = NSDecimalNumberHandler(roundingMode:.down, scale: 1, raiseOnExactness: false,  raiseOnOverflow: false, raiseOnUnderflow: false, raiseOnDivideByZero: false)
        let numRounded = num.rounding(accordingToBehavior: behaviour)

        return numRounded
    }
}

//
extension String {
    public func loadImage() -> UIImage {
        do {
            guard let url = URL(string: self) else { return UIImage(named: "emptyProfilePicture")! }
            
            let data: Data = try Data(contentsOf: url)
            
            return UIImage(data: data) ?? UIImage(named: "emptyProfilePicture")!
        }
        
        catch {
            print("error")
        }
        
        return UIImage(named: "emptyProfilePicture")!
    }
}

extension String {
    var toUKPhoneNumberFormat: String {
        var pureNumber = self.replacingOccurrences( of: "[^0-9]", with: "", options: .regularExpression)
//        let start = pureNumber.index(pureNumber.startIndex, offsetBy: 0)
//        let end = pureNumber.index(pureNumber.startIndex, offsetBy: 11)
//        let range = start...end
        
        for index in 0 ..< "## #### ####".count {
            guard index < pureNumber.count else { return pureNumber }
            let stringIndex = String.Index(utf16Offset: index, in: "## #### ####")
            let patternCharacter = "## #### ####"[stringIndex]
            guard patternCharacter != "#" else { continue }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        
        return pureNumber
    }
}
