const mongoose = require('mongoose')
const intentSchema = require('../../clinic/model/intent.clinic.model')
const accountInformationSchema = require('../../clinic/model/accountInformation.clinic.model')

const paymentSchema = new mongoose.Schema({
    paymentMethod: {
        type: String,
        required: true,
        enum: ['card', 'manually']
    },
    currency: {
        type: String,
        required: true,
        default: 'gbp'
    },
    amount: {
        type: Number,
        required: true
    },
    paidToDentalTemp: {
        type: Boolean,
        required: true,
        default: false
    },
    bankInformation: {
        customerId: {
            type: String,
            required: function () {
                return this.paymentMethod === 'card'
            }
        },
        intent: {
            type: intentSchema,
            required: function () {
                return this.paymentMethod === 'card'
            }
        }
    },
    paymentIntentId: {
        type: String,
        required: function () {
            return this.paymentMethod === 'card'
        }
    },
    // relations
    clinic: {
        _id: mongoose.Schema.Types.ObjectId,
        fullname: String,
        email: String,
        profile: {
            accountInformation: {
                type: accountInformationSchema
            },
        },
    },
    dentalTemp: {
        _id: mongoose.Schema.Types.ObjectId,
        fullname: String,
        email: String,
        userType: String
    },
    invoice: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Invoice',
    },
    offer: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Offer',
    },
    shift: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Shift',
    },
    commission: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Commission',
    },
}, {
    timestamps: true
})

paymentSchema.methods.toJSON = function () {
    const payment = this
    const paymentObject = payment.toObject()

    delete paymentObject.__v
    delete paymentObject.invoice
    delete paymentObject.offer
    delete paymentObject.shift
    delete paymentObject.commission
    delete paymentObject.dentalTemp
    delete paymentObject.clinic

    return invoiceObject
}

paymentSchema.pre('save', async function (next) {
    const payment = this

    next()
})

const Payment = mongoose.model('Payment', paymentSchema)

module.exports = Payment