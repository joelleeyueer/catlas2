const fs = require('fs');
require('dotenv').config();

const environment = fs.readFileSync('./src/environments/environment.prod.ts.template', 'utf8');
const environmentWithEnv = environment.replace('${GOOGLE_MAPS_API_KEY}', process.env.GOOGLE_MAPS_API_KEY);

fs.writeFileSync('./src/environments/environment.prod.ts', environmentWithEnv);