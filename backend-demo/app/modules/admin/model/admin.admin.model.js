const mongoose = require('mongoose')
const validator = require('validator')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')

const authRoles = require('../statics/admin.authroles')
const message = require('../../../scaffold/message.scaffold')

const adminSchema = new mongoose.Schema({
    fullname: {
        type: String,
        required: true,
        trim: true
    },
    email: {
        type: String,
        trim: true,
        required: true,
        unique: true,
        validate(value) {
            if (!validator.isEmail(value)) {
                throw new Error(message.invalidInput('Email address').res)
            }
        }
    },
    password: {
        type: String,
        minlength: 8,
        required: true,
        trim: true,
        validate(value) {
            const isValidPassword = function (password) {
                function hasLowerCase(str) {
                    return str.toLowerCase() != str;
                }
                function hasUpperCase(str) {
                    return str.toUpperCase() != str;
                }
                function hasContainNumber(str) {
                    return str.match(/\d+/g)
                }

                return password.length > 8 &&
                    !password.includes('password') &&
                    hasLowerCase(password) &&
                    hasUpperCase(password) &&
                    hasContainNumber(password)
            }
            if (!isValidPassword(value)) {
                throw new Error(message.invalidInput('The password must be atleast 8 characters and contain atleast 1 uppercase character and 1 number.').res)
            }
        }
    },
    photoURL: {
        type: String,
        default: 'grin-beam.png'
    },
    role: {
        type: String,
        required: true,
        enum: authRoles.guest
    },
    tokens: [{
        token: {
            type: String,
            required: true
        }
    }]
}, {
    timestamps: true
})
