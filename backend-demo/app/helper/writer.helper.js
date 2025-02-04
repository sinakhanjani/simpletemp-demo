const fs = require('fs')
const path = require('path')

const decode = function(filename, callback) {
    try {
        const route = path.join(__dirname, '../json/' + `${filename}.js`)            
        const dataBuffer = fs.readFileSync(route)
        const dataJSON = dataBuffer.toString()
        const data = JSON.parse(dataJSON)
        callback(data)
    } catch (e) {
        console.log(e)
    }
}

const encode = function(data, filename) {
    try {
        const route = path.join(__dirname, '/json/' + `${filename}.js`)            
        const dataJSON = JSON.stringify(data)        
        fs.writeFileSync(route, dataJSON, "utf8")
    } catch (e) {
        console.log(e)
    }
}

module.exports = {
    decode,
    encode
}