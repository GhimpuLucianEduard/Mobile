import React, { Component } from 'react'
import { FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';

export default class GlucoseEntryDetailsComponent extends Component {
  render() {

    const { navigation } = this.props;
    const itemId = navigation.getParam('text', 'NO-ID');

    return (
      <View>
        <Text>itemId: {JSON.stringify(itemId)}</Text>
      </View>
    )
  }
}
