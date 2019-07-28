import React from "react";

export default class Banner extends React.Component {

    // Choose different base values from the backend.
    state = {
        releaseVersion: 'vY',
        tsRelease: '0001-01-01T00:00:00Z'
    };

    componentDidMount() {
        // Get version from the backend.
        fetch('version')
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong');
                }
            })
            .then((data) => {
                this.setState({
                    releaseVersion: data.releaseVersion,
                    tsRelease: data.tsRelease
                })
            })
            .catch((error) => {
                console.log('An error occured when retrieving the version.');
            });
    };

    render() {
        return (
            <div>
                <pre>
                    {` ______ ______ ______ ______ ______ ______ ______ ______ ______ 
|______|______|______|______|______|______|______|______|______|
______   _              ___                       ______        
| ___ \\ (_)            / _ \\                      \\ \\ \\ \\ 
| |_/ /  _  ___ ___   / /_\\ \\  _ __   ___  _ __    \\ \\ \\ \\
|    /  | |/ __/ _ \\  |  _  | | '_ \\ / _ \\| '_ \\    > > > > 
| |\\ \\  | | (_| (_) | | | | | | |_) | (_) | | | |  / / / /    
\\_| \\_| |_|\\___\\___/  \\_| |_/ | .__/ \\___/|_| |_| /_/_/_/
 ______ ______ ______ ______ ______  
|______|______|______|______|______| ` + this.state.releaseVersion + ' on ' + this.state.tsRelease}
                </pre>
                <br/>
                <pre>If this is your first time here, typ 'help'!</pre>
            </div>
        );
    }
}
