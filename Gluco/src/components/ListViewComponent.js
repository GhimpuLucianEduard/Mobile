import React, { Component } from 'react'
import { FlatList, StyleSheet, Text, View, ActivityIndicator } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
// import { axios } from "axios"

export default class ListViewComponent extends Component {

    state = {
        data: [],
        isLoading: true
    }

    componentDidMount() {
        return fetch(
            'http://10.0.2.2:329/glucose', {
                method: 'GET',
                headers: {
                    Authorization: '"Baerer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imx1Y2ZzZGZzZGlhbjIzQGEuY29tIiwidXNlcklkIjoiNWMyN2NlMmZmZGRjMGM0Mzc0ZDdhNGVlIiwiaWF0IjoxNTQ2MTE0NDI5LCJleHAiOjE2MzI1MTQ0Mjl9.g-kzQtnm3fnflXMsAbVFnrx4RajpV8wviareTsKPQw4',
                }
            })
            .then((response) => response.json())
            .then((responseJson) => {

                console.log(responseJson);
                this.setState({
                    data: responseJson,
                    isLoading: false
                });
            })
            .catch((error) => {
                console.error(error);
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
                    keyExtractor={(item) => item._id}
                    data={this.state.data}
                    renderItem={({ item }) => (
                        <Card flexDirection='column' style={styles.card}>
                            <View style={{ flex: 1, flexDirection: "row" }}>
                                <Text style={{ flex: 1, fontSize: 32 }}>{item.value}</Text>
                                <Icon style={{ alignSelf: "flex-end" }} name="edit" size={32} />
                                <Icon style={{ alignSelf: "flex-end" }} name="remove" size={32} />
                            </View>
                            <View style={{ flex: 1, flexDirection: "row" }}>
                                <Text style={{ flex: 1 }}>{item.date}</Text>
                                <Text style={{ alignSelf: "flex-end" }}>{getAfterMeal(item.afterMeal)}</Text>
                            </View>
                        </Card>
                    )}
                />
            </View>
        )
    }
}

function getAfterMeal(isAfter) {
    return isAfter ? "After Meal" : "Before Meal";
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