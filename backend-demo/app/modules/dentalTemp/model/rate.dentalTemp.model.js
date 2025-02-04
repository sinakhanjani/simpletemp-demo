const mongoose = require('mongoose')

const message = require('../../../scaffold/message.scaffold')

const dentalTempRateSchema = new mongoose.Schema({
    dentalTemp: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
        required: true
    },
    clinic: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Clinic',
        required: true
    },
    shift: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Shift',
        required: true
    },
    description: {
        type: String,
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
    professionalism: {
        type: Number,
        validate: function (value) {
            if (((value > 0) && value <= 5) && (parseInt(value) == value)) {
                return true
            } else {
                throw new Error(message.invalidRate.res)
            }
        }
    },
    timekeeping: {
        type: Number,
        validate: function (value) {
            if (((value > 0) && value <= 5) && (parseInt(value) == value)) {
                return true
            } else {
                throw new Error(message.invalidRate(value).res)
            }
        }
    },
    competencyAndSkills: {
        type: Number,
        validate: function (value) {
            if (((value > 0) && value <= 5) && (parseInt(value) == value)) {
                return true
            } else {
                throw new Error(message.invalidRate.res)
            }
        }
    }
}, {
    timestamps: true
})

dentalTempRateSchema.methods.toJSON = function () {
    const rate = this
    const rateObject = rate.toObject()
    // delete unnecessary rateObject keys
    delete rateObject.__v
    delete rateObject.dentalTemp
    delete rateObject.clinic
    delete rateObject.shift
    delete rateObject.updatedAt
    delete rateObject.professionalism
    delete rateObject.timekeeping
    delete rateObject.competencyAndSkills

    return rateObject
}

dentalTempRateSchema.pre('remove', async function (next) {
    const rate = this

    next()
})

const DentalTempRate = mongoose.model('DentalTempRate', dentalTempRateSchema)

module.exports = DentalTempRate