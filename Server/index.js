const express = require('express');
const app = express();
const morgan = require('morgan');
const bodyParser = require('body-parser');
const glucoseRoutes = require('./routes/glucose.js')
const userRoutes = require('./routes/user.js')

const mongoose = require('mongoose');

mongoose.connect(
    'mongodb://admin:' +
    process.env.MONGO_ATLAS_PW +
    '@glucose-shard-00-00-b6a49.mongodb.net:27017,' +
    'glucose-shard-00-01-b6a49.mongodb.net:27017,' +
    'glucose-shard-00-02-b6a49.mongodb.net:27017/test?ssl=true&replicaSet=Glucose-shard-0&authSource=admin&retryWrites=true',
    {
        useNewUrlParser: true
    });


app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.use((req, res, next) => {

    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');

    if (req.method === 'OPTIONS') {
        res.header('Access-Control-Allow-Methods', 'PUT, POST, PATCH, DELETE');
        return res.status(200).json({});
    }

    next();
});

app.use('/glucose', glucoseRoutes);
app.use('/user', userRoutes);

app.use((req, res, next) => {
    const error = new Error('Not Found');
    error.status = 404;
    next(error);
});

app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: error.message
    })
});

module.exports = app;