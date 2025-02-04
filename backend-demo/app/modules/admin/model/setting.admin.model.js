const mongoose = require('mongoose')

const settingSchema = new mongoose.Schema({
    nusrseHourlyPrices: {
        type: [Number],
        required: true
    },
    hygienistHourlyPrices: {
        type: [Number],
        required: true
    },
    nurseShiftCommission: {
        type: Number,
        required: true
    },
    hygienistShiftCommission: {
        type: Number,
        required: true
    }
}, {
    timestamps: true
})

