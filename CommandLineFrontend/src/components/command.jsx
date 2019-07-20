import React, {Component} from 'react';

export default class Command extends Component {
    state = {
        commandPromise: null,
        response: null,
    };

    componentDidMount() {
        this.executeCommand(this.props.commandArgs)
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({response: jsonResponse.response});
                this.props.callback();
            });
    }

    executeCommand = (commandArgs) => {
        return window.fetch('/command', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "commandArgs": commandArgs
            })
        })
    };


    render() {
        return (
            <div>
                <div className="input-line line">
                    <div className="prompt">[rapon@website] #</div>
                    <div>
                        <input className="cmdline" readOnly value={this.props.commandArgs}/>
                    </div>

                </div>
                <p className="preformatted">{ this.state.response }</p>
            </div>
        );
    };
}