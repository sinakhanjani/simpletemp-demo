const moment = require('moment')
const mongoose = require('mongoose')
const Shift = require('../../common/model/shift.common.model')
const Offer = require('../../common/model/offer.common.model')
const Setting = require('../../admin/model/setting.admin.model')

const disputeInvoiceSchema = new mongoose.Schema({
    // input data
    arrivalTime: {
        type: Date,
        required: true,
    },
    endTime: {
        type: Date,
        required: true,
    },
    unpaidBreakTime: {
        type: String,
        enum: ['unpaid', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55', '60'],
        default: 'unpaid',
        required: true
    },
    // set in router by backend
    preferredPrice: {
        type: Number,
        required: true,
    },
    userType: {
        type: String,
        enum: ['nurse', 'hygienist'],
        required: true,
    },
    isAgree: {
        type: String,
        required: true,
        enum: ['none', 'yes', 'no', 'byAdmin'],
        default: 'none'
    },
    // proccess by backend
    factorID: {
        type: String,
        required: true,
    },
    totalTime: {
        type: String,
        required: true,
        default: function () {
            const endDate = moment(this.endTime)
            const firstTime = moment(this.arrivalTime)
            const duration = moment.duration(endDate.diff(firstTime))
            let minute = duration.minutes()
            let hour = duration.hours()
            if (minute < 10) {
                minute = `0${minute}`
            }
            if (hour < 10) {
                hour = `0${hour}`
            }
            return `${hour}:${minute}`
        },
        validate: function () {
            const endDate = moment(this.endTime)
            const firstTime = moment(this.arrivalTime)
            const duration = moment.duration(endDate.diff(firstTime))
            if (duration.asHours() > 0) {
                return true
            } else {
                throw new Error(message.invalidInput('total Time').res)
            }
        }
    },
    billableTime: {
        type: String,
        required: true,
        default: function () {
            let endDate = moment(this.endTime)
            const firstTime = moment(this.arrivalTime)
            if (this.unpaidBreakTime !== 'unpaid') {
                endDate.subtract(Number(this.unpaidBreakTime), 'minutes')
            }
            const duration = moment.duration(endDate.diff(firstTime))
            let minute = duration.minutes()
            let hour = duration.hours()
            if (minute < 10) {
                minute = `0${minute}`
            }
            if (hour < 10) {
                hour = `0${hour}`
            }
            return `${duration.asHours().toFixed(2)}`
        },
        validate: function () {
            let endDate = moment(this.endTime)
            const firstTime = moment(this.arrivalTime)
            if (this.unpaidBreakTime !== 'unpaid') {
                endDate.subtract(Number(this.unpaidBreakTime), 'minutes')
            }
            const duration = moment.duration(endDate.diff(firstTime))
            if (duration.asHours() > 0) {
                return true
            } else {
                throw new Error(message.invalidInput(' Time').res)
            }
        }
    },
    totalPrice: {
        type: Number,
    },
    // relations
    invoice: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Invoice',
        required: true
    },
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
    offer: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Offer',
        required: true
    },
    shift: {
        type: mongoose.Schema.Types.ObjectId,
        'ref': 'Shift',
        required: true
    }
}, {
    timestamps: true
})