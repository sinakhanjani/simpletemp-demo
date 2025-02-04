const mongoose = require('mongoose')
const fs = require('fs')
const path = require('path')
const chalk = require('chalk')
const Admin = require('../modules/admin/model/admin.admin.model')
const Setting = require('../modules/admin/model/setting.admin.model')
const Shift = require('../modules/common/model/shift.common.model')
const Offer = require('../modules/common/model/offer.common.model')

mongoose.connect(process.env.MONGODB_URL)
    .then(async () => {
        console.log(chalk.green.bold('mongodb connected'))
        const superAdmin = await Admin.findOne({ role: 'SA' })
        const setting = await Setting.find()

        if (!superAdmin) {
            // add super admin
            const admin = new Admin({
                fullname: 'Super Admin',
                email: 'superadmin@simpletemp.co.uk',
                password: 'Demo1234%&^S901',
                role: 'SA'
            })
            await Admin.create(admin)
        }

        if (setting.length === 0) {
            const setting = new Setting({
                nusrseHourlyRates: [10, 11, 12, 13, 14, 15],
                hygienistHourlyRates: [34, 35, 36, 37, 38, 39, 40],
                nurseShiftCommission: 29,
                hygienistShiftCommission: 29
            })
            await Setting.create(setting)
        }
    })
    .catch((err) => {
        console.log(err)
    })