const DentalTempNotification = require('../modules/dentalTemp/model/notification.dentalTemp.model')
const ClinicNotification = require('../modules/clinic/model/notification.clinic.model')

module.exports = function (body, tokens = []) {
    /*
    {
          notification: {
              title: "title",
              body: "body"
          },
          data: { // could be null
              userType: '',
              meta: {
                  type: '',
                  _id: '',
                  ref: ''
              }
          }
      }
    */
    let data = {}

    if (body.data.userType === 'clinic') {
        const notification = new ClinicNotification({
            ...body,
            clinic: body.data._id
        })
        notification.save()
    } else {
        const notification = new DentalTempNotification({
            ...body,
            dentalTemp: body.data._id
        })
        notification.save()
    }

    if (body.data.meta) {
        if (body.data.meta.type) {
            data.type = body.data.meta.type
        }
        if (body.data.meta.ref) {
            data.ref = body.data.meta.ref
        }
        if (body.data.meta._id) {
            data._id = body.data.meta._id.toString()
        }
        if (body.data.meta.offerId) {
            data.offerId = body.data.meta.offerId.toString()
        }
    }

    const notif = {
        data,
        notification: body.notification,
        apns: { payload: { aps: { sound: 'default' } } },
        priority: "high",
        mutableContent: true,
        tokens: tokens, // all user fcm tokens
        ttl: process.env.NOTIF_ALIVE_TIME // notif alive time (second)
    }

    return notif
}