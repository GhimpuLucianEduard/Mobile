import React, { Component } from 'react'
import { TextInput, FlatList, StyleSheet, Text, View, ActivityIndicator, TouchableHighlight, AsyncStorage } from 'react-native';
import { Card, ListItem, Button } from 'react-native-elements'
import Icon from 'react-native-vector-icons/FontAwesome';
import { withNavigation } from 'react-navigation';
import axios from 'axios'

export default class SignInComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: '',
        };
    }

    onLogin() {
        const { username, password } = this.state;

        this.setState({
            isLoading: true
        });
        let reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/ ;
        if(reg.test(username) === false || password.length < 7) {
            alert("Username invalid or password invalid!");
            return;
        }

        instance.post('/user/login', 
            {
                email: username,
                password: password
            })
            .then((response) => {
                console.log(response.data.token);
                AsyncStorage.setItem("token", response.data.token);
                this.props.navigation.navigate('App');
            })
            .catch(function (error) {
                console.log(error);
            })
            .then(function () {
                alert("Username invalid or password invalid!");
                console.log("final");
            });
    }

    render() {
        return (
            <View style={styles.container}>
                <TextInput
                    value={this.state.username}
                    onChangeText={(username) => this.setState({ username })}
                    placeholder={'Username'}
                    style={styles.input}
                />
                <TextInput
                    value={this.state.password}
                    onChangeText={(password) => this.setState({ password })}
                    placeholder={'Password'}
                    secureTextEntry={true}
                    style={styles.input}
                />

                <Button
                    title={'Login'}
                    style={styles.input}
                    onPress={this.onLogin.bind(this)}
                />
            </View>
        );
    }
}

const instance = axios.create({
    baseURL: 'http://10.0.2.2:329',
    timeout: 1000
});

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ecf0f1',
    },
    input: {
        width: 200,
        height: 44,
        padding: 10,
        borderWidth: 1,
        borderColor: 'black',
        marginBottom: 10,
    },
});