import React from 'react'
import './ConversationList.css'
import { FaCommentDots, FaUsers } from 'react-icons/fa'

export default function ConversationList({ items, activeId, onSelect }) {
    return (
        <div className="conv-list">
            {items.map(c => (
                <button
                    key={c.id}
                    className={`conv-item ${activeId === c.id ? 'active' : ''}`}
                    onClick={() => onSelect(c.id)}
                >
                    <div className="conv-icon">{c.tipo === 'grupo' ? <FaUsers /> : <FaCommentDots />}</div>
                    <div className="conv-main">
                        <div className="conv-name">{c.nombre}</div>
                        <div className="conv-last">{c.ultimo}</div>
                    </div>
                </button>
            ))}
            {items.length === 0 && <div className="conv-empty">Sin conversaciones</div>}
        </div>
    )
}
