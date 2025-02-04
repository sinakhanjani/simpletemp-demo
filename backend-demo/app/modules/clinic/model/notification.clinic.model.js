const mongoose = require('mongoose')
const message = require('../../../scaffold/message.scaffold')

const clinicNotifSchema = new mongoose.Schema({
    notification: {
        title: {
            type: String,
            required: true,
        },
        body: {
            type: String,
            required: true,
        }
    },
    data: {
        userType: {
            type: String,
            enum: ['clinic'],
            required: true
        },
        meta: {
            type: {
                type: String
            },
            _id: {
                type: mongoose.Schema.Types.ObjectId,
            },
            ref: {
                type: String,
            }
        }
    },
    isRead: {
        type: Boolean,
        required: true,
        default: false,
    },
    clinic: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Clinic',
        required: true
    },
}, {
    timestamps: true
})

clinicNotifSchema.methods.toJSON = function () {
    const notif = this
    const notifObject = notif.toObject()
    // delete unnecessary notifObject keys
    delete notifObject.__v
    delete notifObject.clinic
    delete notifObject.updatedAt
    delete notifObject.data.userType

    return notifObject
}

clinicNotifSchema.pre('save', async function (next) {
    const notif = this
    // send notif here.
    next()
})

const ClinicNotification = mongoose.model('ClinicNotification', clinicNotifSchema)

module.exports = ClinicNotification