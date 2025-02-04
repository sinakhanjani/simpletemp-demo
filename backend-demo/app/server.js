const httpServer = require('./io')
const chalk = require('chalk')

// Listening server on port (process.env.PORT)
httpServer.listen(process.env.PORT, () => {
  const message = `Socket: localhost is opening on port: ${process.env.PORT}`
  console.log(chalk.green.inverse(message))
})