import { useEffect, useState } from 'react'
import api from '../api'
import { useNavigate } from 'react-router-dom'

interface DocItem {
    id: number
    fileName: string
    uploadedAt: string
}

function Documents() {
    const [docs, setDocs] = useState<DocItem[]>([])
    const [file, setFile] = useState<File | null>(null)
    const [uploading, setUploading] = useState(false)
    const [message, setMessage] = useState('')
    const navigate = useNavigate()

    const loadDocs = () => {
        api.get('/documents').then((res) => setDocs(res.data))
    }

    useEffect(() => {
        loadDocs()
    }, [])

    const handleUpload = async () => {
        if (!file) return
        if (!file.name.endsWith('.txt')) {
            setMessage('Chi ho tro file .txt')
            return
        }
        setUploading(true)
        setMessage('')
        const formData = new FormData()
        formData.append('file', file)

        try {
            await api.post('/documents/upload', formData, {
                headers: { 'Content-Type': 'multipart/form-data' },
            })
            setMessage('Upload va xu ly thanh cong!')
            setFile(null)
            loadDocs()
        } catch (err) {
            setMessage('Upload that bai')
        } finally {
            setUploading(false)
        }
    }

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold">Quan ly Tai lieu</h1>
                <button onClick={() => navigate('/chat')} className="bg-blue-600 text-white px-4 py-2 rounded">
                    Ve Chat
                </button>
            </div>

            <div className="border rounded p-4 mb-6 bg-gray-50">
                <input
                    type="file"
                    accept=".txt"
                    onChange={(e) => setFile(e.target.files?.[0] || null)}
                    className="mb-3"
                />
                <button
                    onClick={handleUpload}
                    disabled={!file || uploading}
                    className="bg-green-600 text-white px-4 py-2 rounded disabled:opacity-50 block"
                >
                    {uploading ? 'Dang xu ly...' : 'Upload'}
                </button>
                {message && <p className="mt-2 text-sm">{message}</p>}
            </div>

            <h2 className="text-lg font-semibold mb-2">Danh sach tai lieu</h2>
            <ul className="space-y-2">
                {docs.map((d) => (
                    <li key={d.id} className="border rounded p-2">
                        {d.fileName}
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default Documents