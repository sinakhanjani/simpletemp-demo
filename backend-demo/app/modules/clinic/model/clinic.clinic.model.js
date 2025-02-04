const mongoose = require('mongoose')
const validator = require('validator')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const moment = require('moment')
const config = require('config')
const stripe = require('stripe')(config.get('stripe.sk_test'))

const message = require('../../../scaffold/message.scaffold')
const authenticationTypes = require('../statics/clinic.identities')

const accountInformationSchema = require('./accountInformation.clinic.model')
const detailInformationSchema = require('./detailInformation.clinic.model')
const intentSchema = require('./intent.clinic.model')

const ClinicRate = require('./rate.clinic.model')
const Shift = require('../../common/model/shift.common.model')
const Payment = require('../../common/model/payment.common.model')
const Commission = require('../../common/model/commission.common.model')
const Setting = require('../../admin/model/setting.admin.model')
const Invoice = require('../../common/model/invoice.common.model')
const Ticket = require('../../common/model/ticket.common.model')
const { log } = require('winston')

const formatter = new Intl.NumberFormat('GBP', {
    style: 'currency',
    currency: 'GBP',
    // These options are needed to round to whole numbers if that's what you want.
    //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
})

const clinicSchema = new mongoose.Schema({
    fullname: {
        type: String,
        required: true,
        trim: true,
        minlength: 4,
        validate(value) {
            if (!value.length < this.minlength) {
                throw new Error(message.unitShouldLessThanCharacter(this.minlength).res)
            }
        }
    },
    photoURL: {
        type: String
    },
    email: {
        type: String,
        trim: true,
        required: true,
        validate(value) {
            if (!validator.isEmail(value)) {
                throw new Error(message.invalidInput('Email address').res)
            }
        }
    },
    password: {
        type: String,
        minlength: 8,
        required: true,
        trim: true,
        validate(value) {
            const isValidPassword = function (password) {
                function hasLowerCase(str) {
                    return str.toLowerCase() != str;
                }
                function hasUpperCase(str) {
                    return str.toUpperCase() != str;
                }
                function hasContainNumber(str) {
                    return str.match(/\d+/g)
                }
                function hasNoSpace(str) {
                    return !str.includes(' ')
                }
                return password.length >= 8 &&
                    !password.includes('password') &&
                    hasLowerCase(password) &&
                    hasUpperCase(password) &&
                    hasContainNumber(password) &&
                    hasNoSpace(password)
            }
            if (!isValidPassword(value)) {
                throw new Error(message.invalidInput('The password must be atleast 8 characters and contain atleast 1 uppercase character and 1 number.  The password').res)
            }
        }
    },
    userType: {
        type: String,
        enum: ['clinic'],
        default: 'clinic',
        required: true,
    },
    isEmailVerified: {
        type: Boolean,
        require: true,
        default: false
    },
    authenticationSteps: {
        type: [String],
        enum: authenticationTypes,
        require: true,
        default: [],
        validate(values) {
            function hasDuplicates(values) {
                return (new Set(values)).size !== values.length;
            }
            if (this.isEmailVerified) {
                if (!hasDuplicates(values)) {
                    return values.includes('EmailVerified')
                } else {
                    return false
                }
            } else {
                return true
            }
        }
    },
    authenticationCode: {
        code: {
            type: String,
            minlength: 6
        },
        isExpired: {
            type: Boolean,
            default: false
        },
        passwordToken: {
            type: String
        },
        updatedAt: {
            type: Date,
        }
    },
    profile: {
        percentage: {
            type: Number,
            default: 0
        },
        isPendingIdentity: {
            type: Boolean,
            default: false
        },
        accountInformation: {
            type: accountInformationSchema
        },
        detailInformation: {
            type: detailInformationSchema
        },
        bankInformation: {
            customerId: {
                type: String
            },
            intents: [{
                type: intentSchema
            }]
        },
        description: {
            type: String
        },
        docURL: {
            type: String
        }
    },
    activeNotification: {
        type: Boolean,
        default: true,
        required: true
    },
    fcmTokens: [{
        fcmToken: {
            type: String,
            validate: function (value) {
                return value.length > 0
            }
        },
        deviceId: {
            type: String,
            required: function () {
                return this.fcmToken !== undefined
            }
        }
    }],
    tokens: [{
        token: {
            type: String,
            required: true
        }
    }],
    deletedEmail: {
        type: String,
        validate(value) {
            if (!validator.isEmail(value)) {
                throw new Error(message.invalidInput('Email address').res)
            }
        }
    },
    hasShift: {
        type: Boolean,
        default: false,
    },
    badge: {
        type: Number,
        default: 0,
        required: true
    },
}, {
    timestamps: true
})

