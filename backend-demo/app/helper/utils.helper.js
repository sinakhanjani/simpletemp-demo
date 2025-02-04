module.exports = {
    host: (req) => req.protocol + '://' + req.get('host')
}