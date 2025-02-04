const express = require('express')
const path = require('path')
const router = require('./router')
const hbs = require('hbs')
const firebase = require('../app/helper/firebase.helper')
require('dotenv').config()
// configure firebase-admin
firebase.configuration()
require('../playground/sample')
require('./db/mongoose')
require('./cron')
// Setup express
const app = express()
// Define paths for Express config
const publicDirectoryPath = path.join(__dirname, '../public')
const viewsPath = path.join(__dirname, './templates/views')
const partialsPath = path.join(__dirname, './templates/partials')
// Use for react.js admin dashboard
app.use(express.static(rootBuildPath))
app.use(express.static(clinicBuildPath))
app.use(express.static(adminBuildPath))
// Setup handlebars engine and views location
app.set('view engine', 'hbs')
app.set('views', viewsPath)
hbs.registerPartials(partialsPath)
// Setup directory for documents
app.use(`/${process.env.FILE_DIRECTORY}`, express.static(process.env.FILE_DIRECTORY))
// Setup static directory to server
app.use(express.static(publicDirectoryPath))
app.use(express.json())
// Add route
router(app)

module.exports = app