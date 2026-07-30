import { useState, useRef, useEffect } from 'react'
import api from '../api'

interface Message {
    role: 'user' | 'ai'
    text: string
}

function Chat() {
    const [messages, setMessages] = useState<Message[]>([])
    const [input, setInput] = useState('')
    const [loading, setLoading] = useState(false)
    const bottomRef = useRef<HTMLDivElement>(null)

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
    }, [messages])

    const sendMessage = async () => {
        if (!input.trim()) return
        const userMsg: Message = { role: 'user', text: input }
        setMessages((prev) => [...prev, userMsg])
        setInput('')
        setLoading(true)

        try {
            const res = await api.post('/chat', { message: userMsg.text })
            setMessages((prev) => [...prev, { role: 'ai', text: res.data.reply }])
        } catch (err) {
            setMessages((prev) => [...prev, { role: 'ai', text: 'Loi: khong the ket noi AI' }])
        } finally {
            setLoading(false)
        }
    }

    const handleKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault()
            sendMessage()
        }
    }

    return (
        <div className="flex flex-col h-screen p-4 max-w-2xl mx-auto">
            <h1 className="text-xl font-bold mb-4">Chat AI</h1>
            <div className="flex-1 overflow-y-auto border rounded p-4 mb-4 space-y-3 bg-gray-50">
                {messages.map((m, i) => (
                    <div key={i} className={`flex ${m.role === 'user' ? 'justify-end' : 'justify-start'}`}>
                        <div
                            className={`px-4 py-2 rounded-lg max-w-[75%] whitespace-pre-wrap ${
                                m.role === 'user' ? 'bg-blue-600 text-white' : 'bg-white border'
                            }`}
                        >
                            {m.text}
                        </div>
                    </div>
                ))}
                {loading && <div className="text-gray-400 italic">AI dang tra loi...</div>}
                <div ref={bottomRef} />
            </div>
            <div className="flex gap-2">
        <textarea
            className="flex-1 border rounded p-2 resize-none"
            rows={2}
            placeholder="Nhap tin nhan..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
        />
                <button
                    onClick={sendMessage}
                    disabled={loading}
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    Gui
                </button>
            </div>
        </div>
    )
}

export default Chat