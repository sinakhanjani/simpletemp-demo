module.exports = {
    success: {
        res: 'Success',
        code: 200
    },
    authenticate: {
        res: 'User not authenticated with valid token',
        code: 401
    },
    suspend: {
        res: 'Your account has been suspended، Please contact hello@simpletemp.co.uk.',
        code: 403
    },
    notFound: {
        res: 'Not found',
        code: 404
    },
    unknown: {
        res: 'Something went wrong!',
        code: 451
    },
    unitShouldLessThanCharacter: (character) => ({
        res: `Unit must have ${character} digits`,
        code: 602
    }),
    invalidInput: (input) => {
        return ({
            res: `${input} is not correct, please try again.`,
            code: 603
        });
    },
    sendEmailVerificationCode: {
        res: 'Verification code sent successfully.',
        code: 604
    },
    userIsRegistered: {
        res: 'You are already registered, please login to your account.',
        code: 605
    },
    idHasBeenExisted: {
        res: 'ID already exist',
        code: 606
    },
    idIsRequire: {
        res: 'Parameter ID is require',
        code: 607
    },
    parameterIsRequire: {
        res: 'Path or Parameter is required.',
        code: 608
    },
    successPayment: {
        res: 'Your payment was successful and recorded to mongodb',
        code: 609
    },
    notAnyRequestFound: {
        res: 'Request not found',
        code: 610
    },
    invalidRate: (value) => ({
        res: `Invalid rate ${value}, rate > 0 and rate <= 5 and typeof Int`,
        code: 611
    }),
    resendVerificationCodeRestriction: (timeInterval) => ({
        res: `Please wait ${timeInterval} minutes to resend a verification code`,
        code: 612
    }),
    userNotFound: {
        res: 'Incorrect Username or Password',
        code: 613
    },
    verificationCodeExpired: {
        res: 'Your verification code has expired, please try again',
        code: 614
    },
    invalidVerificationCode: {
        res: 'The verification code is incorrect, please try again',
        code: 615
    },
    verificationCodeUsed: {
        res: 'You can only use this code once.',
        code: 616
    },
    invalidPassword: {
        res: 'The password you entered is incorrect, please try again.',
        code: 617
    },
    expiredLoginTime: {
        res: 'Your login time has expired, please wait',
        code: 618
    },
    expiredLoginTime: (timeInterval) => ({
        res: `Your login time has expired, please wait ${timeInterval} seconds to login again`,
        code: 619
    }),
    notRegistered: {
        res: 'It looks like your account has not yet been verified. Please check your email and follow the verification link to log in',
        code: 620
    },
    uploadSuccessfully: {
        res: 'File uploaded successfully',
        code: 621
    },
    adminNotFound: {
        res: 'Admin not found, please register',
        code: 622
    },
    duplicateItemFound: {
        res: 'This record already exists in the database',
        code: 623
    },
    changePasswordExpiredُTime: {
        res: 'Password change time has expired, please try again',
        code: 624
    },
    suspendRemoved: {
        res: 'Suspension removed',
        code: 625
    },
    fileNotFound: {
        res: 'File not found',
        code: 626
    },
    forcibleCompleteProfileClinic: {
        res: 'Please complete your profile to start sending offers. It won\'t take long!',
        code: 627
    },
    forcibleCompleteProfileTemp: {
        res: 'Please complete your profile to start sending offers. It won\'t take long!',
        code: 689
    },
    cityNotFound: {
        res: 'City not found',
        code: 628
    },
    ticketClosed: {
        res: 'This ticket is closed. Please open a new ticket to send a message.',
        code: 629
    },
    openTicketValidation: {
        res: 'Your ticket has been registered. We will be with you shortly',
        code: 630
    },
    invalidTimeInterval: (timeInterval) => ({
        res: `The time you have inputted is invalid. Please select a minimum of ${timeInterval} hours per shift.`,
        code: 631
    }),
    maximumShiftPerDay: (userType) => ({
        //// userType: nurse || hygienist
        res: `You have created a maximum of 8 shifts for ${userType} per day.`,
        code: 632
    }),
    offerSent: {
        res: 'You have already submitted an offer for this shift.',
        code: 633
    },
    maximumOfferPerDay: (count) => ({
        //// userType: nurse || hygienist
        res: `You have sent a maximum of ${count} offers per day.`,
        code: 634
    }),
    invalidDate: {
        res: 'Please select a valid date.',
        code: 636
    },
    offerExpired: {
        res: 'This offer has expired.',
        code: 637
    },
    offerAccepted: {
        res: 'This offer has been accepted.',
        code: 638
    },
    offerNotExpire: {
        res: 'This offer is not expired.',
        code: 639
    },
    offerSubmitted: {
        res: 'This offer has been resubmitted.',
        code: 640
    },
    shiftCanceledByClinic: (count) => {
        let body = this.suspend
        if (count === 1) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation, your account will be suspended for 7 days. It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental professionals negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 641
            }
        }
        if (count === 2) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation for the second time, your account will be suspended for 3 months.  It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental professionals negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 641
            }
        }
        if (count >= 3) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation for the third time, your account is now disabled indefinitely. It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental professionals negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 641
            }
        }
        return body
    },
    shiftCanceledByDentalTemp: (count) => {
        let body = this.suspend
        if (count === 1) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation, your account will be suspended for 7 days. It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental clinics negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 644
            }
        }
        if (count === 2) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation for the second time, your account will be suspended for 14 days. It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental clinics negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 644
            }
        }
        if (count >= 3) {
            body = {
                res: `We take all cancellations seriously. Due to cancellation after shift confirmation for the third time, your account will be suspended for 30 days. It is important to the SimpleTemp community that you keep your commitments as cancellations impact dental clinics negatively. Please contact hello@simpletemp.co.uk if you have future confirmed shifts and need assistance tracking them. We are help to help!`,
                code: 644
            }
        }
        return body
    },
    shiftNotStarted: {
        res: 'This shift has not yet started. You may mark this shift as complete at the end of your shift.',
        code: 647
    },
    shiftCompleted: {
        res: 'This shift was already marked as completed.',
        code: 648
    },
    offerNotAcceptedByClinic: {
        res: 'The offer has not been accepted by the clinic yet and it is not possible to mark shift as complete.',
        code: 649
    },
    shiftIsAccpeted: {
        res: 'You have already been confirmed for a shift on this date. Unfortunately, you may not place any more offers.',
        code: 650
    },
    shiftAcceptedByAnotherUser: {
        res: 'The shift was accepted. Please go through the list again and select another shift.',
        code: 651
    },
    shiftDatePast: {
        res: 'Action not permitted.\nInvalid date and time.',
        code: 652
    },
    shiftNotFound: {
        res: 'Shift not found',
        code: 653
    },
    offerNotFound: {
        res: 'Offer not found',
        code: 654
    },
    offerAlreadyResubmitted: {
        res: 'This offer has already been resubmitted.',
        code: 655
    },
    dentalTempNotAvailable: {
        res: 'The temp you are trying to hire was accepted by another office. Please review other offers.',
        code: 656
    },
    anotherOfferAccepted: {
        res: 'You have already accepted an offer on this date. Unfortunately you can not accept more than one offer per day.',
        code: 657
    },
    offerNotAccepted: {
        res: 'The offer is not accepted',
        code: 658
    },
    offerRated: {
        res: 'You have already rated this shift.',
        code: 659
    },
    workInClinicRestricted: {
        res: 'You have never worked in this clinic.',
        code: 660
    },
    requestShiftRestricted: {
        res: 'You have already submitted a shift request for this date.',
        code: 661
    },
    filterDateNotAccepted: {
        res: 'You are unable to request shifts beyond 1 month.',
        code: 662
    },
    completeWrongStep: {
        res: 'You are not able to complete this step.',
        code: 663
    },
    invoiceNotFound: {
        res: 'Invoice not found.',
        code: 664
    },
    disputeInvoiceNotFound: {
        res: 'Dispute invoice not found.',
        code: 665
    },
    invoiceSentUnableResendAgain: {
        res: 'It seems that you have already sent an invoice today. Please be patient as the clinic reviews your invoice and try again tomorrow.',
        code: 666
    },
    disputeInvoiceSentUnableSendAgain: {
        res: 'You sent the disputed invoice before.',
        code: 667
    },
    disputeInvoiceReplied: {
        res: 'You have already accepted this disputed invoice. Please hang tight as the clinic reviews and pays.',
        code: 668
    },
    shiftIsDisputedUnablePay: {
        res: 'This shifts invoice is currently being disputed. You will be able to make a payment after the dispute is resolved.',
        code: 669
    },
    setupIntentNotFound: {
        res: 'Setup intent not found.',
        code: 670
    },
    setupIntentIsNotSuccess: {
        res: 'Setup intent was not successful.',
        code: 671
    },
    PaymentVerifiedCanNotBeChanged: {
        res: 'PaymentVerified and EmailVerified can not be changed',
        code: 672
    },
    localIntentNotFound: {
        res: 'Local intent not found.',
        code: 673
    },
    paymentIntentNotFound: {
        res: 'Payment intent not found.',
        code: 674
    },
    paymentNotSuccess: {
        res: 'Invoice payment was not successful, try again.',
        code: 674
    },
    invoicePaid: {
        res: 'This invoice has been paid.',
        code: 675
    },
    unpaidInvoiceUnableDeleteAccount: {
        res: 'You have pending invoices to pay. You are not able to delete your account until they are paid.',
        code: 676
    },
    offerAcceptedUnableDeleteAccount: {
        res: 'You have pending shifts associated to your account. You are unable to delete your account until they are completed.',
        code: 677
    },
    invoiceNotCreated: {
        res: 'The invoice has not yet been issued by the dental temp.',
        code: 678
    },
    invoiceHasPaidByClinic: {
        res: 'The invoice has been paid by the clinic and your payment will be made by the company soon',
        code: 679
    },
    disputedInvoiceWilNotBePaid: {
        res: 'The disputed invoice will not be paid.',
        code: 680
    },
    shiftCommissionPaid: {
        res: 'The commission for this shift has been paid.',
        code: 681
    },
    emailVerifiedCanNotBeChanged: {
        res: 'EmailVerified and EmailVerified can not be changed',
        code: 682
    },
    invoiceAlreadyCreated: {
        res: 'You have already created an invoice.',
        code: 683
    },
    invoiceAlreadyCreated: {
        res: 'This invoice has already been paid',
        code: 684
    },
    shiftCountDocuments_confirmed_unwork_shift: {
        res: 'You cannot delete your account as you have an upcoming confirmed shift. We kindly request that you consider keeping the shift, as the hygienist is relying on your presence.',
        code: 685
    },
    shiftCountDocuments_confirmed_unpaid_completed_shift: {
        res: 'You are unable to delete your account due to pending invoices. To proceed, kindly access the "Money" tab located at the bottom of the page. If no pending invoices are visible in the "Money" tab, please submit a ticket and our team will promptly address the matter.',
        code: 686
    },
    shiftCountDocuments_invoice_pending: {
        res: 'You are unable to delete your account due to pending invoices. To proceed, kindly access the "Money" tab located at the bottom of the page. If no pending invoices are visible in the "Money" tab, please submit a ticket and our team will promptly address the matter.',
        code: 687
    },
    certificateRequired: {
        res: 'To activate the user account, please upload your certificates in the profile information section',
        code: 688
    },
    indemnityInsuranceExpired: {
        res: 'Indemnity Insurance Expired!, Message: “Please upload your updated insurance to continue using SimpleTemp.',
        code: 689
    },
    dbsIssueDate: {
        res: 'DBS certificate Expired!, Message: “Please upload your updated insurance to continue using SimpleTemp.',
        code: 690
    },
    proofOfWorkExpired: {
        res: 'Your right to work details are out of date!, Message: “Please upload your updated insurance to continue using SimpleTemp.',
        code: 691
    },
}