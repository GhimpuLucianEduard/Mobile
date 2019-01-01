const express = require('express');
const router = express.Router();
const GlucoseEntry = require('../Models/glucoseEntry.js');
const mongoose = require('mongoose');
const checkAuth = require('../middleware/check-auth')
const jwt = require('jsonwebtoken');

router.get('/', checkAuth, (req, res, next) => {

    GlucoseEntry.find()
        .exec()
        .then(docs => {
            console.log(docs);
            res.status(200).json(docs)
        })
        .catch(err => {
            console.log(err);
            res.status(500).json(err);
        });
});

router.post('/', checkAuth, (req, res, next) => {

    console.log(req.body);
    const token = req.headers.authorization.split(" ")[1];
    const decoded = jwt.verify(token, process.env.JWT_KEY);
    console.log(decoded)
    const entry = new GlucoseEntry({
        _id: mongoose.Types.ObjectId(),
        value: req.body.value,
        user: decoded.userId,
        afterMeal: req.body.afterMeal,
        note: req.body.note,
        timestamp: req.body.timestamp
    });

    entry.save()
        .then(result => {
            console.log(result);
            res.status(200).json(result);
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            })
        });
});

router.get('/:id', checkAuth, (req, res, next) => {
    const id = req.params.id;
    GlucoseEntry.findById(id)
        .exec()
        .then(doc => {
            console.log(doc);
            if (doc) {
                res.status(200).json(doc)
            } else {
                res.status(404).json({
                    error: 'Invalid id.'
                })
            }
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            })
        })
});

router.delete('/:id', checkAuth, (req, res, next) => {
    const id = req.params.id;
    GlucoseEntry.remove({ _id: id })
        .exec()
        .then(result => {
            res.status(200).json(result);
        })
        .catch(err => {
            res.status(500).json(err);
        });
});

router.patch('/', checkAuth, (req, res, next) => {
    const entry = req.body;

    console.log(req.body);
    const token = req.headers.authorization.split(" ")[1];
    const decoded = jwt.verify(token, process.env.JWT_KEY);
    console.log(decoded);

    entry.userId = decoded.userId;
    entry._id = (entry._id === "") ? mongoose.Types.ObjectId() : entry._id;
    GlucoseEntry.update({ _id: entry._id }, { $set: entry })
        .exec()
        .then(result => {
            console.log(result);
            res.status(200).json(result);
        })
        .catch(err => {
            console.log(err);
            res.status(500).json(err);
        });
});

module.exports = router;