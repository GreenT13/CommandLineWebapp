import React, {Component} from 'react';
import './App.css';
import Command from './components/command'
import 'whatwg-fetch'


class App extends Component {
    state = {
        commands: [],
        inputValue: '',
        isMostRecentCommandFinished: true
    };

    /**
     * Handle the event when a key is pressed.
     * @param e
     */
    handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            console.log('Entered!');
            this.state.commands.push(this.state.inputValue);
            this.setState({
                commands: this.state.commands,
                inputValue: '',
                isMostRecentCommandFinished: false
            })
        }
    };

    setMostRecentCommandFinished = () => {
        this.setState({isMostRecentCommandFinished: true})
    };

    render() {
        let inputDiv;
        if (this.state.isMostRecentCommandFinished) {
            inputDiv =
                <div id="input-line" className="input-line">
                    <div className="prompt">[rapon@website] #</div>
                    <div><input className="cmdline" autoFocus onKeyDown={this.handleKeyDown}
                                value={this.state.inputValue} onChange={evt => this.updateInputValue(evt)}/></div>
                </div>
        } else {
            inputDiv = <div id="input-line" className="input-line"/>
        }
        return (
            <div id="container">
                <output>
                    <pre>{`  .   ____          _            __ _ _
 /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\
( ( )\\___ | '_ | '_| | '_ \\/ _\` | \\ \\ \\ \\
 \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.6.RELEASE)`}
                    </pre>
                </output>
                <output>
                    {this.state.commands.map((command, index) => (
                        <Command key={index} commandArgs={command} callback={this.setMostRecentCommandFinished}/>
                    ))}
                </output>
                {inputDiv}
            </div>
        );
    }

    updateInputValue(evt) {
        this.setState({inputValue: evt.target.value});
    }
}

export default App;
