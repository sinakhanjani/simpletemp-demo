const mongoose = require('mongoose')
const validator = require('validator')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')

const message = require('../../../scaffold/message.scaffold')
const identitiyTypes = require('../statics/dentalTemp.identities')

const accountInformationSchema = require('./accountInformation.dentalTemp.model')
const personalInformationSchema = require('./personalInformation.dentalTemp.model')
const bankInformationSchema = require('./bankInformation.dentalTemp.model')

const DentalTempRate = require('./rate.dentalTemp.model')
const Offer = require('../../common/model/offer.common.model')
const Ticket = require('../../common/model/ticket.common.model')

const dentalTempSchema = new mongoose.Schema({
    fullname: {
        type: String,
        required: true,
        trim: true,
        minlength: 2,
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
                throw new Error(message.invalidInput('The password must be atleast 8 characters and contain atleast 1 uppercase character and 1 number.').res)
            }
        }
    },
    userType: {
        type: String,
        enum: ['nurse', 'hygienist'],
        required: true,
    },
    isEmailVerified: {
        type: Boolean,
        require: true,
        default: false
    },
    authenticationSteps: {
        type: [String],
        enum: identitiyTypes,
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
        personalInformation: {
            type: personalInformationSchema
        },
        bankInformation: {
            type: bankInformationSchema
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
    badge: {
        type: Number,
        default: 0,
        required: true
    },
}, {
    timestamps: true
})

dentalTempSchema.methods.toJSON = function () {
    const dentalTemp = this
    const dentalTempObject = dentalTemp.toObject()

    const path = `${process.env.SERVER_ADDRESS}/${process.env.FILE_DIRECTORY}`
    const photoURL = dentalTempObject.photoURL
    const dbsCertificationURL = dentalTempObject.profile?.personalInformation?.dbsCertificationURL
    const resume = dentalTempObject.profile?.personalInformation?.resumeURL
    const gdcCertificationURL = dentalTempObject.profile?.personalInformation?.gdcCertificationURL
    const indemnityInsuranceURL = dentalTempObject.profile?.personalInformation?.indemnityInsuranceURL
    const helpBCertificateURL = dentalTempObject.profile?.personalInformation?.helpBCertificateURL

    if (photoURL) {
        dentalTempObject.photoURL = `${path}/${photoURL}`
    }
    if (dbsCertificationURL) {
        dentalTempObject.profile.personalInformation.dbsCertificationURL = `${path}/${dbsCertificationURL}`
    }
    if (resume) {
        dentalTempObject.profile.personalInformation.resumeURL = `${path}/${resume}`
    }
    if (gdcCertificationURL) {
        dentalTempObject.profile.personalInformation.gdcCertificationURL = `${path}/${gdcCertificationURL}`
    }
    if (indemnityInsuranceURL) {
        dentalTempObject.profile.personalInformation.indemnityInsuranceURL = `${path}/${indemnityInsuranceURL}`
    }
    if (helpBCertificateURL) {
        dentalTempObject.profile.personalInformation.helpBCertificateURL = `${path}/${helpBCertificateURL}`
    }

    dentalTempObject.profile.percentage = ((dentalTempObject.authenticationSteps.length / identitiyTypes.length) * 100)

    delete dentalTempObject.deletedEmail
    delete dentalTempObject.password
    delete dentalTempObject.tokens
    delete dentalTempObject.fcmTokens
    delete dentalTempObject.isEmailVerified
    delete dentalTempObject.authenticationCode
    delete dentalTempObject.isAccountInformationRequired
    // delete dentalTempObject.profile.isPendingIdentity
    delete dentalTempObject.updatedAt
    // delete dentalTempObject.createdAt
    delete dentalTempObject.__v

    if (dentalTempObject.userType === 'hygienist') {
        delete dentalTempObject.profile.personalInformation?.specialties
    }

    return dentalTempObject
}

dentalTempSchema.methods.generateAuthToken = async function () {
    const dentalTemp = this
    const token = jwt.sign({ _id: dentalTemp._id.toString() }, process.env.JWT_SECRET_DENTALTEMP, { expiresIn: '365d' })
    dentalTemp.tokens = dentalTemp.tokens.concat({ token })

    return token
}

dentalTempSchema.methods.hideAccount = async function () {
    const dentalTemp = this
    // remove SuspendAccount if needed
    const SuspendAccount = require('../../admin/model/suspend.admin.model')
    await SuspendAccount.findOneAndDelete({ user: dentalTemp._id })
    await DentalTempRate.findOneAndDelete({ user: dentalTemp._id })
    await Offer.deleteMany({
        dentalTemp: dentalTemp._id,
        state: { $ne: 'accepted' }
    })
    // close all tickets
    await Ticket.updateMany({ user: dentalTemp._id, state: { $ne: 'closed' } }, { "$set": { state: 'closed' } })
    // hide account
    dentalTemp.tokens = []
    dentalTemp.fcmTokens = []
    dentalTemp.isEmailVerified = false
    dentalTemp.authenticationSteps = []
    dentalTemp.deletedEmail = dentalTemp.email
    dentalTemp.email = `${dentalTemp._id}@simpletemp.uk.co`
    await dentalTemp.save()
}

dentalTempSchema.pre('save', async function (next) {
    const dentalTemp = this
    // const userObject = dentalTemp.toObject()
    if (dentalTemp.isModified('password')) {
        dentalTemp.password = await bcrypt.hash(dentalTemp.password, 8)
    }
    if (dentalTemp.isModified('tokens.token')) {
        dentalTemp.isEmailVerified = true
        if (!dentalTemp.authenticationSteps.includes('EmailVerified')) {
            dentalTemp.authenticationSteps.push('EmailVerified')
        }
    }
    if (dentalTemp.tokens.length > process.env.MAX_ACCOUNT_TOKEN) {
        dentalTemp.tokens.shift()
    }

    dentalTemp.email = dentalTemp.email.toLowerCase()

    next()
})

// dentalTempSchema.pre('remove', async function (next) {
//     const dentalTemp = this
//     next()
// })

module.exports = mongoose.model('DentalTemp', dentalTempSchema)