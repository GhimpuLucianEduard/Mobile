import React, { Component } from 'react'
import { NetInfo, FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';
import axios from 'axios'
import ActionButton from 'react-native-action-button';
// import { axios } from "axios"

class ListViewComponent extends Component {

    state = {
        data: [],
        isLoading: true,
        listFlag: 0
    }

    _fetchData = async () => {

        var token = await AsyncStorage.getItem('token');
        var listOfEntries = await AsyncStorage.getItem('entries');
        const parsed = JSON.parse(listOfEntries);
        const shouldFetchData = parsed === undefined || parsed.length == 0;
        if (!shouldFetchData) {
            console.log("---------------------")
            console.log(parsed);
            this.setState({
                data: parsed,
                isLoading: false
            });
        } else {
            alert("in req");
            instance.get('/glucose', {
                headers: {
                    Authorization: `Baerer ${token}`
                }
            })
                .then((response) => {
                    this.setState({
                        data: response.data,
                        isLoading: false
                    });
                    console.log("---------------------------------");
                    console.log(response.data);
                    AsyncStorage.setItem('entries', JSON.stringify(response.data));
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    console.log("final");
                });
        }
    }

    componentDidMount() {
        this._fetchData();
        NetInfo.isConnected.addEventListener('connectionChange', this.handleConnectivityChange);
    }


    render() {

        if (this.state.isLoading) {
            return (
                <View style={{ flex: 1, padding: 20 }}>
                    <ActivityIndicator />
                </View>
            )
        }

        return (
            <View style={styles.container}>
                <FlatList
                    extraData={this.state.listFlag}
                    keyExtractor={(item) => item._id}
                    data={this.state.data}
                    renderItem={({ item }) => (
                        <Card flexDirection='column' style={styles.card}>
                            <View style={{ flex: 1, flexDirection: "row" }}>
                                <Text style={{ flex: 1, fontSize: 32 }}>{item.value}</Text>
                                <TouchableHighlight onPress={() => this._onPress(item)}>
                                    <Icon style={{ alignSelf: "flex-end" }} name="remove" size={32} />
                                </TouchableHighlight>
                                <TouchableHighlight onPress={() => { this.props.navigation.navigate('Details', { entry: item, isSave: false, returnData: this.returnData.bind(this) }) }}>
                                    <Icon style={{ alignSelf: "flex-end" }} name="edit" size={32} />
                                </TouchableHighlight>
                            </View>
                            <View style={{ flex: 1, flexDirection: "row" }}>
                                <Text style={{ flex: 1 }}>{item.date}</Text>
                                <Text>{getDate(item.timestamp)}</Text>
                                <Text style={{ alignSelf: "flex-end" }}>{getAfterMeal(item.afterMeal)}</Text>
                            </View>
                        </Card>
                    )}
                />
                <ActionButton buttonColor="rgba(231,76,60,1)" onPress={() => { this.props.navigation.navigate('Details', { entry: {}, isSave: true, returnData: this.returnData.bind(this) }) }}>
                </ActionButton>
            </View>
        )
    }

    componentWillUnmount() {
        NetInfo.isConnected.removeEventListener('connectionChange', this.handleConnectivityChange);
    }

    handleConnectivityChange = async (isConnected) => {
        if (isConnected) {
            var token = await AsyncStorage.getItem('token');
            var listOfEntries = await AsyncStorage.getItem('entries');
            const parsed = JSON.parse(listOfEntries);
            instance.post('/glucose/sync',
                parsed
                , {
                    headers: {
                        Authorization: `Baerer ${token}`
                    }
                })
                .then((response) => {
                    alert("data sync completed!");
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    console.log("final");
                });
            console.log("online");
        } else {
            console.log("offline");
        }
    };

    _onPress = async (item) => {
        console.log(item);
        var array = [...this.state.data];
        var index = array.indexOf(item);
        if (index !== -1) {
            array.splice(index, 1);
            this.setState({ data: array });
        } else {
            console.log("NU MERGE")
            return;
        }

        const entires = await AsyncStorage.getItem('entries');
        const parsed = JSON.parse(entires);
        for (var i = 0; i < parsed.length; i++) {
            var obj = parsed[i];
            if (obj._id === item._id) {
                parsed.splice(i, 1);
            }
        }
        var token = await AsyncStorage.getItem('token');

        instance.delete(`/glucose/${item._id}`, {
            headers: {
                Authorization: `Baerer ${token}`
            }
        })
            .then((response) => {
                console.log("delete req ok")
            })
            .catch(function (error) {
                console.log("error");
            })
            .then(function () {
                console.log("final");
            });

        // alert(this.state.data.length);
        // await AsyncStorage.setItem('entries', JSON.stringify(this.state.data));
        await AsyncStorage.setItem('entries', JSON.stringify(parsed));
    }


    returnData = async (entryJson) => {

        console.log(entryJson);
        var entries = this.state.data;
        var updated = false;
        for (var i in entries) {
            if (entries[i]._id == entryJson._id) {
                entries[i] = entryJson;
                updated = true;
                break;
            }
        }
        var token = await AsyncStorage.getItem('token');
        if (!updated) {
            console.log("---ADD---");
            const rnd = Math.floor(Math.random() * 1000) + 1;
            entryJson._id = `${entryJson.timestamp}${entryJson.value}${rnd}`;
            console.log(entryJson);
            entries.push(entryJson)

            instance.post('/glucose',
                entryJson
                , {
                    headers: {
                        Authorization: `Baerer ${token}`
                    }
                })
                .then((response) => {
                    console.log("add req ok")
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    console.log("final");
                });

        } else {

            instance.patch('/glucose',
                entryJson
                , {
                    headers: {
                        Authorization: `Baerer ${token}`
                    }
                })
                .then((response) => {
                    console.log("update req ok")
                })
                .catch(function (error) {
                    console.log("error");
                })
                .then(function () {
                    console.log("final");
                });
            console.log("===UPDATE====");
            console.log(entryJson);
        }


        this.setState({
            data: entries,
            listFlag: Math.floor(Math.random() * 1000) + 1
        });

        console.log("=----here----=");
        await AsyncStorage.setItem('entries', JSON.stringify(this.state.data));
    }
}

export default withNavigation(ListViewComponent);

const instance = axios.create({
    baseURL: 'http://10.0.2.2:329',
    timeout: 1000,
});

function getAfterMeal(isAfter) {
    return isAfter ? "After Meal" : "Before Meal";
}

function getDate(timestamp) {

    var date = new Date(timestamp);
    return date.getFullYear() + "-"
        + (date.getMonth() + 1) + "-"
        + date.getDate() + "  "
        + date.getHours() + ":"
        + date.getMinutes();
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 22
    },
    card: {
        padding: 10,
        height: 72,
    },
    actionButtonIcon: {
        fontSize: 20,
        height: 22,
        color: 'white',
    }
})