import React, { Fragment, useState, useReducer, useEffect } from 'react'
import { styled } from '@mui/system'
import TicketTable from './TicketTable'
import axios from 'axios.js'
import { uniqueId } from 'lodash';
import { Card, Select, Space, Row, Col, Button } from 'antd';
import moment from 'moment'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth'
import useApp from 'app/hooks/useApp';
import { useNavigate } from 'react-router-dom';

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const TicketList = () => {
    const [items, setItems] = useState([])
    const { user } = useAuth()
    const { restfulAPI } = useApp()
    const navigate = useNavigate()

    useEffect(() => {
        const request = axios.get(`/clinic/ticket`)
        restfulAPI(request).then((model) => {
            const data = model.data

            if (data) { // array[Model]
                const items = data.map((item) => {
                    return {
                        ...item,
                        userId: item.user?._id ?? '',
                        createdAt: moment(item.createdAt).format('YYYY-MM-DD HH:mm'),
                        key: uniqueId()
                    }
                })

                setItems(items)
            }
        })

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [user])

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Tickets" style={{ borderRadius: '10px' }}>
                    <Button type='primary' style={{ 'marginBottom': '8px', borderRadius: '15px' }} onClick={() => {
                        navigate('/clinic/send-ticket')
                    }}>Create a Ticket</Button>
                    <TicketTable data={items} />
                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default TicketList
