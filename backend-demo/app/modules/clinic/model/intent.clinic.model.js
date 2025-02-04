const mongoose = require('mongoose')

module.exports = new mongoose.Schema({
    setupIntentId: {
        type: String,
    },
    paymentMethodId: {
        type: String,
    },
    card: {
        brand: {
            type: String
        },
        expMonth: {
            type: String
        },
        expYear: {
            type: String
        },
        last4: {
            type: String
        }
    },
    status: {
        type: String
    },
    isDefault: {
        type: Boolean,
        default: false,
    },
})
