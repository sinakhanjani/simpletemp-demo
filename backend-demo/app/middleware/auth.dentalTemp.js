const jwt = require('jsonwebtoken')
const chalk = require('chalk')

const identitiyTypes = require('../modules/dentalTemp/statics/dentalTemp.identities')
const DentalTemp = require('../modules/dentalTemp/model/dentalTemp.dentalTemp.model')
const scaffold = require('../scaffold/scaffold')
const message = require('../scaffold/message.scaffold')
const structure = require('./structure.scaffold')
const moment = require('moment')

const dentalTempAuth = async function (req, res, next) {
    try {
        const token = req.header('Authorization').replace('Bearer ', '')
        const decodedJWT = jwt.verify(token, process.env.JWT_SECRET_DENTALTEMP)
        const user = await DentalTemp.findOne({ _id: decodedJWT._id, 'tokens.token': token })

        if (user) {
            req.user = user
            req.userToken = token
            await structure(req, res, next)
        } else {
            scaffold(res).failed(message.authenticate)
        }

    } catch (e) {
        console.log(chalk.red.bold(`ERROR dentalTempAuth.dentalTemp.js (e) ${e}`));
        scaffold(res).failed(message.authenticate)
    }
}

const fullAccessDentalTempAuth = async function (req, res, next) {
    try {
        const token = req.header('Authorization').replace('Bearer ', '')
        const decodedJWT = jwt.verify(token, process.env.JWT_SECRET_DENTALTEMP)
        const user = await DentalTemp.findOne({ _id: decodedJWT._id, 'tokens.token': token })

        if (user) {
            const percentage = (user.authenticationSteps.length / identitiyTypes.length) * 100

            if (percentage === 100) {
                if (user.profile.personalInformation.resumeURL === undefined) {
                    scaffold(res).failed(message.certificateRequired)
                } else {
                    const today = Date()
                    const indemnityInsuranceExpiryDate = moment(user.profile.personalInformation.indemnityInsuranceExpiryDate).toDate()
                    const dbsExpiryDate = moment(user.profile.personalInformation.dbsIssueDate).add(544, 'day').toDate()
                    const proofOfWorkExpiryDate = moment(user.profile.personalInformation.proofOfWorkExpiryDate).toDate()

                    if (today > indemnityInsuranceExpiryDate) {
                        scaffold(res).failed(message.indemnityInsuranceExpired)
                    } else {
                        if (today > dbsExpiryDate) {
                            scaffold(res).failed(message.dbsIssueDate)
                        } else {
                            if (today > proofOfWorkExpiryDate) {
                                scaffold(res).failed(message.proofOfWorkExpired)
                            } else {
                                req.user = user
                                req.userToken = token
                                await structure(req, res, next)
                            }
                        }
                    }
                }
            } else {
                scaffold(res).failed(message.forcibleCompleteProfileTemp)
            }
        } else {
            scaffold(res).failed(message.authenticate)
        }
    } catch (e) {
        console.log(chalk.red.bold(`ERROR fullDentalTempAuth.dentalTemp.js (e) ${e}`));
        scaffold(res).failed(message.authenticate)
    }
}

module.exports = {
    dentalTempAuth,
    fullAccessDentalTempAuth
}
