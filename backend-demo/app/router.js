const path = require('path')

const dentalTempRouter = require('./modules/dentalTemp/dentalTemp.router')
const clinicRouter = require('./modules/clinic/clinic.router')
const commonRouter = require('./modules/common/common.router')
const adminRouter = require('./modules/admin/admin.router')

const footerHTML = 'SimpleTemp all right reserved | 2022'

module.exports = (app) => {
    // add dentalTemp router:
    app.use('/api/v1/dentaltemp', dentalTempRouter)
    // add clinic router:
    app.all("/api/v1/clinic/*", (req, res, next) => {
        let origin = req.headers.origin
        // if (origin === "https://simpletemp.co.uk/clinic") {
        res.header("Access-Control-Allow-Origin", origin)
        // }
        res.header("Access-Control-Allow-Headers", ["Content-Type", "X-Requested-With", "Authorization", "X-HTTP-Method-Override", "Accept"])
        res.header("Access-Control-Allow-Credentials", true)
        res.header("Access-Control-Allow-Methods", "GET,POST")
        res.header("Cache-Control", "no-store,no-cache,must-revalidate")
        res.header("Vary", "Origin")
        next()
    })
    app.use('/api/v1/clinic', clinicRouter)
    // add common router
    app.all("/api/v1/common/*", (req, res, next) => {
        let origin = req.headers.origin
        // if (origin === "https://simpletemp.co.uk/clinic") {
        res.header("Access-Control-Allow-Origin", origin)
        // }
        res.header("Access-Control-Allow-Headers", ["Content-Type", "X-Requested-With", "Authorization", "X-HTTP-Method-Override", "Accept"])
        res.header("Access-Control-Allow-Credentials", true)
        res.header("Access-Control-Allow-Methods", "GET,POST")
        res.header("Cache-Control", "no-store,no-cache,must-revalidate")
        res.header("Vary", "Origin")
        next()
    })
    app.use('/api/v1/common', commonRouter)
    // add admin router:
    app.all("/api/v1/admin/*", (req, res, next) => {
        let origin = req.headers.origin
        // if (origin === "https://simpletemp.co.uk/admin") {
        res.header("Access-Control-Allow-Origin", origin)
        // }
        res.header("Access-Control-Allow-Headers", ["Content-Type", "X-Requested-With", "Authorization", "X-HTTP-Method-Override", "Accept"])
        res.header("Access-Control-Allow-Credentials", true)
        res.header("Access-Control-Allow-Methods", "GET,POST")
        res.header("Cache-Control", "no-store,no-cache,must-revalidate")
        res.header("Vary", "Origin")
        next()
    })
    app.use('/api/v1/admin', adminRouter)
    // static html route
    app.use('/api/v1', (req, res, next) => {
        res.render('index', {
            title: 'SimpleTemp',
            description: footerHTML,
            message: 'Application Programing Interface | V1',
            subMessage: 'You can communicate to server with "Postman API Collection" Documents.'
        })
    })
    // Privacy and Terms
    app.use('/termsofservice', (req, res) => {
        res.render('termsofservice', {
            title: '',
            description: footerHTML,
            message: ''
        })
    })
    app.use('/privacy', (req, res) => {
        res.render('privacy', {
            title: '',
            description: footerHTML,
            message: ''
        })
    })
    /*app.use('/support', (req, res) => {
        res.render('support', {
            title: '',
            description: footerHTML,
            message: ''
        })
    })*/
   
    // 404
    app.use('*', (req, res) => {
        res.render('404', {
            title: '404',
            description: footerHTML,
            message: 'Page not found!'
        })
    })
}
