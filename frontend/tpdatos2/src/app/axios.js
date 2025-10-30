import axios from 'axios'

export const USE_MOCK = true

const api = axios.create({ baseURL: 'http://localhost:8080/api' })

api.interceptors.request.use(cfg => {
    const token = localStorage.getItem('authToken')
    if (token) cfg.headers.Authorization = `Bearer ${token}`
    return cfg
})

export default api