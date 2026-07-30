import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
})

// Tu dong gan token vao moi request neu da dang nhap
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

export default api