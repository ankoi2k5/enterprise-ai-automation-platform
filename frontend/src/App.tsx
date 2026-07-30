import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Users from './pages/Users'

function PrivateRoute({ children }: { children: React.ReactNode }) {
    const token = localStorage.getItem('token')
    return token ? <>{children}</> : <Navigate to="/login" />
}

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route
                    path="/users"
                    element={
                        <PrivateRoute>
                            <Users />
                        </PrivateRoute>
                    }
                />
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App