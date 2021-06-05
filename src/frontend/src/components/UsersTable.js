import React from "react";

class UsersTable extends React.Component {
    render() {
        return (
            <table>
                <tbody>
                <tr>
                    <th>Login</th>
                    <th>Email</th>
                    <th>Liczba zarezerwowanych prelekcji</th>
                </tr>
                {this.props.users.map((user, index) => {
                    return (
                        <tr key={index}>
                            <td>{user.login}</td>
                            <td>{user.email}</td>
                            <td>{user.lectures.length}</td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
        );
    }
}

export default UsersTable;