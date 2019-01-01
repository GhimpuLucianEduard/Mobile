const mongoose = require('mongoose');

const glucoseEntrySchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true},
    value: { type: Number, requirednp: true},
    afterMeal: { type: Boolean, required: true},
    note: { type: String },
    timestamp: { type: Number, default: new Date().getTime() }
}, { versionKey: false });

module.exports = mongoose.model('GlucoseEntry', glucoseEntrySchema);