clinicSchema.methods.toJSON = function () {
    const clinic = this
    const clinicTempObject = clinic.toObject()

    const path = `${process.env.SERVER_ADDRESS}/${process.env.FILE_DIRECTORY}`
    const photoURL = clinic.photoURL
    const docURL = clinic.profile.docURL

    if (photoURL) {
        clinicTempObject.photoURL = `${path}/${photoURL}`
    }
    if (docURL) {
        clinicTempObject.profile.docURL = `${path}/${docURL}`
    }

    clinicTempObject.profile.percentage = ((clinicTempObject.authenticationSteps.length / authenticationTypes.length) * 100)

    delete clinicTempObject.deletedEmail
    delete clinicTempObject.password
    delete clinicTempObject.tokens
    delete clinicTempObject.profile.bankInformation.customerId
    // delete clinicTempObject.profile.isPendingIdentity
    delete clinicTempObject.fcmTokens
    delete clinicTempObject.isEmailVerified
    delete clinicTempObject.authenticationCode
    delete clinicTempObject.isAccountInformationRequired
    delete clinicTempObject.updatedAt
    // delete clinicTempObject.createdAt
    delete clinicTempObject.__v

    return clinicTempObject
}

clinicSchema.methods.generateAuthToken = async function () {
    const clinic = this
    const token = jwt.sign({ _id: clinic._id.toString() }, process.env.JWT_SECRET_CLINIC, { expiresIn: '365d' })
    clinic.tokens = clinic.tokens.concat({ token })

    return token
}

clinicSchema.methods.hideAccount = async function () {
    const clinic = this
    // remove SuspendAccount if needed
    const SuspendAccount = require('../../admin/model/suspend.admin.model')
    await SuspendAccount.findOneAndDelete({ user: clinic._id })
    await ClinicRate.findOneAndDelete({ user: clinic._id })
        // close All tickets
    await Ticket.updateMany({ user: clinic._id, state: { $ne: 'closed' } }, { "$set": { state: 'closed' } })
    // shifts by this clinic
    const shifts = await Shift.find({
        clinic: clinic._id,
        dentalTemp: undefined,
        acceptedOffer: undefined,
    })
    await Promise.all(shifts.map(async function (shift) {
        await shift.remove()
    }))
    // get today commission when clinic removed
    const commissionShifts = await Shift.find({
        clinic: clinic._id,
        acceptedOffer: { $ne: undefined },
        date: {
            $lt: moment().startOf('day').add(1, 'day').toDate()
        },
        commissionPaid: false,
    })
    await Promise.all(commissionShifts.map(async function (shift) {
        const intent = clinic.profile.bankInformation.intents.find((intent) => (intent.isDefault))
        if (intent) {
            const settings = await Setting.find()
            const amount = (shift.userType === 'nurse') ? settings[0].nurseShiftCommission : settings[0].hygienistShiftCommission
           
            const invoice = await Invoice.findOne({ shift: shift._id })
            const paymentIntent = await stripe.paymentIntents.create({
                amount: (amount.toFixed(2)) * 100,
                currency: 'gbp',
                customer: clinic.profile.bankInformation.customerId,
                payment_method: intent.paymentMethodId,
                off_session: true,
                confirm: true,
                receipt_email: clinic.email,
                description: `Hello ${clinic.fullname}\nThis is a receipt for the service fee that pertains to the following shift:\nShift Date: ${moment(shift.date).format("dddd, MMMM Do YYYY")}\nStart to End: ${moment(invoice.arrivalTime).format("h:mm a")}-${moment(invoice.endTime).format("h:mm a")}`
            })
            if (paymentIntent) {
                if (paymentIntent.status === 'succeeded') {
                    await shift.populate({
                        path: 'dentalTemp',
                        select: ['_id', 'fullname', 'email', 'userType']
                    })
                    const dentalTemp = shift.dentalTemp
                    let body = {
                        amount: Math.ceil(amount),
                        bankInformation: {
                            customerId: clinic.profile.bankInformation.customerId,
                            intent: intent
                        },
                        paymentIntentId: paymentIntent.id,
                        shift: shift._id,
                        clinic: clinic._id
                    }
                    const payment = await Payment.findOne({ shift: shift._id, 'clinic._id': clinic._id })
                    if (payment) {
                        body.payment = payment._id
                    }
                    const commission = new Commission(body)
                    if (payment) {
                        payment.commission = commission._id
                        await payment.save()
                    }

                    await commission.save()
                    if (payment) {
                        payment.commission = commission._id
                        await payment.save()
                    }
                    shift.commissionPaid = true
                    await shift.save()
                }
            }
        }
    }))
    // hide account
    clinic.tokens = []
    clinic.fcmTokens = []
    clinic.authenticationSteps = []
    clinic.isEmailVerified = false
    // delete email key
    clinic.deletedEmail = clinic.email
    clinic.email = `${clinic._id}@simpletemp.uk.co`

    await clinic.save()
}

clinicSchema.pre('save', async function (next) {
    const clinic = this
    // const userObject = clinic.toObject()
    if (clinic.isModified('password')) {
        clinic.password = await bcrypt.hash(clinic.password, 8)
    }
    if (clinic.isModified('tokens.token')) {
        clinic.isEmailVerified = true
        if (!clinic.authenticationSteps.includes('EmailVerified')) {
            clinic.authenticationSteps.push('EmailVerified')
        }
    }
    if (clinic.tokens.length > process.env.MAX_ACCOUNT_TOKEN) {
        clinic.tokens.shift()
    }

    clinic.email = clinic.email.toLowerCase()

    next()
})

// clinicSchema.pre('remove', async function (next) {
//     const clinic = this

//     next()
// })

const Clinic = mongoose.model('Clinic', clinicSchema)

module.exports = Clinic