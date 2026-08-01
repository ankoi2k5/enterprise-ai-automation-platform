import { useState } from 'react'
import api from '../api'
import { useNavigate } from 'react-router-dom'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'

function Reports() {
    const [report, setReport] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const generateReport = async () => {
        setLoading(true)
        setError('')
        setReport('')
        try {
            const res = await api.get('/reports/system')
            setReport(res.data.report)
        } catch (err: any) {
            if (err.response?.status === 403) {
                setError('Ban khong co quyen xem bao cao nay (chi danh cho Admin)')
            } else {
                setError('Co loi xay ra khi tao bao cao')
            }
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="p-8 max-w-3xl mx-auto">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold">Bao cao He thong (AI)</h1>
                <button onClick={() => navigate('/chat')} className="bg-blue-600 text-white px-4 py-2 rounded">
                    Ve Chat
                </button>
            </div>

            <button
                onClick={generateReport}
                disabled={loading}
                className="bg-indigo-600 text-white px-4 py-2 rounded mb-6 disabled:opacity-50"
            >
                {loading ? 'AI dang tao bao cao...' : 'Tao bao cao moi'}
            </button>

            {error && <p className="text-red-500 mb-4">{error}</p>}

            {report && (
                <div className="prose max-w-none border rounded p-6 bg-white shadow-sm">
                    <ReactMarkdown remarkPlugins={[remarkGfm]}>{report}</ReactMarkdown>
                </div>
            )}
        </div>
    )
}

export default Reports