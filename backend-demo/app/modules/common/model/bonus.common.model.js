const mongoose = require('mongoose')

const bonusSchema = new mongoose.Schema({
    count: {
        type: Number,
        required: true,
        default: 0
    },
    userType: {
        type: String,
        required: true,
        enum: ['nurse', 'hygienist'],
    },
    dentalTemp: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
    },
}, {
    timestamps: true
})

bonusSchema.methods.toJSON = function () {
    const bonus = this
    const bonusObject = bonus.toObject()

    delete bonusObject.__v

    return bonusObject
}

bonusSchema.pre('save', async function (next) {
    const bonus = this

    next()
})

const Bonus = mongoose.model('Bonus', bonusSchema)

module.exports = Bonus