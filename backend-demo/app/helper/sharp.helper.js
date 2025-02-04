const sharp = require('sharp')

const compressJPG = (file,bounds) => {
    const filename = file.filename
    const path = file.path 
    const [name, ext] = filename.split('.')
    
    sharp(`./${path}`)
    .jpeg({ compressionLevel: 1, adaptiveFiltering: true, force: true })
    .resize(bounds)
    .withMetadata()
    .toFile(`./${process.env.FILE_DIRECTORY}/${name}-micro.${ext}`, function(err) {
        if (err) {
            
        }
    })
        
    return `/${process.env.FILE_DIRECTORY}/${name}-micro.${ext}`
}

module.exports = compressJPG