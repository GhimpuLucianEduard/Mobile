import React, { Component } from 'react'
import { FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';
import axios from 'axios'
// import { axios } from "axios"

class ListViewComponent extends Component {

    state = {
        data: [],
        isLoading: true,
        listFlag: 0
    }

    componentDidMount() {

        instance.get('/glucose')
            .then((response) => {
                this.setState({
                    data: response.data,
                    isLoading: false
                });
            })
            .catch(function (error) {
                // handle error
                console.log(error);
            })
            .then(function () {
                console.log("final");
            });
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
            </View>
        )
    }

    _onPress = (item) => {
        console.log(item);
        var array = [...this.state.data];
        var index = array.indexOf(item);
        if (index !== -1) {
            array.splice(index, 1);
            this.setState({ data: array });
        } else {
            console.log("NU MERGE")
        }
    }


    returnData = (entryJson) => {
        console.log(entryJson);
        var entries = this.state.data;
        var updated = false;
        for (var i in entries) {
            if (entries[i]._id == entryJson._id) {
                entries[i] = entryJson;
                updated = true;
                break; //Stop this loop, we found it!
            }
        }
        if (!updated) {
            entries.push(entryJson)
        }

        this.setState({
            data: entries,
            listFlag: 1
        });
    }
}

export default withNavigation(ListViewComponent);

const instance = axios.create({
    baseURL: 'http://10.0.2.2:329',
    timeout: 1000,
    headers: { 'Authorization': 'Baerer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imx1Y2ZzZGZzZGlhbjIzQGEuY29tIiwidXNlcklkIjoiNWMyN2NlMmZmZGRjMGM0Mzc0ZDdhNGVlIiwiaWF0IjoxNTQ2MTE0NDI5LCJleHAiOjE2MzI1MTQ0Mjl9.g-kzQtnm3fnflXMsAbVFnrx4RajpV8wviareTsKPQw4' }
});

function getAfterMeal(isAfter) {
    return isAfter ? "After Meal" : "Before Meal";
}

function getDate(timestamp) {

    var date = new Date(timestamp);
    return date.getFullYear() + "-"
    + (date.getMonth()+1)  + "-" 
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
})