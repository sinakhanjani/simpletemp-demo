import React, { Fragment, useState, useReducer, useEffect } from 'react'
import { styled } from '@mui/system'
import RateTable from './RateTable'
import RateContext from 'app/contexts/RateContext'
import axios from 'axios.js'
import { uniqueId } from 'lodash';
import { Card, Select, Space, Row, Col, Rate, Divider, Progress } from 'antd';
import AppBreadCrumb from 'app/AppBreadCrumb';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import moment from 'moment'
import useApp from 'app/hooks/useApp'

const filterReducer = (state, action) => {
    switch (action.type) {
        case 'POPULATE':
            return action.state
        default:
            return state
    }
}

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const Rates = () => {
    const [items, setItems] = useState([])
    const [rates, setRates] = useState(undefined)
    // const { userId } = useParams()
    // const navigate = useNavigate()
    const { restfulAPI } = useApp()

    useEffect(() => {
        const request = axios.get(`/clinic/rate?limit=10000&page=0`)
        restfulAPI(request).then((model) => {
            const data = model?.data?.rates

            if (data) { // array[Model]
                const items = data.map((item) => {
                    return {
                        ...item,
                        fullname: item.dentalTemp?.fullname,
                        userId: item.dentalTemp?._id,
                        userType: item.dentalTemp?.userType,
                        key: uniqueId(),
                        createdAt: moment(item.createdAt).format('YYYY-MM-DD HH:mm')
                    }
                })
                setRates(model.data)
                setItems(items)
            }
        })
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const percent = (value) => {
        const totalCount = rates?.ratesCount
        if (totalCount && value) {
            return Math.round((value * 100) / totalCount)
        } else {
            return 0
        }
    }

    return (
            <Fragment>
                <ContentBox>
                    <Card title="Ratings" style={{ borderRadius: '10px' }}>
                        {
                            rates &&
                            <Row style={{ marginBottom: 8 }} justify="start" gutter={[16, 8]}>
                                <Col>
                                    <Card title="Overall Rating" bordered={false} style={{ width: 300 }}>
                                        <h4>Number of Reviews: {rates?.ratesCount}</h4>
                                        <Space>
                                            <Rate disabled defaultValue={rates?.averages?.rate} />
                                        </Space>
                                        <Divider />
                                        <Space>
                                            <Rate disabled defaultValue={1} />
                                        </Space>
                                        <Progress percent={percent(rates?.star1?.count)} size="small" status="active" />

                                        <Space>
                                            <Rate disabled defaultValue={2} />
                                        </Space>
                                        <Progress percent={percent(rates?.star2?.count)} size="small" status="active" />

                                        <Space>
                                            <Rate disabled defaultValue={3} />
                                        </Space>
                                        <Progress percent={percent(rates?.star3?.count)} size="small" status="active" />

                                        <Space>
                                            <Rate disabled defaultValue={4} />
                                        </Space>
                                        <Progress percent={percent(rates?.star4?.count)} size="small" status="active" />

                                        <Space>
                                            <Rate disabled defaultValue={5} />
                                        </Space>
                                        <Progress percent={percent(rates?.star5?.count)} size="small" status="active" />

                                    </Card>
                                </Col>
                            </Row>
                        }
                        <RateTable data={items} />
                    </Card>
                </ContentBox>
            </Fragment>
    )
}

export default Rates
