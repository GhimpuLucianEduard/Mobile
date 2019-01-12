import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, Button, AsyncStorage } from 'react-native';
import { createMaterialBottomTabNavigator } from 'react-navigation-material-bottom-tabs';
import { createSwitchNavigator, createBottomTabNavigator, createAppContainer, createStackNavigator } from 'react-navigation';
import Icon from 'react-native-vector-icons/FontAwesome';
import ListViewComponent from './components/ListViewComponent';
import GlucoseEntryDetailsComponent from './components/GlucoseEntryDetailsComponent';
import SignInComponent from './components/SignInComponent'
import AuthLoadingScreen from './components/AuthLoadingScreen'
import { LineChart, Grid, YAxis } from 'react-native-svg-charts'


class MainListComponent extends Component {
  render() {
    return (
      <ListViewComponent style={styles.listView} />
    );
  }
}


class StatisticsComponent extends Component {


  state = {
    data: []
  }


  getData = async () => {
    let rez = [];
    var listOfEntries = await AsyncStorage.getItem('entries');
    const parsed = JSON.parse(listOfEntries);
    const sorted = parsed.sort(function (a, b) {
      return a.timestamp - b.timestamp;
    });
    for (var i = 0; i < sorted.length; i++) {
      var obj = sorted[i];
      console.log(obj);
      rez.push(parseInt(obj.value));
    }
    this.setState({
      data: rez
    })
    console.log(this.state.data);
  }

  componentDidMount() {
    this.getData();
  }

  render() {
    const contentInset = { top: 20, bottom: 20 }
    return (
      <View style={{ flexDirection: "column" }}>
        <View style={{ height: 200, flexDirection: 'row' }}>
          <YAxis
            data={this.state.data}
            svg={{
              fill: 'grey',
              fontSize: 10,
            }}
            contentInset={contentInset}
            numberOfTicks={10}
            formatLabel={value => `${value}`}
          />
          <LineChart
            contentInset={contentInset}
            style={{ flex: 1, marginLeft: 16 }}
            data={this.state.data}
            svg={{ stroke: 'rgb(134, 65, 244)' }}
          >
            <Grid />
          </LineChart>
        </View>
        <View style={{ margin: 24 }}>
          <Button
            title={'Reload Data'}
            style={styles.input}
            onPress={this.getData.bind(this)}
          />
        </View>
      </View>
    );
  }
}

class SettingsComponent extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Button
          title={'Logout'}
          style={styles.input}
          onPress={this.onLogout.bind(this)}
        />
      </View>
    );
  }

  onLogout = async () => {
    AsyncStorage.removeItem('token');
    this.props.navigation.navigate('Auth');
  }
}


const ListViewStack = createStackNavigator({
  Home: MainListComponent,
  Details: GlucoseEntryDetailsComponent,
});

const TabNavigator = createMaterialBottomTabNavigator({
  Glucose: {
    screen: ListViewStack,
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


const AuthStack = createStackNavigator({ SignIn: SignInComponent });

export default createAppContainer(createSwitchNavigator(
  {
    AuthLoading: AuthLoadingScreen,
    App: TabNavigator,
    Auth: AuthStack,
  },
  {
    initialRouteName: 'AuthLoading',
  }
));

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
  input: {
    width: 200,
    height: 44,
    padding: 10,
    borderWidth: 1,
    borderColor: 'black',
    marginBottom: 10,
  },
  title: {
    fontSize: 24,
    margin: 10
  }
});
