const jwt = require('jsonwebtoken')
const chalk = require('chalk')

const identitiyTypes = require('../modules/clinic/statics/clinic.identities')
const Clinic = require('../modules/clinic/model/clinic.clinic.model')
const scaffold = require('../scaffold/scaffold')
const message = require('../scaffold/message.scaffold')
const structure = require('./structure.scaffold')

const clinicAuth = async function (req, res, next) {
    try {
        const token = req.header('Authorization').replace('Bearer ', '')
        const decodedJWT = jwt.verify(token, process.env.JWT_SECRET_CLINIC)

        const user = await Clinic.findOne({ _id: decodedJWT._id, 'tokens.token': token })
        if (user) {
            req.user = user
            req.userToken = token
            await structure(req, res, next)
        } else {
            scaffold(res).failed(message.authenticate)
        }

    } catch (e) {
        console.log(chalk.red.bold(`ERROR clinic.clinic.js (e) ${e}`));
        scaffold(res).failed(message.authenticate)
    }
}

const fullAccessClinicAuth = async function (req, res, next) {
    try {
        const token = req.header('Authorization').replace('Bearer ', '')
        const decodedJWT = jwt.verify(token, process.env.JWT_SECRET_CLINIC)
        const user = await Clinic.findOne({ _id: decodedJWT._id, 'tokens.token': token })

        if (user) {
            const percentage = (user.authenticationSteps.length / identitiyTypes.length) * 100

            if (percentage === 100) {
                req.user = user
                req.userToken = token
                await structure(req, res, next)
            } else {
                scaffold(res).failed(message.forcibleCompleteProfileClinic)
            }
        } else {
            scaffold(res).failed(message.authenticate)
        }
    } catch (e) {
        console.log(chalk.red.bold(`ERROR fullClinicTempAuth.clinic.js (e) ${e}`));
        scaffold(res).failed(message.authenticate)
    }
}

module.exports = {
    clinicAuth,
    fullAccessClinicAuth
}
