import React, { Fragment, useState, useReducer, useEffect } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Collapse, Select, Space, message, Button } from 'antd';
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth';
import { useNavigate } from 'react-router-dom';

const { Panel } = Collapse;

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const FaqList = () => {
    const [expandIconPosition,] = useState('left');
    const [items, setItems] = useState([])
    const { user } = useAuth()
    const navigate = useNavigate()
    const onChange = (key) => {
        //
    };

    useEffect(() => {
        axios.get(`/common/faq?limit=100&page=0&userType=clinic`).then((response) => {
            const data = response.data.data
            if (data) { // array[Model]
                setItems(data)
            }
        }).catch((e) => {
            message.error(e.message);
        })
    }, [user])

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="FAQ" style={{ borderRadius: '10px' }}>
                    <Space direction='vertical'>
                        <Collapse
                            defaultActiveKey={['1']}
                            onChange={onChange}
                            expandIconPosition={expandIconPosition}
                        >
                            {
                                items.map((item) => {
                                    return (
                                        <Panel header={item.question} key={item._id}>
                                            <div>{item.answer}</div>
                                        </Panel>
                                    )
                                })
                            }
                        </Collapse>
                    </Space>
                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default FaqList
