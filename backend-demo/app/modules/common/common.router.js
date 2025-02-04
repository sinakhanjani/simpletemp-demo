const express = require('express');
const router = new express.Router();

const authController = require('./controller/auth.common.controller')
const faqController = require('./controller/faq.common.controller')
const versionController = require('./controller/version.common.controller')
const settingController = require('./controller/setting.common.controller')

const structure = require('../../middleware/structure.scaffold')

// register user
router.post('/register', structure, authController.register)
// login user
router.post('/login', structure, authController.login)
// verify email code for both register and login
router.post('/verifyCode', structure, authController.verifyEmail)
// resend code
router.post('/sendcode', structure, authController.resendCode)
router.post('/changepassword', structure, authController.changePassword)
// faq
router.get('/faq', structure, faqController.read)
// version control
router.get('/version', structure, versionController.read)
// setting
router.get('/setting', structure, settingController.read)

module.exports = router