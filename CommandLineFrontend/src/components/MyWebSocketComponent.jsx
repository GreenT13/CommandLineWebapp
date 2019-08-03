import React from 'react';
/**
 * Class for communicating with the backend.
 * This class calls the proper callbacks when needed.
 */
export default class MyWebSocketComponent extends React.Component {

    componentDidMount() {
        this.initialize();
    }

    determineURL = () => {
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
        this.setState({
            connected: false
        });

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
        console.log({function: 'handleWsOnOpen', message: message});

        this.setState({
            connected: true
        });
        this.props.onConnectedCallback();
    };

    handleWsOnMessage = (commandOutput) => {
        let data = JSON.parse(commandOutput.data);
        let message = data.message;
        let commandStatus = data.commandStatus;

        if (commandStatus === 'MESSAGE') {
            this.props.onMessageCallback(message);
        } else if (commandStatus === 'FINAL') {
            this.props.onFinalCallback(message);
        }
    };

    handleWsOnClose = (message) => {
        console.log({function: 'handleWsOnClose', message: message});
        this.props.onDisconnectedCallback();

        // The onClose method will be called every time after initialize failed.
        // Thus we can just retry after 1 second, meaning it will be an infinite loop until it connects.
        this.setState({
            connected: false
        });
        setTimeout(this.initialize, 1000);
    };

    handleWsOnError = (message) => {
        console.log({function: 'handleWsOnError', message: message});
    };

    sendWsMessage = (message) => {
        this.state.webSocket.send(message);
    };

    render() {
        // TODO: Add the file input type here, and only trigger upload-file from this place (so app isn't clouded).
        // Or make some in-between class that handles it. Maybe rethink the structure anyway?
        return null;
    }
}
