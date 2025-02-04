const fa = require("firebase-admin")

// this is for firebase-admin installation
// https://www.npmjs.com/package/firebase-admin#documentation
// this is for add topic and send a message to topic:
// https://firebase.google.com/docs/cloud-messaging/js/topic-messaging

module.exports = {
  configuration: function () {
    var serviceAccount = require("../simpletemp-ec368-firebase-adminsdk-mdaof-b1d8da7f8a.json");
    fa.initializeApp({
      credential: fa.credential.cert(serviceAccount)
    })
  },
  sendMulticast: function (notif) {
    fa.messaging().sendMulticast(notif)
      .then((response) => {
        if (response.failureCount > 0) {
          const failedTokens = []
          response.responses.forEach((resp, idx) => {
            if (!resp.success) {
              failedTokens.push(idx)
            }
          })
          console.log('List of tokens that caused failures: ' + failedTokens)
        }
      })
  },
  send: function (notif) {
    fa.messaging().send(notif)
      .then((response) => {
        console.log('Successfully sent message: ', response)
      })
      .catch((error) => {
        console.log('Error sending message: ', error)
      })
  },
  createTopic: function (name) {
    //// https://firebase.google.com/docs/cloud-messaging/js/topic-messaging
    // V2
  },
  sendToTopics: function () {
    //// https://firebase.google.com/docs/cloud-messaging/js/topic-messaging
    // V2
  }
}