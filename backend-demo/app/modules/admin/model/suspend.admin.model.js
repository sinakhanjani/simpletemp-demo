const mongoose = require('mongoose')
const Account = require('../../account.js')
const Penalty = require('../model/penalty.common.model')

const suspendAccountSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        unique: true,
        'ref': () => (this.userType === 'clinic') ? 'Clinic' : 'DentalTemp'
    },
    userType: {
        type: String,
        required: true,
    },
    expiredDate: {
        type: Date
    },
    finite: {
        type: Boolean,
        default: false,
        required: true,
    },
    description: {
        type: String,
        required: true,
    }
}, {
    timestamps: true
})

suspendAccountSchema.methods.toJSON = function () {
    const suspendAccount = this
    const suspendAccountObject = suspendAccount.toObject()
    // delete unnecessary user keys
    delete suspendAccountObject.user.tokens
    delete suspendAccountObject.user.fcmToken
    delete suspendAccountObject.user.isAccountInformationRequired
    delete suspendAccountObject.user.updatedAt
    delete suspendAccountObject.user.createdAt
    delete suspendAccountObject.user.__v
    // delete unnecessary suspendAcount keys
    delete suspendAccountObject.updatedAt
    delete suspendAccountObject.createdAt
    delete suspendAccountObject.__v

    return suspendAccountObject
}

suspendAccountSchema.pre('save', async function (next) {
    const suspendAccount = this
    const finite = suspendAccount.finite

    if (finite === true) {
        const req = {
            body: {
                userType: suspendAccount.userType
            }
        }
        const user = await Account(req).findOne({ _id: suspendAccount.user })
        const penalty = await Penalty.findOne({ email: user.email, userType: user.userType })
        if (user) {
            if (penalty) {
                penalty.count += 1
                await penalty.save()
            } else {
                const newPenalty = new Penalty({
                    user: suspendAccount.user,
                    userType: suspendAccount.userType,
                    email: user.email,
                    count: 1
                })
                await newPenalty.save()
            }
        }
    }

    next()
})

const SuspendAccount = mongoose.model('SuspendAccount', suspendAccountSchema)

module.exports = SuspendAccount