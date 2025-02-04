import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography, message } from 'antd';
import { Breadcrumb } from 'app/components'
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { PageHeader, Tag, Statistic, Descriptions, Row, Divider, Select, Col, Comment, Tooltip } from 'antd';
import moment from 'moment';
import { Link } from 'react-router-dom'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useApp from 'app/hooks/useApp';
import useAuth from 'app/hooks/useAuth';
import '../../../../../Styles/Messages.css';

const { TextArea } = Input;
const { Title, Paragraph } = Typography;
const { Option } = Select;

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const AdminCard = ({ message }) => {
    return (
      <div>
        <Comment 
          author={'ADMIN'}
          className="admin-chat-bubble" // Add a custom class for styling
          content={
            <p style={{ margin: '0'}}>
              {message.message}
            </p>
          }
          datetime={
            <Tooltip title={moment(message.createdAt).format('YYYY-MM-DD HH:mm')}>
              <span>{moment(message.createdAt).fromNow()}</span>
            </Tooltip>
          }
          style={{ display: 'inline-block', width: 'auto', maxWidth: '350px'}}
        />
      </div>
    )
}

const UserCard = ({ message, fullname }) => {
    return (
      <div style={{ padding: 10, textAlign: 'right' , display: 'inline-block', width: 'auto', maxWidth: '350px'}} className="user-chat-bubble">
        <Space style={{ color: 'gray' }}>
          {fullname}
          <Tooltip title={moment(message.createdAt).format('YYYY-MM-DD HH:mm')}>
            <span style={{ color: '#b5b5b5', fontSize: '12px' }}>{moment(message.createdAt).fromNow()}</span>
          </Tooltip>
        </Space>
  
        <p style={{ margin: '0'}}>
          {message.message}
        </p>
      </div>
    );
  };

const Messages = () => {
    const [description, setDescription] = React.useState(undefined)
    const [items, setItems] = useState([])
    const { ticketId } = useParams()
    // const navigate = useNavigate()
    const { restfulAPI } = useApp()
    const { user } = useAuth()

    const [loadings, setLoadings] = useState([]);

    const inputValueChanged = (e) => {
        setDescription(e.target.value)
    }

    const enterLoading = (index) => {
        setLoadings((prevLoadings) => {
            const newLoadings = [...prevLoadings];
            newLoadings[index] = true;
            return newLoadings;
        })
        const request = axios.post(`/clinic/message/send/${ticketId}`, {
            "message": description
        })

        restfulAPI(request).then((model) => {
            const success = model.success

            if (success) {
                const item = model.data
                setItems([
                    ...items,
                    item
                ])
                setDescription(undefined)
                setLoadings((prevLoadings) => {
                    const newLoadings = [...prevLoadings];
                    newLoadings[index] = false;
                    return newLoadings;
                });
            } else {
                setLoadings((prevLoadings) => {
                    const newLoadings = [...prevLoadings];
                    newLoadings[index] = false;
                    return newLoadings;
                });
            }
        }).catch((e) => {
            setLoadings((prevLoadings) => {
                const newLoadings = [...prevLoadings];
                newLoadings[index] = false;
                return newLoadings;
            });
            message.error(e.message)
        })
    }

    useEffect(() => {
        const request = axios.get(`/clinic/message/${ticketId}`)
        // get messages
        restfulAPI(request).then((model) => {
            const data = model.data
            if (data) { // array[Model]
                setItems(data)
            }
        })
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [user])

    return (
        <Fragment>
            <ContentBox >
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Conversation"  style={{ borderRadius: '10px', width: '50%'}}>
                    <div>
                        <div>
                            {items.map((item, index) => {
                                if (item.sender === 'admin') {
                                return <AdminCard message={item} key={index} />;
                                } else {
                                return (
                                    <div style={{ display: 'flex', justifyContent: 'flex-end' }} key={index}>
                                    <UserCard message={item} fullname={user.fullname ?? 'NO-NAME'} />
                                    </div>
                                );
                                }
                            })}
                        </div>
                    </div>

                    <div>
                        <TextArea rows={4} style={{ marginBottom: 8, borderRadius: '10px' }} onChange={inputValueChanged} value={description} />
                        <Button type="primary" style={{ borderRadius: '15px'}} loading={loadings[0]} onClick={() => enterLoading(0)}>
                            Send
                        </Button>
                    </div>
                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default Messages
