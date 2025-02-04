const express = require('express')

const multer = require('../../helper/multer.helper')

const { dentalTempAuth, fullAccessDentalTempAuth } = require('../../middleware/auth.dentalTemp')
const completeProfileController = require('./controller/completeProfile.dentalTemp.Controller')
const profileController = require('./controller/profile.dentalTemp.controller')
const ticketController = require('./controller/ticket.dentalTemp.controller')
const messageController = require('./controller/message.dentalTemp.controllers')
const rateController = require('./controller/rate.dentalTemp.controller')
const shiftController = require('./controller/shift.dentalTemp.controller')
const offerController = require('./controller/offer.dentalTemp.controller')
const invoiceController = require('./controller/invoice.dentalTemp.controller')
const notificationController = require('./controller/notification.dentalTemp.controller')
// add Router
const router = new express.Router()
// profile
router.get('/profile/me', dentalTempAuth, profileController.me)
router.post('/profile/logout', dentalTempAuth, profileController.logout)
router.post('/profile/delete', dentalTempAuth, profileController.delete)
router.post('/profile/updatefcmtoken', dentalTempAuth, profileController.updateFCMToken)
router.post('/profile/testnotification', dentalTempAuth, profileController.testNoification)
// complete profile
router.post('/uploadphoto', dentalTempAuth, multer.single('profile_photo'), completeProfileController.uploadPhoto)
router.post('/profile/accountinfo', dentalTempAuth, completeProfileController.completeAccountInfo)
router.post('/profile/personalinfo', dentalTempAuth, multer.fields([
    { name: 'dbs', maxCount: 1 },
    { name: 'resume', maxCount: 1 },
    { name: 'gdc', maxCount: 1 },
    { name: 'indemnityInsurance', maxCount: 1 },
    { name: 'helpB', maxCount: 1 }
]), completeProfileController.completePersonalInfo)
router.post('/profile/togglenotification', dentalTempAuth, completeProfileController.toggleNotification)
router.post('/profile/bankinfo', dentalTempAuth, completeProfileController.completeBankInfo)
// ticket 
router.get('/ticket', dentalTempAuth, ticketController.read)
router.post('/ticket/create', dentalTempAuth, ticketController.create)
// message 
router.get('/message/:ticketId', dentalTempAuth, messageController.read)
router.post('/message/send/:ticketId', dentalTempAuth, messageController.create)
// rate
router.get('/rate', dentalTempAuth, rateController.read)
router.post('/rate/clinic', fullAccessDentalTempAuth, rateController.create)
// shift and offer
router.get('/shift/calendar', dentalTempAuth, shiftController.calendar)
router.get('/shift/clinic/search', dentalTempAuth, shiftController.searchClinic)
router.get('/shift/confirmed', dentalTempAuth, shiftController.confirmedList)
router.get('/shift/money', dentalTempAuth, shiftController.money)
router.get('/shift/jobhistory', dentalTempAuth, shiftController.jobHistory)
router.get('/shift/home', dentalTempAuth, shiftController.home)
router.post('/shift', dentalTempAuth, shiftController.shiftByID)
router.post('/shift/markascomplete', fullAccessDentalTempAuth, shiftController.markAsComplete)
router.post('/shift/markaspaid', fullAccessDentalTempAuth, shiftController.markAsPaid)
router.post('/shift/requestshift', fullAccessDentalTempAuth, shiftController.requestShift)
// offer
router.post('/offer', fullAccessDentalTempAuth, offerController.create)
router.post('/offer/delete/:offerId', fullAccessDentalTempAuth, offerController.delete)
router.post('/offer/resubmitted', fullAccessDentalTempAuth, offerController.resubmitted)
// invoice
router.get('/invoice/shift/:shiftId', dentalTempAuth, invoiceController.byShiftID)
router.get('/invoice/findone/:invoiceId', dentalTempAuth, invoiceController.byInvoiceID)
router.post('/invoice/create', fullAccessDentalTempAuth, invoiceController.create)
router.post('/invoice/emailme', fullAccessDentalTempAuth, invoiceController.emailMe)
router.post('/invoice/agreedispute', fullAccessDentalTempAuth, invoiceController.agreeDispute)
router.post('/invoice/disagreedispute', fullAccessDentalTempAuth, invoiceController.disagreeDispute)
router.post('/invoice/resend', fullAccessDentalTempAuth, invoiceController.resend)
// disputed invoice
router.get('/disputedinvoice/findone/:disputedInvoiceId', dentalTempAuth, invoiceController.byDisputedInvoiceID)
// notification
router.get('/notification', dentalTempAuth, notificationController.read)

module.exports = router
