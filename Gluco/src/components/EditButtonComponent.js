import React, { Component } from 'react'
import { FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';

class EditButtonComponent extends Component {
  render() {
    return (
        <TouchableHighlight onPress={() => { this.props.navigation.navigate('Details') }}>
            <Icon style={{ alignSelf: "flex-end" }} name="remove" size={32} />
        </TouchableHighlight>
    )
  }
}

export default withNavigation(EditButtonComponent);