const multer = require('multer');
const fs = require('fs');
const moment = require('moment')
const {v4 : uuidv4} = require('uuid')

const generateRandomId = function () {
    const min = 0
    const max = 9999999
    const randomNumber = Math.floor(Math.random() * (max - min + 1)) + min

    return randomNumber
}

const storage = multer.diskStorage({
    limits: {
        fileSize: 10000000
    },
    destination(req, file, cb) {
        const path = `./${process.env.FILE_DIRECTORY}/`
        // directory existence check and creation
        if (!fs.existsSync(path)) {
            fs.mkdirSync(path, { recursive: true })
        }
        cb(null, path);
    },
    filename(req, file, cb) {
        const newId = uuidv4()
        file.ext = (file.originalname).split('.').pop();
        file.date = moment().format('YYYY-MM-DD-hh-mm')
        file.id = newId
        const userId = req.user._id ?? 'undefined'
        cb(null, `${file.date}-${file.fieldname}-${userId}-${file.id}.${file.ext}`);
    },
    fileFilter(req, file, cb) {
        if (!file.originalname.match(/\.(jpg|jpeg|png|pdf)$/)) {
            return cb(new Error('File format is not supported'))
        }

        cb(undefined, true)
    }
});

module.exports = multer({ storage })
