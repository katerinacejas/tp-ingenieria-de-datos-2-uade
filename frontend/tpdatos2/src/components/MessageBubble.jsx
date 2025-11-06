import React from 'react'
import './MessageBubble.css'

export default function MessageBubble({ me = false, name, text, time }) {
    return (
        <div className={`bubble-row ${me ? 'me' : ''}`}>
            {!me && <div className="bubble-name">{name}</div>}
            <div className="bubble">
                <div className="bubble-text">{text}</div>
                <div className="bubble-time">{time}</div>
            </div>
        </div>
    )
}
