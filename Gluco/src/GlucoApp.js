import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import { createMaterialBottomTabNavigator } from 'react-navigation-material-bottom-tabs';
import { createBottomTabNavigator, createAppContainer } from 'react-navigation';
import Icon from 'react-native-vector-icons/FontAwesome';
import ListViewComponent from './components/ListViewComponent';

class MainListComponent extends Component {
  render() {
    return (
        <ListViewComponent style={styles.listView}/>
    );
  }
}

class StatisticsComponent extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text>
          Statistics
        </Text>
      </View>
    );
  }
}

class SettingsComponent extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Icon name="rocket" size={30} color="#f2f2f2" />
      </View>
    );
  }
}


const TabNavigator = createMaterialBottomTabNavigator({
  Glucose: {
    screen: MainListComponent,
    navigationOptions: {
      tabBarIcon: ({ tintColor }) => (<Icon name="list" size={24} color={tintColor} />)
    }
  },
  Statistics: {
    screen: StatisticsComponent,
    navigationOptions: {
      tabBarIcon: ({ tintColor }) => (<Icon name="line-chart" size={24} color={tintColor} />)
    }
  },
  Settings: {
    screen: SettingsComponent,
    navigationOptions: {
      tabBarIcon: ({ tintColor }) => (<Icon name="gear" size={24} color={tintColor} />)
    }
  }
}, {
    activeColor: '#D81B60',
    inactiveColor: '#3e2465',
    barStyle: { backgroundColor: '#f2f2f2' }
  })

export default createAppContainer(TabNavigator)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  listView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  
});
