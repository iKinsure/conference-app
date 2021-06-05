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
    }).then(res => res.json())
        .then(this.responseMessage);
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
        return (
            <div>
              <h3>Konferencja IT</h3>
              <p>1.	Konferencja trwa 1 dzień: 1 czerwca 2021. </p>
              <p>2.	Rozpoczyna się o godzinie 10:00 a kończy o godzinie 15:45.</p>
              <p>3.	Każda prelekcja trwa 1h 45m (15 minut to przerwa na kawę):</p>
              <p>- pierwsza prelekcja rozpoczyna się o 10:00 i trwa do 11:45. </p>
              <p>- druga rozpoczyna się o 12:00 i kończy o 13:45</p>
              <p>- trzecia rozpoczyna się o 14:00 i kończy o 15:45</p>
              <p>4.	W ramach konferencji obsługiwane są 3 różne ścieżki tematyczne prowadzone równolegle.
                Jeśli użytkownik zapisze się w danej ścieżce na daną godzinę, to nie może już
                uczęszczać w tym okresie w innej ścieżce, natomiast o innej godzinie najbardziej
                może wybrać inną ścieżkę. </p>
              <p>5.	Każda prelekcja może pomieścić maksymalnie 5 słuchaczy. </p>
            </div>
        );
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
            <li className={'menuItem'} onClick={() => this.updateMode(0)}>Info</li>
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
            <li className={'menuItem'} onClick={() => this.updateMode(1)}>Plan konferencji</li>
            <li className={'menuItem'} onClick={() => this.updateMode(2)}>Użytkownicy</li>
            <li className={'menuItem'} onClick={() => this.updateMode(3)}>Rezerwacje użytkownika</li>
            <li className={'menuItem'} onClick={() => this.updateMode(4)}>Zmiana emaila</li>
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
