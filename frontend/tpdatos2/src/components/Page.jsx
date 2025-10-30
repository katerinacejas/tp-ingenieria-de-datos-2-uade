import React from 'react'
import './Page.css'

export default function Page({ title, actions, children }) {
    return (
        <div className="page">
            <div className="page-head">
                <h1>{title}</h1>
                <div className="page-actions">{actions}</div>
            </div>
            {children}
        </div>
    )
}