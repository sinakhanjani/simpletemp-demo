const mongoose = require('mongoose')

const summarySchema = new mongoose.Schema({
    // commission
    commissionAmount: {
        type: Number,
        required: true
    },
    commissionCreatedCount: {
        type: Number,
        required: true
    },
    // invoice
    invoiceCreatedCount: {
        type: Number,
        required: true
    },
    // users
    nurseCreatedCount: {
        type: Number,
        required: true
    },
    hygienistCreatedCount: {
        type: Number,
        required: true
    },
    clinicCreatedCount: {
        type: Number,
        required: true
    },
    // dispute
    disputeCreatedCount: {
        type: Number,
        required: true
    },
}, {
    timestamps: true
})

summarySchema.methods.toJSON = function () {
    const summary = this
    const summaryObject = summary.toObject()

    delete summaryObject.__v

    return summaryObject
}

const Summary = mongoose.model('Summary', summarySchema)

module.exports = Summary