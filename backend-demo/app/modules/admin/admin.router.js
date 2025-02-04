const express = require('express')
const router = new express.Router()
const multer = require('../../helper/multer.helper')

// MARK: - controller
// common controller
const ticketController = require('./controller/ticket.admin.controller')
const faqController = require('./controller/faq.admin.controller')
const versionControl = require('./controller/version.admin.controller')
// dentalTemp controller
const authController = require('./controller/auth.admin.controller')
const dentalTempController = require('./controller/profile.dentalTemp.admin.controller')
const suspendUserController = require('./controller/suspend.admin.controller')
const identityDentalTempController = require('./controller/identity.dentalTemp.admin.controller')
// clinic controller
const identityClinicController = require('./controller/identity.clinic.admin.controller')
const clinicController = require('./controller/profile.clinic.admin.controller')
// rate controller
const dentalTempRateController = require('./controller/dentalTempRate.admin.controller')
const clinicRateController = require('./controller/clinicRate.admin.controller')
// setting controller
const settingController = require('./controller/setting.admin.controller')
// shift controller
const shiftController = require('./controller/shift.admin.controller')
const offerController = require('./controller/offer.admin.controller')
const penaltyController = require('./controller/penalty.admin.controller')
// giftcard
const giftcardController = require('./controller/giftcard.admin.controller')
// Invoice
const invoiceController = require('./controller/invoice.admin.controller')
const disputeInvoiceController = require('./controller/disputeInvoice.admin.controller')
// Stripe
const stripeController = require('./controller/stripe.webhook.admin.controller')
// Paymnet
const paymentController = require('./controller/payment.admin.controller')
// Commission
const commissionController = require('./controller/commission.admin.controller')
// Summary
const summaryController = require('./controller/summary.admin.controller')

// MARK: - middleware
const structure = require('../../middleware/structure.scaffold')
const adminAuth = require('../../middleware/auth.admin')

// MARK: - Common
// suspend
router.post('/suspend/create', adminAuth, suspendUserController.create)
router.post('/suspend/read', adminAuth, suspendUserController.read)
router.post('/suspend/delete/:id', adminAuth, suspendUserController.delete)
// ticket
router.post('/ticket/create', adminAuth, ticketController.create)
router.post('/ticket/sendmessage/:ticketId', adminAuth, ticketController.sendMessage)
router.post('/ticket/changestate/:ticketId', adminAuth, ticketController.changeState)
router.post('/ticket', adminAuth, ticketController.read)
router.get('/message/:ticketId', adminAuth, ticketController.messages)
// faq
router.post('/faq/create', adminAuth, faqController.create)
router.post('/faq/delete/:faqId', adminAuth, faqController.delete)
router.get('/faq', adminAuth, faqController.read)
// version control
router.post('/version/update', adminAuth, versionControl.update)
router.get('/version', adminAuth, versionControl.read)
// user rating
router.get('/rate/dentaltemp/:userId', adminAuth, dentalTempRateController.read)
router.get('/rate/clinic/:userId', adminAuth, clinicRateController.read)
// MARK: - dentalTemp
router.post('/dentaltemp/list', adminAuth, dentalTempController.read)
router.post('/dentaltemp/delete/:userId', adminAuth, dentalTempController.delete)
router.post('/dentaltemp/update/account/:userId', adminAuth, dentalTempController.updateAccount)
router.post('/dentaltemp/update/personal/:userId', adminAuth, multer.fields([{ name: 'dbs', maxCount: 1 },
{ name: 'resume', maxCount: 1 },
{ name: 'gdc', maxCount: 1 },
{ name: 'indemnityInsurance', maxCount: 1 },
{ name: 'helpB', maxCount: 1 }
]), dentalTempController.updatePersonal)
router.post('/dentaltemp/identity/:userId', adminAuth, identityDentalTempController.identity)
// MARK: - clinic
router.post('/clinic/list', adminAuth, clinicController.read)
router.post('/clinic/update/account/:userId', adminAuth, clinicController.update)
router.post('/clinic/delete/:userId', adminAuth, clinicController.delete)
router.post('/clinic/identity/:userId', adminAuth, identityClinicController.identity)
// MARK: - setting
router.get('/setting', adminAuth, settingController.read)
router.post('/setting/update', adminAuth, settingController.update)
// MARK: - shift
router.get('/shift/:shiftId', adminAuth, shiftController.shiftByID)
router.post('/shift', adminAuth, shiftController.read)
router.post('/shift/all', adminAuth, shiftController.readAll)
router.post('/shift/delete/:shiftId', adminAuth, shiftController.delete)
router.post('/shift/commission/pay/:shiftId', adminAuth, shiftController.payCommissionManually)
// MARK: - offer
router.post('/offer', adminAuth, offerController.read)
router.post('/offer/delete/:offerId', adminAuth, offerController.delete)
// MARK: - penalty
router.get('/penalty', adminAuth, penaltyController.read)
router.post('/penalty/delete/:penaltyId', adminAuth, penaltyController.delete)

module.exports = router