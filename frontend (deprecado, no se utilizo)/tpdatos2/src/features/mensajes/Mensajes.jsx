import React, { useEffect, useMemo, useRef, useState } from 'react'
import './Mensajes.css'
import Page from '../../components/Page.jsx'
import ConversationList from '../../components/ConversationList.jsx'
import MessageBubble from '../../components/MessageBubble.jsx'
import { listConversaciones, listMensajes, enviarMensaje, fmt } from './mensajesApi.js'

export default function Mensajes(){
  const [tab, setTab] = useState('privado') // 'privado' | 'grupo'
  const [convs, setConvs] = useState([])
  const [active, setActive] = useState(null) // id conv
  const [msgs, setMsgs] = useState([])
  const [text, setText] = useState('')
  const boxRef = useRef(null)

  useEffect(()=>{ // cargar conversaciones según tab
    (async()=>{
      const data = await listConversaciones(tab)
      setConvs(data)
      setActive(data[0]?.id || null)
    })()
  },[tab])

  useEffect(()=>{ // cargar mensajes de la activa
    if(!active) { setMsgs([]); return }
    (async()=>{
      setMsgs(await listMensajes(active))
      // scrollear al final
      setTimeout(()=> boxRef.current?.scrollTo({ top: 1e9, behavior: 'smooth' }), 0)
    })()
  },[active])

  const convActual = useMemo(()=> convs.find(c=>c.id===active) || null, [convs, active])

  const send = async (e) => {
    e.preventDefault()
    const t = text.trim()
    if(!t) return
    const nuevo = await enviarMensaje(active, t)
    setMsgs(m => [...m, nuevo])
    setText('')
    setTimeout(()=> boxRef.current?.scrollTo({ top: 1e9, behavior: 'smooth' }), 10)
  }

  return (
    <Page title="Mensajes" actions={
      <div className="tabs">
        <button className={`tab ${tab==='privado'?'active':''}`} onClick={()=>setTab('privado')}>Privados</button>
        <button className={`tab ${tab==='grupo'?'active':''}`} onClick={()=>setTab('grupo')}>Grupos</button>
      </div>
    }>
      <div className="msg-layout">
        <aside className="msg-left card">
          <ConversationList
            items={convs}
            activeId={active}
            onSelect={setActive}
          />
        </aside>

        <section className="msg-right card">
          {convActual ? (
            <>
              <header className="msg-header">
                <div className="h-title">
                  <div className="circle">{convActual.nombre.slice(0,1)}</div>
                  <div>
                    <div className="h-name">{convActual.nombre}</div>
                    <div className="h-sub">Último: {convActual.ultimo} · {fmt(convActual.ts)}</div>
                  </div>
                </div>
              </header>

              <div className="msg-box" ref={boxRef}>
                {msgs.map(m=> (
                  <MessageBubble
                    key={m.id}
                    me={m.autor?.id === 'u-me'}
                    name={m.autor?.nombre || '—'}
                    text={m.texto}
                    time={fmt(m.ts)}
                  />
                ))}
              </div>

              <form className="msg-input" onSubmit={send}>
                <input
                  className="input"
                  placeholder={`Mensaje para ${convActual.nombre}…`}
                  value={text}
                  onChange={e=>setText(e.target.value)}
                />
                <button className="btn" type="submit">Enviar</button>
              </form>
            </>
          ) : (
            <div className="msg-empty">Seleccioná una conversación</div>
          )}
        </section>
      </div>
    </Page>
  )
}
