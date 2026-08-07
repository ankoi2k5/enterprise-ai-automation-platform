import { useState } from 'react'
import api from '../api'
import { useNavigate } from 'react-router-dom'

function EmailAgent() {
    const [instruction, setInstruction] = useState('')
    const [recipientName, setRecipientName] = useState('')
    const [recipientEmail, setRecipientEmail] = useState('')
    const [subject, setSubject] = useState('')
    const [body, setBody] = useState('')
    const [loading, setLoading] = useState(false)
    const [sending, setSending] = useState(false)
    const [status, setStatus] = useState('')
    const navigate = useNavigate()

    const handleDraft = async () => {
        setLoading(true)
        setStatus('')
        try {
            const res = await api.post('/email/draft', { instruction, recipientName })
            setSubject(res.data.subject)
            setBody(res.data.body)
        } catch (err) {
            setStatus('Loi khi soan email')
        } finally {
            setLoading(false)
        }
    }

    const handleSend = async () => {
        setSending(true)
        setStatus('')
        try {
            await api.post('/email/send', { to: recipientEmail, subject, body })
            setStatus('Da gui email thanh cong!')
        } catch (err) {
            setStatus('Gui email that bai')
        } finally {
            setSending(false)
        }
    }

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold">Email Agent</h1>
                <button onClick={() => navigate('/chat')} className="bg-blue-600 text-white px-4 py-2 rounded">
                    Ve Chat
                </button>
            </div>

            <div className="space-y-3 mb-6">
                <input
                    className="border w-full p-2 rounded"
                    placeholder="Ten nguoi nhan (vd: Nguyen Van A)"
                    value={recipientName}
                    onChange={(e) => setRecipientName(e.target.value)}
                />
                <input
                    className="border w-full p-2 rounded"
                    placeholder="Email nguoi nhan"
                    value={recipientEmail}
                    onChange={(e) => setRecipientEmail(e.target.value)}
                />
                <textarea
                    className="border w-full p-2 rounded"
                    rows={3}
                    placeholder="Yeu cau AI soan email (vd: nhac nho nop bao cao thu 6)"
                    value={instruction}
                    onChange={(e) => setInstruction(e.target.value)}
                />
                <button
                    onClick={handleDraft}
                    disabled={loading || !instruction || !recipientName}
                    className="bg-indigo-600 text-white px-4 py-2 rounded disabled:opacity-50"
                >
                    {loading ? 'AI dang soan...' : 'AI soan email'}
                </button>
            </div>

            {subject && (
                <div className="border rounded p-4 bg-gray-50 mb-4">
                    <label className="block text-sm font-semibold mb-1">Tieu de</label>
                    <input
                        className="border w-full p-2 rounded mb-3"
                        value={subject}
                        onChange={(e) => setSubject(e.target.value)}
                    />
                    <label className="block text-sm font-semibold mb-1">Noi dung</label>
                    <textarea
                        className="border w-full p-2 rounded"
                        rows={8}
                        value={body}
                        onChange={(e) => setBody(e.target.value)}
                    />
                    <button
                        onClick={handleSend}
                        disabled={sending || !recipientEmail}
                        className="bg-green-600 text-white px-4 py-2 rounded mt-3 disabled:opacity-50"
                    >
                        {sending ? 'Dang gui...' : 'Gui email'}
                    </button>
                </div>
            )}

            {status && <p className="mt-2">{status}</p>}
        </div>
    )
}

export default EmailAgent