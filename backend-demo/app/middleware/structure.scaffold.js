const moment = require('moment-timezone');
const scaffold = require('../scaffold/scaffold')
const message = require('../scaffold/message.scaffold')
const SuspendAccount = require('../modules/admin/model/suspend.admin.model')
const Penalty = require('../modules/admin/model/penalty.common.model')

const structure = async function (req, res, next) {
    try {
        console.log(req.path);
        res.scaffold = scaffold(res)
        // check is user suspend or not
        if (req.user) {
            const suspendAccount = await SuspendAccount.findOne({ user: req.user._id, userType: req.user.userType })
            if (suspendAccount) {
                if (suspendAccount.finite === true) {
                    const expiredDate = suspendAccount.expiredDate
                    // check expiration date
                    const endDate = moment(expiredDate)
                    const currentDate = moment.tz("Europe/London").toDate()
                    const duration = moment.duration(endDate.diff(currentDate))
                    const isExpired = (duration.asHours() > 0) ? false : true
                    if (isExpired) {
                        // remove suspend
                        await suspendAccount.remove()
                        next()
                    } else {
                        const penalty = await Penalty.findOne({ email: req.user.email, userType: req.user.userType })

                        if (penalty) {
                            if (penalty.userType === 'clinic') {
                                if (req.path === '/profile/logout') {
                                    next()
                                } else {
                                    scaffold(res).failed(message.shiftCanceledByClinic(penalty.count))
                                }
                            } else {
                                if (req.path === '/profile/logout') {
                                    next()
                                } else {
                                    scaffold(res).failed(message.shiftCanceledByDentalTemp(penalty.count))
                                }
                            }
                        } else {
                            scaffold(res).failed(message.suspend)
                        }
                    }
                } else {
                    scaffold(res).failed(message.suspend)
                }
            } else {
                next()
            }
        } else {
            next()
        }
    } catch (e) {
        scaffold(res).failed(message.unknown)
    }
}

module.exports = structure
