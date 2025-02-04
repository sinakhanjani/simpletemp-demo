const app = require('./app')
const http = require('http')
// const socketio = require('socket.io')

// Create WebServer
const httpServer = http.createServer(app)
// const io = socketio(server)

module.exports = httpServer
