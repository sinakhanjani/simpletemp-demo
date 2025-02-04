const message = require('./message.scaffold')

module.exports = function (response) {
    return {
        res: {
            success: true,
            data: null,
            message: ''
        },
        add: function (data) {
            this.res.data = data
            this.res.success = true
            this.res.message = message.success.res
            this.res.code = message.success.code
            response.status(200).send(this.res)
        },
        success: function (message) {
            this.res.data = undefined
            this.res.success = true
            this.res.message = message.res
            this.res.code = message.code
            response.status(200).send(this.res)
        },
        failed: function (message = message.unknown) {
            this.res.data = undefined
            this.res.success = false
            this.res.message = message.res
            this.res.code = message.code
            response.status(406).send(this.res)
        },
        eFailed: function (e) {
            console.log(e);
            if (e.errors) {
                const errors = e.errors
                const keys = Object.keys(errors)
                let message = ''
                keys.forEach(function (key) {
                    message += `${errors[key].message}\n`
                })
                this.failed({ res: message, code: 601 })
            } else if (e.code === 11000) {
                this.failed(message.duplicateItemFound)
            } else {
                var content = e.toString()
                
                this.failed({
                    res: content.replace('Error: ', '') ?? 'can not read the cast error.',
                    code: 451
                })
            }
        }
    }
}
