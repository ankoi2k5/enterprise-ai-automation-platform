import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Users from './pages/Users'
import Chat from './pages/Chat'
import Documents from './pages/Documents'
import Reports from './pages/Reports'
import EmailAgent from './pages/EmailAgent'

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
                <Route
                    path="/chat"
                    element={
                        <PrivateRoute>
                            <Chat />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/documents"
                    element={
                        <PrivateRoute>
                            <Documents />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/reports"
                    element={
                        <PrivateRoute>
                            <Reports />
                        </PrivateRoute>
                    }
                />
                <Route path="/email" element={<PrivateRoute><EmailAgent /></PrivateRoute>} />
            </Routes>
        </BrowserRouter>
    )
}

export default App