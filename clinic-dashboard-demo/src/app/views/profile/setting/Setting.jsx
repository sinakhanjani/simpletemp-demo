
import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography } from 'antd';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import moment from 'moment';
import { Link } from 'react-router-dom'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth';
import useApp from 'app/hooks/useApp'
import { Switch } from 'antd';
const { Title } = Typography;

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const Setting = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const { restfulAPI } = useApp()
    const [defaultChecked, setDefaultChecked] = useState(true);

    const onChange = (checked) => {
        console.log(`switch to ${checked}`);
        const request = axios.post('/clinic/profile/togglenotification', { activeNotification: checked })

        restfulAPI(request).then((model) => {
            // Get this url from response in real world.
            console.log(model);
            if (!model.success) {
                setDefaultChecked(!defaultChecked)
            }
        })
    };

    useEffect(() => {
        console.log(user.activeNotification);
        setDefaultChecked(user.activeNotification)
    }, [user])

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Settings" style={{borderRadius: '10px', width: '500px'}}>
                    <Space direction="vertical" align="start">
                        <Space align="start">
                            <p style={{fontSize: '14px'}}>Notifications</p>
                            {defaultChecked ? <Switch defaultChecked={defaultChecked} onChange={onChange} /> : <Switch defaultChecked={defaultChecked} onChange={onChange} />}
                        </Space>
                        <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => navigate('/clinic/session/forgot-password')}>Change Password</Button>
                        
                        <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => navigate('/clinic/ticket-list',)}>Contact Us</Button>

                        <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => window.open('https://simpletemp.co.uk/termsofservice', '_blank')}>Terms of Serice</Button>

                        <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => window.open('https://simpletemp.co.uk/privacy', '_blank')}>Privacy Policy</Button>
                    </Space>
                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default Setting