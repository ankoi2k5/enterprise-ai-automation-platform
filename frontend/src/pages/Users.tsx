import { useEffect, useState } from 'react'
import api from '../api'
import { useNavigate } from 'react-router-dom'

interface User {
    id: number
    email: string
    fullName: string
    role: string
}

function Users() {
    const [users, setUsers] = useState<User[]>([])
    const fullName = localStorage.getItem('fullName')
    const role = localStorage.getItem('role')
    const isAdmin = role === 'ADMIN'
    const navigate = useNavigate()
    const loadUsers = () => {
        api.get('/users')
            .then((res) => setUsers(res.data))
            .catch((err) => console.error('Loi goi API:', err))
    }

    useEffect(() => {
        loadUsers()
    }, [])

    const handleDelete = async (id: number) => {
        if (!confirm('Ban co chac muon xoa user nay?')) return
        try {
            await api.delete(`/users/${id}`)
            loadUsers() // tai lai danh sach sau khi xoa
        } catch (err) {
            alert('Khong the xoa (co the ban khong co quyen)')
        }
    }

    const handleLogout = () => {
        localStorage.clear()
        window.location.href = '/login'
    }

    return (
        <div className="p-8">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold">
                    Xin chao, {fullName} <span className="text-sm text-gray-500">({role})</span>
                </h1>
                <div className="flex gap-2">
                    <button onClick={() => navigate('/chat')} className="bg-green-600 text-white px-4 py-2 rounded">
                        Chat AI
                    </button>
                    <button onClick={() => navigate('/documents')} className="bg-purple-600 text-white px-4 py-2 rounded">
                        Tai lieu
                    </button>
                    {isAdmin && (
                        <button onClick={() => navigate('/reports')} className="bg-indigo-600 text-white px-4 py-2 rounded">
                            Bao cao
                        </button>
                    )}
                    <button onClick={handleLogout} className="bg-red-500 text-white px-4 py-2 rounded">
                        Dang xuat
                    </button>
                </div>
            </div>
            <h2 className="text-lg font-semibold mb-2">Danh sach Users</h2>
            <table className="w-full border-collapse">
                <thead>
                <tr className="border-b text-left">
                    <th className="p-2">Ho ten</th>
                    <th className="p-2">Email</th>
                    <th className="p-2">Vai tro</th>
                    {isAdmin && <th className="p-2">Hanh dong</th>}
                </tr>
                </thead>
                <tbody>
                {users.map((u) => (
                    <tr key={u.id} className="border-b">
                        <td className="p-2">{u.fullName}</td>
                        <td className="p-2">{u.email}</td>
                        <td className="p-2">{u.role}</td>
                        {isAdmin && (
                            <td className="p-2">
                                <button
                                    onClick={() => handleDelete(u.id)}
                                    className="text-red-600 hover:underline"
                                >
                                    Xoa
                                </button>
                            </td>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default Users