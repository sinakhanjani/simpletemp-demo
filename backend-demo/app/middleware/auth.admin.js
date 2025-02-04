const jwt = require('jsonwebtoken')
const chalk = require('chalk')

const Admin = require('../modules/admin/model/admin.admin.model')
const scaffold = require('../scaffold/scaffold')
const message = require('../scaffold/message.scaffold')
const structure = require('./structure.scaffold')

const auth = async function (req, res, next) {
    try {
        const token = req.header('Authorization').replace('Bearer ', '')
        const decodedJWT = jwt.verify(token, process.env.JWT_SECRET_ADMIN)
        const admin = await Admin.findOne({ _id: decodedJWT._id, 'tokens.token': token })
        if (admin) {
            // add user to response
            req.user = admin
            // send response to clients
            await structure(req, res, next)
        } else {
            scaffold(res).failed(message.authenticate)
        }

    } catch (e) {
        scaffold(res).failed(message.authenticate)
    }
}

module.exports = auth
