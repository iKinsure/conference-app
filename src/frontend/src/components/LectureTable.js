import React from "react";

class LectureTable extends React.Component {
    render() {
        return (
            <table>
                <tbody>

                    <tr>
                        <th>Category</th>
                        <th>Name</th>
                        <th>Start time</th>
                        <th>End time</th>
                    </tr>

                    {this.props.lectures.map((lecture, index) => {
                        return (
                            <tr key={index}>
                                <td>{lecture.category}</td>
                                <td>{lecture.name}</td>
                                <td>{lecture.startTime}</td>
                                <td>{lecture.endTime}</td>
                                <td className={'buttonTable'}>
                                    <button onClick={event => this.props.onClick(event, lecture, 'POST')}>
                                        Reserve
                                    </button>
                                </td>
                                <td className={'buttonTable'}>
                                    <button onClick={event => this.props.onClick(event, lecture, 'DELETE')}>
                                        Cancel
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