const express = require('express')
const multer = require('../../helper/multer.helper')
const stripe = require('stripe')(require('config').get('stripe.sk_test'))
// middleware
const { clinicAuth, fullAccessClinicAuth } = require('../../middleware/auth.clinic')
// controller
const completeProfileController = require('./controller/completeProfile.clinic.controller')
const profileController = require('./controller/profile.clinic.controller')
const ticketController = require('./controller/ticket.clinic.controller')
const messageController = require('./controller/message.clinic.controller')
const rateController = require('./controller/rate.clinic.controller')
const shiftController = require('./controller/shift.clinic.controller')
const offerController = require('./controller/offer.clinic.controller')
const invoiceController = require('./controller/invoice.clinic.controller')
const notificationController = require('./controller/notification.clinic.controller')

// Router
const router = new express.Router()
// profile
router.get('/profile/me', clinicAuth, profileController.me)
router.post('/profile/logout', clinicAuth, profileController.logout)
router.post('/profile/delete', clinicAuth, profileController.delete)
router.post('/profile/uploaddoc', clinicAuth, multer.single('document'), profileController.uploadDoc)
router.post('/profile/updatefcmtoken', clinicAuth, profileController.updateFCMToken)
router.post('/profile/testnotification', clinicAuth, profileController.testNoification)
// complete profile
router.post('/profile/setupintent', clinicAuth, completeProfileController.setupIntent)
router.post('/profile/uploadphoto', clinicAuth, multer.single('profile_photo'), completeProfileController.uploadPhoto)
router.post('/profile/accountinfo', clinicAuth, completeProfileController.completeAccountInfo)
router.post('/profile/detailinfo', clinicAuth, completeProfileController.completeDetailInfo)
router.post('/profile/togglenotification', clinicAuth, completeProfileController.toggleNotification)
// ticket 
router.get('/ticket', clinicAuth, ticketController.read)
router.post('/ticket/create', clinicAuth, ticketController.create)
// message 
router.get('/message/:ticketId', clinicAuth, messageController.read)
router.post('/message/send/:ticketId', clinicAuth, messageController.create)
// rate
router.get('/rate', clinicAuth, rateController.read)
router.post('/rate/dentaltemp', fullAccessClinicAuth, rateController.rateDentalTemp)
// shift
router.get('/shift/calendar', clinicAuth, shiftController.calendar)
router.get('/shift/home/pendingshifts', clinicAuth, shiftController.pendingshifts)
router.get('/shift/home/confirmedshifts', clinicAuth, shiftController.confirmedShifts)
router.post('/shift', fullAccessClinicAuth, shiftController.create)
router.post('/shift/delete/:shiftId', fullAccessClinicAuth, shiftController.delete)
router.post('/shift/delete/:shiftId', fullAccessClinicAuth, shiftController.delete)
// offer
router.post('/offer', fullAccessClinicAuth, offerController.read)
router.post('/offer/accept', fullAccessClinicAuth, offerController.accept)
router.post('/offer/resubmission', fullAccessClinicAuth, offerController.resubmission)
// invoice
router.get('/invoice/read', clinicAuth, invoiceController.read)
router.get('/invoice/findone/:invoiceId', clinicAuth, invoiceController.byInvoiceID)
router.post('/invoice/pay/:invoiceId', fullAccessClinicAuth, invoiceController.pay)
router.post('/invoice/paymanually/:invoiceId', fullAccessClinicAuth, invoiceController.payManually)
router.post('/invoice/emailme', fullAccessClinicAuth, invoiceController.emailMe)
router.post('/invoice/dispute', fullAccessClinicAuth, invoiceController.dispute)
// notification
router.get('/notification', clinicAuth, notificationController.read)

module.exports = router
