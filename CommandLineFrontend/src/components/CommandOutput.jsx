import React from "react";

export default class CommandOutput extends React.Component {

    render() {
        return (
            <div>
                <div className="input-line line">
                    <div className="prompt">[rapon@website] #</div>
                    <div className="cmdline-wrapper">
                        <input className="cmdline" readOnly value={this.props.command.commandArg}/>
                    </div>

                </div>
                <p className="preformatted">{ this.props.command.output }</p>
            </div>
        );
    }
}
