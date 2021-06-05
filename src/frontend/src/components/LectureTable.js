import React from "react";

class LectureTable extends React.Component {
    render() {
        return (
            <table>
                <tbody>

                    <tr>
                        <th>Kategoria</th>
                        <th>Nazwa</th>
                        <th>Czas rozpoczęcia</th>
                        <th>Koniec</th>
                        <th>Dostępność</th>
                        <th colSpan={2}>Zarządzaj</th>
                    </tr>

                    {this.props.lectures.map((lecture, index) => {
                        return (
                            <tr key={index}>
                                <td>{lecture.category}</td>
                                <td>{lecture.name}</td>
                                <td>{lecture.startTime}</td>
                                <td>{lecture.endTime}</td>
                                <td>{lecture.size + '/' + lecture.maxSize}</td>
                                <td className={'buttonTable'}>
                                    <button onClick={event => this.props.onClick(event, lecture, 'POST')}>
                                        Zarezerwuj
                                    </button>
                                </td>
                                <td className={'buttonTable'}>
                                    <button onClick={event => this.props.onClick(event, lecture, 'DELETE')}>
                                        Anuluj
                                    </button>
                                </td>
                            </tr>
                        );
                    })}

                </tbody>
            </table>
        );
    }
}

export default LectureTable;