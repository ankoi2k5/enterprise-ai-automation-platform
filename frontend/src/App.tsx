import { useEffect, useState } from 'react'

interface User {
  id: number
  email: string
  fullName: string
}

function App() {
  const [users, setUsers] = useState<User[]>([])

  useEffect(() => {
    fetch('http://localhost:8080/api/users')
      .then((res) => res.json())
      .then((data) => setUsers(data))
      .catch((err) => console.error('Loi goi API:', err))
  }, [])

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Danh sach Users</h1>
      <ul>
        {users.map((u) => (
          <li key={u.id}>{u.fullName} - {u.email}</li>
        ))}
      </ul>
    </div>
  )
}

export default App