import React from "react";

/**
 * Class for communicating with the backend.
 * This class calls the proper callbacks when needed.
 */
export default class MyWebSocketComponent extends React.Component {

    componentDidMount() {
        this.setState({
            connected: false
        });
        this.initialize();
    }

    determineURL = () => {
        // Added this for heroku debugging purposes.
        console.log(window.location);

        // Because of issues with local development proxy stuff, we need to manually put this port here.
        let port;
        if (window.location.hostname === 'localhost') {
            port = 8080;
        } else {
            port = window.location.port;
        }

        let protocol;
        if (window.location.protocol.includes('https')) {
            protocol = "wss";
        } else {
            protocol = "ws";
        }

        return protocol + "://" + window.location.hostname + ":" + port + "/webSocket";
    };

    initialize = () => {
        let webSocket = new WebSocket(this.determineURL());

        webSocket.onopen = this.handleWsOnOpen;
        webSocket.onmessage = this.handleWsOnMessage;
        webSocket.onclose = this.handleWsOnClose;
        webSocket.onerror = this.handleWsOnError;

        this.setState({
            webSocket: webSocket,
        });
    };

    handleWsOnOpen = (message) => {
        this.setState({
            connected: true
        });

        this.props.onConnectedCallback();
    };

    handleWsOnMessage = (commandOutput) => {
        let data = JSON.parse(commandOutput.data);
        let message = data.message;
        let commandStatus = data.commandStatus;

        this.props.onMessageCallback(message);

        if (commandStatus === 'FINAL') {
            this.props.onFinalCallback();
        }
    };

    handleWsOnClose = (message) => {
        this.props.onDisconnectedCallback();

        // The onClose method will be called every time after initialize failed.
        // Thus we can just retry after 1 second, meaning it will be an infinite loop until it connects.
        this.setState({
            connected: false
        });
        setTimeout(this.initialize, 1000);
    };

    handleWsOnError = (message) => {
        console.log("Error happened.");
        console.log(message);
    };

    sendWsMessage = (message) => {
        this.state.webSocket.send(message);
    };

    render() {
        return (null);
    }
}
