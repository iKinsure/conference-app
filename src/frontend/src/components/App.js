import React from "react";
import LectureTable from "./LectureTable";
import UsersTable from "./UsersTable";

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      mode: 0,
      login: '',
      email: '',
      users: [],
      lectures: [],
      userLectures: [],
    };
    this.login = '';
  }

  onClickUpdate() {
    fetch(`api/users`, {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      method: 'PUT',
      body: JSON.stringify({login: this.state.login, email: this.state.email}),
    }).then(res => res.json()).then(this.responseMessage);
  }

  onClickReservation(event, lecture, method) {
    event.preventDefault();
    fetch(`api/lectures/${lecture.id}`, {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      method: method,
      body: JSON.stringify({login: this.state.login, email: this.state.email}),
    }).then(res => res.json()).then(this.responseMessage);
  }

  responseMessage(obj) {
    let msg = '';
    msg += obj.login ? obj.login + '\n' : '';
    msg += obj.email ? obj.email + '\n' : '';
    msg += obj.message ? obj.message + '\n' : '';
    alert(msg);
  }

  updateView() {
    switch (this.state.mode) {
      case 1:
        return (
            <LectureTable
                lectures={this.state.lectures}
                onClick={(event, lecture, method) => this.onClickReservation(event, lecture, method)}
            />
        );
      case 2:
        return <UsersTable users={this.state.users}/>;
      case 3:
        return (
            <div>
              <h3>{this.login}</h3>
              <LectureTable
                  lectures={this.state.userLectures}
                  onClick={(event, lecture, method) => this.onClickReservation(event, lecture, method)}
              />
            </div>
        );
      default:
        return null;
    }
  }

  updateMode(mode) {
    switch (mode) {
      case 1:
        fetch(`api/lectures`)
            .then(res => res.json())
            .then(json => this.setState({lectures: json}));
        break
      case 2:
        fetch(`api/users`)
            .then(res => res.json())
            .then(json => this.setState({users: json}));
        break;
      case 3:
        this.login = this.state.login;
        fetch(`api/users/${this.state.login}/lectures`)
            .then(res => {
              if (!res.ok) {
                throw new Error();
              }
              return res;
            })
            .then(res => res.json())
            .then(json => json ?? [])
            .then(json => this.setState({userLectures: json}))
            .catch(e => {
              mode = 0;
              this.setState({userLectures: []});
            });
        break;
      case 4:
        this.onClickUpdate();
        mode = 0;
        break;
      default:
        mode = 0;
    }
    this.setState({mode: mode});
  }

  render() {
    return (
      <div>

        <nav>
          <ul>
            <li>
              <label>Login</label>
              <input
                  type={'text'}
                  value={this.state.login}
                  onChange={e => this.setState({login: e.target.value})}
              />
            </li>
            <li>
              <label>Email</label>
              <input
                  type={'text'}
                  value={this.state.email}
                  onChange={e => this.setState({email: e.target.value})}
              />
            </li>
            <li onClick={() => this.updateMode(1)}>Lectures</li>
            <li onClick={() => this.updateMode(2)}>All users</li>
            <li onClick={() => this.updateMode(3)}>User lectures</li>
            <li onClick={() => this.updateMode(4)}>Change email</li>
          </ul>
        </nav>

        <div>
          {this.updateView()}
        </div>

      </div>
    );
  }
}

export default App;
