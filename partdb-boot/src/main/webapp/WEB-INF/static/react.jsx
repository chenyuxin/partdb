/*
import React from 'react';
import { Button } from '@material-ui/core';
import React from 'react';

function App() {
    return <Button color="primary">Hello World</Button>;
}
*/



/**
欢迎组件
 */
class Welcome extends React.Component {
  render() {
    return <h2>react:Hello World! {this.props.name}</h2>;
  }
}

/**
时钟组件
 */
class Clock extends React.Component {
  constructor(props) {
    super(props);
    this.state = {date: new Date()};
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      1000
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick = () => {
    this.setState({
      date: new Date()
    });
  }
  

  render() {
    return (
      <div>
        <h1>Hello, world!</h1>
        <h2>It is {this.state.date.toLocaleTimeString()}</h2>
      </div>
    );
  }
}


const root = ReactDOM.createRoot(document.getElementById('root'));
//const element = <Welcome name="Part Data Base v0.0.1" />;
const element = <Clock />;
root.render(element);



