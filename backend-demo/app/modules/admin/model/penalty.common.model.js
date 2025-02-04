const mongoose = require('mongoose')

const penaltySchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': () => (this.userType === 'clinic') ? 'Clinic' : 'DentalTemp',
        required: true
    },
    userType: {
        type: String,
        enum: ['nurse', 'hygienist', 'clinic'],
        required: true,
    },
    email: {
        type: String,
        required: true,
    },
    count: {
        type: Number,
        default: 0,
        required: true
    }
}, {
    timestamps: true
})

penaltySchema.methods.toJSON = function () {
    const penalty = this
    const penaltyObject = penalty.toObject()

    delete penaltyObject.__v
    // delete penaltyObject.createdAt
    delete penaltyObject.updatedAt

    return penaltyObject
}

const Penalty = mongoose.model('Penalty', penaltySchema)

module.exports = Penalty