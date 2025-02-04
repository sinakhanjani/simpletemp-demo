const moment = require('moment')

module.exports = {
    durationDiff: (firstDate, secondDate) => {
        const duration = moment.duration(firstDate.diff(secondDate))

        return duration.asSeconds()
    }
}
