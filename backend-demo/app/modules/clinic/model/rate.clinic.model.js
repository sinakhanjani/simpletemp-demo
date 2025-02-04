const mongoose = require('mongoose')

const message = require('../../../scaffold/message.scaffold')

const clinicRateSchema = new mongoose.Schema({
    clinic: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Clinic',
        required: true
    },
    dentalTemp: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
        required: true 
    },
    shift: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Shift',
        required: true
    },
    acceptedOffer: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Shift',
        required: true
    },
    rate: {
        type: Number,
        required: true,
        validate: function (value) {
            if (((value > 0) && value <= 5) && (parseInt(value) == value)) {
                return true
            } else {
                throw new Error(message.invalidRate.res)
            }
        }
    },
    description: {
        type: String,

    }
}, {
    timestamps: true
})

clinicRateSchema.methods.toJSON = function () {
    const rate = this
    const rateObject = rate.toObject()

    delete rateObject.__v
    delete rateObject.dentalTemp
    delete rateObject.clinic
    delete rateObject.shift
    delete rateObject.updatedAt

    return rateObject
}

clinicRateSchema.pre('remove', async function (next) {
    const rate = this

    next()
})

const ClinicRate = mongoose.model('ClinicRate', clinicRateSchema)

module.exports = ClinicRate