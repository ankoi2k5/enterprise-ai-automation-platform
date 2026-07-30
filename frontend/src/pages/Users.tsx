import { useEffect, useState } from 'react'
import api from '../api'

interface User {
    id: number
    email: string
    fullName: string
}

function Users() {
    const [users, setUsers] = useState<User[]>([])
    const fullName = localStorage.getItem('fullName')

    useEffect(() => {
        api.get('/users')
            .then((res) => setUsers(res.data))
            .catch((err) => console.error('Loi goi API:', err))
    }, [])

    const handleLogout = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('fullName')
        window.location.href = '/login'
    }

    return (
        <div className="p-8">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold">Xin chao, {fullName}</h1>
                <button onClick={handleLogout} className="bg-red-500 text-white px-4 py-2 rounded">
                    Dang xuat
                </button>
            </div>
            <h2 className="text-lg font-semibold mb-2">Danh sach Users</h2>
            <ul>
                {users.map((u) => (
                    <li key={u.id}>{u.fullName} - {u.email}</li>
                ))}
            </ul>
        </div>
    )
}

export default Users