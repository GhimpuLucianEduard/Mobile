const express = require('express');
const router = express.Router();
const User = require('../Models/user.js');
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

router.post('/login', async (req, res, next) => {
    try {
        const user = await User.findOne({email: req.body.email})
        if (user) {
            bcrypt.compare(req.body.password, user.password, async (err, resp) => {
                if (err) {
                    return res.status(404).json({
                        error: 'Wrong password.'
                    });
                }

                if (resp) {
                    const token = jwt.sign({
                        email: user.email,
                        userId: user._id
                    }, process.env.JWT_KEY, 
                    {
                        expiresIn: "1000d"
                    })
                    return res.status(200).json({
                        user: user,
                        token: token
                    });
                }

                res.status(404).json({
                    error: 'Auth failed.'
                });
            });
        } else {
            res.status(404).json({
                error: 'User does not exist.'
            });
        }
    } catch (e) {
        res.status(500).json(e);
    }
});

router.post('/signup', async (req, res, next) => {
    if (req.body.password) {
        try {
            const user = await User.findOne({ email: req.body.email });
            if (user) {
                return res.status(409).json({
                    error: 'Email already in use.'
                });
            } else {
                bcrypt.hash(req.body.password, 10, async (err, hash) => {
                    if (err) {
                        return res.status(500).json({
                            error: err
                        });
                    } else {
                        const user = new User({
                            _id: new mongoose.Types.ObjectId(),
                            email: req.body.email,
                            password: hash
                        });
                        try {
                            await user.save();
                            res.status(200).json({
                                message: 'User created!'
                            })
                        } catch (e) {
                            res.status(500).json(e)
                        }
                    }
                })
            }
        } catch (e) {
            res.status(500).json(e)
        }
    } else {
        res.status(401).json({
            error: 'Password cannot be empty.'
        })
    }
});

module.exports = router;