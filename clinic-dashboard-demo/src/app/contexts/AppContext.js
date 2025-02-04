import React from 'react'
import { message } from 'antd';
import useAuth from 'app/hooks/useAuth';
import { Button, Modal, Space } from 'antd';
import { modalClasses } from '@mui/material';

const AppContext = React.createContext({
    restfulAPI: () => Promise.resolve(),
})

export const AppProvider = ({ children }) => {
    const { logout } = useAuth()

    const error = (model) => {
        if (model.code === 641) {
            Modal.error({
                title: 'Your account has been suspended',
                content: model.message,
                onOk: () => {
                    logout()
                }
            });
        } else if (model.code === 403) {
            // account suspend
            Modal.error({
                title: 'Your account has been suspended',
                content: model.message,
            });
        } else {
            message.error(model.message)
        }
    }

    const restfulAPI = (axiosInstance) => {
        return new Promise(function (resolve, reject) {
            axiosInstance.then((response) => {
                if (response.data.success === true) {
                    resolve(response.data)
                } else {
                    error(response.data)
                }
            }).catch((e) => {
                console.log(e);
                error(e)
            })
        })
    }

    return (
        <AppContext.Provider
            value={{
                restfulAPI,
            }}
        >
            {children}
        </AppContext.Provider>
    )
}

export default AppContext

