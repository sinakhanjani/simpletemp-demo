import axios from 'axios'

const axiosInstance = axios.create()

axiosInstance.defaults.baseURL = '---------'
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) =>
        Promise.reject(
            (error.response && error.response.data) || 'Something went wrong!'
        )
)

export default axiosInstance
