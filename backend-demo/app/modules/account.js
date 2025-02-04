const Admin = require('./admin/model/admin.admin.model')
const Clinic = require('./clinic/model/clinic.clinic.model')
const DentalTemp = require('./dentalTemp/model/dentalTemp.dentalTemp.model')

module.exports = function (req) {
    switch (req.body.userType) {
        case 'nurse': return DentalTemp
        case 'hygienist': return DentalTemp
        case 'clinic': return Clinic
        case 'admin': return Admin
        default: throw new Error('userType is undefined')
    }
}