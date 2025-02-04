const mongoose = require('mongoose')

const giftcardSchema = new mongoose.Schema({
    reedemcode: {
        type: String,
        required: true
    },
    isUsed: {
        type: Boolean,
        default: false,
        required: true
    },
    dentalTemp: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'DentalTemp',
        required: function () {
            return this.isUsed === true
        }
    },
    userType: {
        type: String,
        enum: ['nurse', 'hygienist'],
        required: function () {
            return this.isUsed === true
        }
    },
}, {
    timestamps: true
})

