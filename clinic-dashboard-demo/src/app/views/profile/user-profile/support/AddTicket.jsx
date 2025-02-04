import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography } from 'antd';
import { Breadcrumb } from 'app/components'
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { PageHeader, Tag, Statistic, Descriptions, Row, Divider, Select, Col, Comment, Tooltip, message } from 'antd';
import moment from 'moment';
import { Link } from 'react-router-dom'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useApp from 'app/hooks/useApp';
import useAuth from 'app/hooks/useAuth';

const { TextArea } = Input;
const { Option } = Select;

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const AddTicket = () => {
    const [loadings, setLoadings] = useState([]);

    const [description, setDescription] = React.useState(undefined)
    const [subject, setSubject] = React.useState(undefined)

    const [priority, setpriority] = useState([])
    const [department, setDepartment] = useState([])
    const { restfulAPI } = useApp()
    const navigate = useNavigate()

    const inputValueChanged = (e) => {
        setDescription(e.target.value)
    }

    const enterLoading = (index) => {
        setLoadings((prevLoadings) => {
            const newLoadings = [...prevLoadings];
            newLoadings[index] = true;
            return newLoadings;
        });

        const request = axios.post(`/clinic/ticket/create`, {
            "subject": subject,
            "message": description,
            "priority": priority,
            "department": department,
        })

        restfulAPI(request).then((model) => {
            const success = model.success
            if (success) {
                setDescription(undefined)
                navigate('/clinic/ticket-list')
            } else {
                message.error(model.message)
            }
        }).catch((e) => {
            message.error(e.message)
        })
    };

    const departmentHandleChange = (value) => {
        setDepartment(value ?? undefined)
    };

    const priorityHandleChange = (value) => {
        setpriority(value ?? undefined)
    };

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Create - Ticket">
                    <div>
                        <Row style={{ marginBottom: 8 }} justify="start" gutter={[16, 8]}>
                            <Col >
                                <Space>
                                    Priority:
                                    <Select
                                        defaultValue={'None'}
                                        style={{
                                            width: 120,
                                        }}
                                        onChange={priorityHandleChange}
                                    >
                                        <Option value="Low">Low</Option>
                                        <Option value="Medium">Medium</Option>
                                        <Option value="High">High</Option>
                                    </Select>
                                </Space>
                            </Col>

                            <Col>
                                <Space>
                                    Department:
                                    <Select
                                        defaultValue={'None'}
                                        style={{
                                            width: 120,
                                        }}
                                        onChange={departmentHandleChange}
                                    >
                                        <Option value="Technical">Technical</Option>
                                        <Option value="Financial">Financial</Option>
                                        <Option value="Complaint">Complaint</Option>
                                        <Option value="Help">Help</Option>
                                    </Select>
                                </Space>
                            </Col>
                        </Row>
                    </div>
                    <div>
                        <Input
                            placeholder={`Subject`}
                            onChange={e => {
                                setSubject(e.target.value)
                            }}
                            style={{ marginBottom: 8, display: 'block' }}
                        />
                        <TextArea rows={4} style={{ marginBottom: 8 }} onChange={inputValueChanged} value={description} />
                        <Button type="primary" loading={loadings[0]} onClick={() => enterLoading(0)}>
                            Send
                        </Button>
                    </div>
                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default AddTicket
