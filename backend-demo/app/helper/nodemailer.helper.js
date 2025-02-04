const nodemailer = require('nodemailer')
const { google } = require('googleapis')
const chalk = require('chalk')
const config = require('config')

const OAuth2 = google.auth.OAuth2
const clientId = config.get('googleOauth.clientId')
const clientSecret = config.get('googleOauth.clientSecret')
const refreshToken = config.get('googleOauth.refreshToken')
const user = config.get('googleOauth.user')

module.exports = async function (html = `
    <h1> Welcome to SimpleTemp <h1>
`, subject = 'SimpleTemp', recipient) {
    try {
        const OAuth2_client = new OAuth2(clientId, clientSecret)
        OAuth2_client.setCredentials({ refresh_token: refreshToken })
        const accessTokenResponse = await OAuth2_client.getAccessToken()
        if (accessTokenResponse.res.data) {
            const accessToken = accessTokenResponse.res.data.access_token
            const transporter = nodemailer.createTransport({
                service: "gmail",
                auth: {
                    type: 'OAuth2',
                    user: user,
                    clientId: clientId,
                    clientSecret: clientSecret,
                    refreshToken: refreshToken,
                    accessToken: accessToken
                }
            })
    
            const mailoptions = {
                from: `SimpleTemp <hello@simpletemp.co.uk>`,
                to: recipient,
                subject: subject,
                // text: 'SimpleTemp'
                html: html
            }
    
            transporter.sendMail(mailoptions, function (error, result) {
                if (!error) {
                    console.log(chalk.green.bold(`Email send successfully to: ${recipient}`))
                }
    
                transporter.close()
            })
        }
    } catch (e) {
        console.log(chalk.red.bold(`Email could not be sent to ${recipient} ${e}`))
    }
}