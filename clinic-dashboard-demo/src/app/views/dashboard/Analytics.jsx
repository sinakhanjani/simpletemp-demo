/* eslint-disable react-hooks/exhaustive-deps */


import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography, Modal, Popover, DatePicker } from 'antd';
import { Breadcrumb } from 'app/components'
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { PageHeader, Tag, Statistic, Descriptions, Row, Divider, Select, Col, Comment, Tooltip, Progress, List, Image, message, Upload, UploadProps } from 'antd';
import moment from 'moment';
import { Link } from 'react-router-dom'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth';
import Icon from '@ant-design/icons';
import { UploadOutlined } from '@ant-design/icons';
import FormData from 'form-data'
import useApp from 'app/hooks/useApp'
import { uniqueId } from 'lodash';
import { Rate } from 'antd';
import { Avatar, Segmented } from 'antd';
import { UserOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const { Option } = Select;

const formatter = new Intl.NumberFormat('GBP', {
  style: 'currency',
  currency: 'GBP',
  // These options are needed to round to whole numbers if that's what you want.
  //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
  //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
})

const ContentBox = styled('div')(({ theme }) => ({
  margin: '30px',
  [theme.breakpoints.down('sm')]: {
    margin: '16px',
  },
}))
const { Meta } = Card

const Analytics = () => {

  const navigate = useNavigate()
  // const { invoiceId } = useParams()
  const { restfulAPI } = useApp()
  const { user } = useAuth()
  const [penddingShifts, setPenddingShifts] = useState([])
  const [confirmedShift, setConfirmedShift] = useState([])

  const [dayPenddingShifts, setDayPenddingShifts] = useState()
  const [dayConfirmedShift, setDayConfirmedShift] = useState()

  const [isProfileComplete, setIsProfileComplete] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const [segmentOption, setSegmentOption] = useState('Pending Shifts')
  const [currentDay, setCurrentDay] = useState(moment().format('YYYY-MM-DD'))

  const moments = () => {
    let today = moment().startOf('day');
    let todayAdd1 = moment().add(1, 'days').startOf('day');
    let todayAdd2 = moment().add(2, 'days').startOf('day');
    let todayAdd3 = moment().add(3, 'days').startOf('day');
    let todayAdd4 = moment().add(4, 'days').startOf('day');
    let todayAdd5 = moment().add(5, 'days').startOf('day');
    let todayAdd6 = moment().add(6, 'days').startOf('day');

    const days = [today, todayAdd1, todayAdd2, todayAdd3, todayAdd4, todayAdd5, todayAdd6]
    const items = days.map((time, index) => {
      return {
        label: (
          <div style={{ padding: 4 }}>
            <div>{time.format('dddd')}</div>
            {
              index !== 0 &&
              <div>{time.format('MM-DD')}</div>
            }
          </div>
        ),
        value: time.format('YYYY-MM-DD'),
      }
    })

    return items
  }

  useEffect(() => {
    const requestPending = axios.get(`/clinic/shift/home/pendingshifts`)
    const requestConfirmed = axios.get(`/clinic/shift/home/confirmedshifts`)

    restfulAPI(requestPending).then((model) => {
      if (model.success) {
        setPenddingShifts(model.data)
      }
    })
    restfulAPI(requestConfirmed).then((model) => {
      if (model.success) {
        setConfirmedShift(model.data)
      }
    })

    // Check if the user has completed their profile
    const checkProfileCompletion = async () => {
      try {
        const response = await axios.get('/clinic/profile');
        if (response.data.completed) {
          setIsProfileComplete(true);
        }
      } catch (error) {
        console.error('Error retrieving profile:', error);
      }
    };

    checkProfileCompletion();
  }, [])

  useEffect(() => [
    filterDay(currentDay, segmentOption)
  ], [penddingShifts, confirmedShift])

  function filterDay(value, option) {
    if (option === 'Pending Shifts') {
      const found = penddingShifts?.find((i) => i.date === value)
      if (found) {
        setDayPenddingShifts(found)
      } else {
        setDayPenddingShifts(undefined)
      }
    } else {
      const found = confirmedShift?.find((i) => i.date === value)
      if (found) {
        setDayConfirmedShift(found)
      } else {
        setDayConfirmedShift(undefined)
      }
    }
  }

  const PendingList = ({ data }) => {

    return (
      <>
        <List
          itemLayout="horizontal"
          dataSource={data}
          style={{ margin: '0', padding: '0' }}
          renderItem={item => (
            <List.Item
              style={{ background: '#f5f5f5', padding: '8px', margin: '8px 0', borderRadius: '15px' }}
              extra={<PendingExtra item={item} />}
            >
              <List.Item.Meta
                title={<a href={item.countOffers === 0 ? `/clinic/calendar` : `/clinic/calendar/${item._id}?tag=orange`}>{`${item.userType.toUpperCase()}`}</a>}
                description={`${moment(item.date).format("dddd, MMMM Do YYYY")},at ${moment(item.arrivalTime).format("h:mm a")} - ${moment(item.endTime).format("h:mm a")}`}
              />
            </List.Item>
          )}
        />
      </>
    )
  }

  const ConfirmExtra = ({ item }) => {
    return (
      <>
        <Space direction='vertical'>
          <h4>Hourly Rate: {formatter.format(item.offers[0].preferredPrice)}</h4>
          <h4>Total: {item.offers[0].cost}</h4>
        </Space>
      </>
    )
  }

  const PendingExtra = ({ item }) => {
    return (
      <>
        <Space direction='vertical'>
          <h4>Hourly Rate: {formatter.format(item.preferredPrice)}</h4>
          <h4>Offer to Review: {item.countOffers}</h4>
        </Space>
      </>
    )
  }

  const ConfirmList = ({ data }) => {

    return (
      <>
        <List
          itemLayout="horizontal"
          dataSource={data}
          style={{ margin: '0', padding: '0' }}
          renderItem={item => (
            <List.Item
              style={{ background: '#f5f5f5', padding: '8px', margin: '8px 0', borderRadius: '15px' }}
              extra={<ConfirmExtra item={item} />}
            >
              <List.Item.Meta
                avatar={<Image
                  width={100}
                  height={100}
                  src={item.dentalTemp?.photoURL}
                  style={{ borderRadius: '50%', objectFit: 'cover' }}
                />}
                title={<a href={`/clinic/calendar/${item._id}?tag=green`}>{`${item.dentalTemp?.fullname} | ${item.dentalTemp?.userType.toUpperCase()}`}</a>}
                description={`${moment(item.date).format("dddd, MMMM Do YYYY")},at ${moment(item.arrivalTime).format("h:mm a")} - ${moment(item.endTime).format("h:mm a")}`}
              />
            </List.Item>
          )}
        />
      </>
    )
  }

  const Content = () => {
    return (
      <>
        <Row gutter={[16, 16]}>
          <Col span={24}>
            <Segmented
              options={moments()}
              onChange={(value) => {
                filterDay(value, segmentOption);
                setCurrentDay(value);
              }}
              defaultValue={currentDay}
              size='small'
              block
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '8px',
                borderRadius: '4px',
                backgroundColor: '#f5f5f5',
              }}

            />
          </Col>
        </Row>
        <Row gutter={[16, 16]} justify="center" style={{ marginTop: '16px' }}>
          <Col>
            <Segmented defaultValue={segmentOption} options={['Pending Shifts', 'Confirmed Shifts']} onChange={(value) => {
              setSegmentOption(value)
              filterDay(currentDay, value)
            }} />
          </Col>
        </Row>
        <Row gutter={[16, 16]} justify="center">
          <Col flex='700px'>
            {
              segmentOption === 'Pending Shifts' ?
                <PendingList data={dayPenddingShifts?.shifts} />
                :
                <ConfirmList data={dayConfirmedShift?.shifts} />
            }
          </Col>
        </Row>
      </>
    )
  }

  return (
    <Fragment>
      <ContentBox>
        <Card style={{ borderRadius: '10px' }} title="SimpleTemp">
          <Row gutter={[16, 16]}>
            <Col>
              <h2>Hello, {user.fullname}</h2>
              <h3>Requesting a dental temp has never been easier!</h3>
            </Col>
          </Row>
          {user.hasShift ? (
            <Content />
          ) : (
            <>
              <Space direction="vertical">
                <p>
                  Welcome! Book your first dental temp by posting a shift and watch the offers come through.
                </p>
                {isProfileComplete ? (
                  <Button type="primary" onClick={() => navigate('/clinic/post-shift')}>
                    Post a Shift
                  </Button>
                ) : (
                  <Button type="primary" onClick={() => setIsModalVisible(true)}>
                    Post a Shift
                  </Button>
                )}
              </Space>
              <Modal
                title="Complete Profile"
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                footer={[
                  <Button key="cancel" onClick={() => setIsModalVisible(false)}>
                    Cancel
                  </Button>,
                  <Button key="complete" type="primary" onClick={() => navigate('/clinic/user-profile/my-profile')}>
                    Complete Profile
                  </Button>,
                ]}
              >
                <p>To start posting shifts and securing temps, complete your profile. It won't take long!</p>
              </Modal>
            </>
          )}

        </Card>
      </ContentBox>
    </Fragment>
  )
}

export default Analytics