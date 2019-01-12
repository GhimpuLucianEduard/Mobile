import React, { Component } from 'react'
import { Alert, TimePickerAndroid, TouchableOpacity, FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { CheckBox, FormLabel, FormInput, FormValidationMessage, Card, ListItem, Button } from 'react-native-elements';
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';
import DatePicker from 'react-native-datepicker'
import DateTimePicker from 'react-native-modal-datetime-picker';

export default class GlucoseEntryDetailsComponent extends Component {

  state = {
    entry: {},
    isSave: false,
    isFirstLoad: true,
    afterMeal: true,
    value: "",
    date: "2019-01-10",
    time: "00:00"
  }

  _showDateTimePicker = async () => {
    try {
      const { action, hour, minute } = await TimePickerAndroid.open({
        hour: 14,
        minute: 0,
        is24Hour: true, // Will display '2 PM'
      });
      var minToSave = "";
      if (minute === 0) {
        minToSave = "00";
      } else {
        minToSave = minute;
      }
      const timedroid = hour + ":" + minToSave;
      this.setState({
        time: timedroid
      })
      if (action !== TimePickerAndroid.dismissedAction) {
      }
    } catch ({ code, message }) {
      console.warn('Cannot open time picker', message);
    }
  }

  getDate = (timestamp) => {

    var date = new Date(timestamp);
    return date.getFullYear() + "-"
      + (date.getMonth() + 1) + "-"
      + date.getDate();
  }


  getTime = (timestamp)=> {

    var date = new Date(timestamp);
    return date.getHours() + ":"
        + date.getMinutes();
}
  constructor(props) {
    super(props)

    this.state = {
      entry: props.navigation.state.params.entry,
      isSave: props.navigation.state.params.isSave,
      isFirstLoad: true,
      afterMeal: props.navigation.state.params.entry.afterMeal,
      value: `${props.navigation.state.params.entry.value}`,
      date: this.getDate(props.navigation.state.params.entry.timestamp),
      time: this.getTime(props.navigation.state.params.entry.timestamp)
    }

    //console.log(this.state.entry);
  }

  render() {

    const { navigation } = this.props;

    return (
      <View>
        <FormLabel>Value</FormLabel>
        <FormInput onChangeText={(text) => this._inputChanged(text)} value={this.state.value} placeholder="type value here..."></FormInput>
        <FormLabel>After Meal</FormLabel>
        <CheckBox
          checked={this.state.afterMeal}
          onPress={() => this._onChangeCheck()}
          title='Click Here'
        />
        <FormLabel>Date</FormLabel>
        <DatePicker
          style={{ width: 200 }}
          date={this.state.date}
          mode="date"
          placeholder="select date"
          format="YYYY-MM-DD"
          minDate="1971-05-01"
          confirmBtnText="Confirm"
          cancelBtnText="Cancel"
          customStyles={{
            dateIcon: {
              position: 'absolute',
              left: 0,
              top: 4,
              marginLeft: 0
            },
            dateInput: {
              marginLeft: 36
            }
          }}
          onDateChange={(date) => {
            this.setState({ date: date });
            console.log(this.state.date);
            this._showDateTimePicker();
          }}
        />
        <FormLabel>Time</FormLabel>
        <FormInput value={this.state.time} editable={false} placeholder="time"></FormInput>


        <Button
          onPress={() => this._onSaveClicked()}
          raised
          title='SAVE' />

      </View>
    )
  }

  _inputChanged = (text) => {
    console.log(`Input changedL: ${text}`)
    this.setState({
      value: text
    })
  }

  _onSaveClicked = () => {
    var msg = "";
    const valueNr = this.state.value;
    if (valueNr === "" || parseInt(valueNr) < 0 || parseInt(valueNr) > 400) {
      msg += "Value invalid! \n";
    }
    if (msg !== "") {
      Alert.alert(
        'Error',
        msg,
        [
          { text: 'OK', onPress: () => console.log('OK Pressed') },
        ],
        { cancelable: false }
      )
    } else {
      var dateAr = this.state.date.split("-");
      var hourAr = this.state.time.split(":");


      var dateTemp = new Date(this.state.date);
      dateTemp.setHours(hourAr[0]);
      dateTemp.setMinutes(hourAr[1]);
      var timestampJs = dateTemp.getTime();
      var entryFS = { ...this.state.entry };
      entryFS.value = valueNr;
      entryFS.timestamp = timestampJs;
      console.log(this.state.afterMeal);
      entryFS.afterMeal = this.state.afterMeal || false;
      //pop  request and save async
      this.props.navigation.state.params.returnData(entryFS);
      this.props.navigation.goBack();
    }
  }

  _onChangeCheck = () => {
    this.setState({ afterMeal: !this.state.afterMeal });
    console.log(this.state.afterMeal);
  }
}
