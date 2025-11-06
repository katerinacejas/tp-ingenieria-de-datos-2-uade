import React from 'react'
import './DataTable.css'

export default function DataTable({ columns, data }) {
    return (
        <div className="card">
            <table className="table">
                <thead>
                    <tr>{columns.map(c => <th key={c.key}>{c.header}</th>)}</tr>
                </thead>
                <tbody>
                    {data.map((row, i) => (
                        <tr key={i}>
                            {columns.map(c => <td key={c.key}>{c.render ? c.render(row[c.key], row) : row[c.key]}</td>)}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}