const mongoose = require('mongoose');

const glucoseEntrySchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    user: { type: mongoose.Schema.Types.ObjectId, ref: 'User'},
    value: { type: Number, requirednp: true},
    timestamp: { type: Number, default: new Date().getTime() }
});

module.exports = mongoose.model('GlucoseEntry', glucoseEntrySchema);