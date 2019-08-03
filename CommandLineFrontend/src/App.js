import './App.css';
import React from 'react';
import MyWebSocketComponent from "./components/MyWebSocketComponent";
import CommandOutput from "./components/CommandOutput";
import Banner from "./components/Banner";

class App extends React.Component {
    state = {
        commands: [],
        isMostRecentCommandFinished: true,
        isConnectionEstablished: false,
        inputValue: ''
    };

    sendMessage = (input, fileBase64) => {
        // Update state with new command.
        this.state.commands.push({
            commandArg: input,
            output: ""
        });
        this.setState({
            isMostRecentCommandFinished: false,
            commands: this.state.commands
        });

        // Send the message.
        this.myWebSocketComponent.sendWsMessage(JSON.stringify({commandArg: input, fileBase64: fileBase64}));

        // Clear the input
        this.setState({
            inputValue: '',
        })

    };

    onMessage = (message) => {
        // Update the output of the most recent command.
        this.setState(state => {
            const commands = state.commands;
            const mostRecentCommand = commands[commands.length - 1];
            mostRecentCommand.output = mostRecentCommand.output + message + "\n";

            return {
                commands,
            };
        });
    };

    onFinal = (message) => {
        if (message) {
            this.onMessage(message);
        }
        this.setState({
            isMostRecentCommandFinished: true
        });
    };

    onConnected = () => {
        this.setState({
            isConnectionEstablished: true
        });
    };

    onDisconnected = () => {
        this.setState({
            isConnectionEstablished: false
        });
    };

    handleKeyDown = (e) => {
        // Possibilities for more cool stuff:
        // https://stackoverflow.com/questions/5203407/how-to-detect-if-multiple-keys-are-pressed-at-once-using-javascript
        if (e.key === 'Enter') {
            // If we typed the "upload-plugin" command, we first upload a file and then send the command.
            // Else, we just execute the command.
            if (this.state.inputValue.startsWith('upload-plugin')) {
                // The command will be executed whenever a file has been uploaded.
                this.openFileUpload();
            } else {
                this.sendMessage(this.state.inputValue, null);
            }
        }
    };

    openFileUpload = () => {
        this.myInput.click();
    };

    setUploadedFile = (uploadedFile) => {
        let reader = new FileReader();
        reader.readAsDataURL(uploadedFile.target.files[0]);
        reader.onload = () => {
            this.sendMessage(this.state.inputValue, reader.result);
        };
        reader.onerror = function (error) {
            console.log('Error: ', error);
        };

    };

    render() {
        let inputDiv;

        if (!this.state.isConnectionEstablished) {
            inputDiv = <div id="reconnect">No connection to the server. Trying to connect to the server...</div>
        } else if (this.state.isMostRecentCommandFinished) {
            inputDiv =
                <div id="input-line" className="input-line">
                    <div className="prompt">[rapon@website] #</div>
                    <div className="cmdline-wrapper"><input className="cmdline" autoFocus onKeyDown={this.handleKeyDown}
                                                            value={this.state.inputValue}
                                                            onChange={evt => this.updateInputValue(evt)}/></div>
                </div>
        } else {
            inputDiv = <div id="input-line" className="input-line"/>
        }

        return (
            <div id="container">
                <MyWebSocketComponent ref={this.setMyWebSocketComponent}
                                      onMessageCallback={this.onMessage}
                                      onFinalCallback={this.onFinal}
                                      onConnectedCallback={this.onConnected}
                                      onDisconnectedCallback={this.onDisconnected}/>

                <input id="fileUploader" type="file" ref={this.setMyInput}
                       onChange={file => this.setUploadedFile(file)}
                       style={{display: 'none'}}/>

                <output>
                    <Banner/>
                    <br/>
                </output>
                <output>
                    {this.state.commands.map((command, index) => (
                        <CommandOutput key={index} command={command}/>
                    ))}
                </output>

                {inputDiv}
            </div>
        );
    }

    setMyWebSocketComponent = (myWebSocketComponent) => {
        this.myWebSocketComponent = myWebSocketComponent;
    };

    setMyInput = (myInput) => {
        this.myInput = myInput;
    };

    updateInputValue(evt) {
        this.setState({inputValue: evt.target.value});
    };
}

export default App;
