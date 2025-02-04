const mongoose = require('mongoose')
const moment = require('moment')
const message = require('../../../scaffold/message.scaffold')
const Setting = require('../../admin/model/setting.admin.model')
const Offer = require('./offer.common.model')

const shiftSchema = new mongoose.Schema({
    clinic: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Clinic',
        required: true
    },
    createdBy: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
    },
    acceptedOffer: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Offer'
    },
    dentalTemp: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
    },
    invoice: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Invoice',
    },
    date: { // min: current and max: 90 day from now
        type: Date,
        required: true,
    },
    userType: {
        type: String,
        enum: ['nurse', 'hygienist'],
        required: true,
    },
    arrivalTime: {
        type: Date,
        required: true,
    },
    endTime: {
        type: Date,
        required: true,
    },
    preferredPrice: {
        type: Number,
        required: true,
        validate: {
            validator: async function (value) {
                const setting = await Setting.find()
                if (this.userType === 'nurse') {
                    return setting[0].nusrseHourlyPrices.includes(value)
                } else {
                    return setting[0].hygienistHourlyPrices.includes(value)
                }
            },
            message: (props) =>
                `${props.value} is an invalid value as it violates the limitations set by the request.`,
        }
    },
    unpaidBreakTime: {
        type: String,
        enum: ['unpaid', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55', '60'],
        default: 'unpaid',
        required: true
    },
    status: {
        type: String,
        enum: ['accepted', 'completed', 'rated', 'paid'],
        required: true,
        default: 'accepted'
    },
    isResubmission: {
        type: Boolean,
        required: true,
        default: false,
    },
    commissionPaid: {
        type: Boolean,
        required: true,
        default: false,
    }
}, {
    timestamps: true
})

// add virtual offers:
shiftSchema.virtual('offers', {
    ref: 'Offer',
    localField: '_id',
    foreignField: 'shift'
})

shiftSchema.methods.toJSON = function () {
    const shift = this
    const shiftObject = shift.toObject()

    delete shiftObject.__v
    delete shiftObject.createdAt
    delete shiftObject.updatedAt
    delete shiftObject.createdBy
    delete shiftObject.dentalTemp
    delete shiftObject.clinic
    delete shiftObject.invoice
    delete shiftObject.acceptedOffer
    delete shiftObject.isResubmission
    delete shiftObject.commissionPaid

    return shiftObject
}

shiftSchema.pre('remove', async function (next) {
    const shift = this
    await Offer.deleteMany({
        shift: shift._id,
    })

    next()
})

const Shift = mongoose.model('Shift', shiftSchema)

module.exports = Shift