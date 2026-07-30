import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

function Login() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleLogin = async () => {
        setError('')
        try {
            const res = await api.post('/auth/login', { email, password })
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('fullName', res.data.fullName)
            localStorage.setItem('role', res.data.role)
            navigate('/users')
        } catch (err) {
            setError('Sai email hoac mat khau')
        }
    }

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-80">
                <h1 className="text-xl font-bold mb-4">Dang nhap</h1>
                {error && <p className="text-red-500 mb-2">{error}</p>}
                <input
                    className="border w-full p-2 mb-3 rounded"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    className="border w-full p-2 mb-3 rounded"
                    placeholder="Mat khau"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button
                    className="bg-blue-600 text-white w-full py-2 rounded hover:bg-blue-700"
                    onClick={handleLogin}
                >
                    Dang nhap
                </button>
            </div>
        </div>
    )
}

export default Login