/* eslint-disable react-hooks/exhaustive-deps */

import moment from 'moment'
import React, { Fragment, useState, useReducer, useEffect, useRef} from 'react'
import { styled } from '@mui/system'
import CalendarContext from 'app/contexts/CalendarContext'
import axios from 'axios.js'
// import { uniqueId } from 'lodash';
import AppBreadCrumb from 'app/AppBreadCrumb';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { Link } from 'react-router-dom'
import useApp from 'app/hooks/useApp'
import { Calendar,Card, Select, Space, Row, Button, Badge, Popconfirm, Modal, message } from 'antd';
import '../../../Styles/ShiftCard.css';
import { LeftOutlined, RightOutlined } from '@ant-design/icons';



const formatter = new Intl.NumberFormat('GBP', {
    style: 'currency',
    currency: 'GBP',
    // These options are needed to round to whole numbers if that's what you want.
    //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
})

const { Option } = Select;

const badgeColor = (tag) => {
    switch (tag) {
        case 'green': return 'rgba(134, 220, 61, 0.20)' 
        case 'red': return 'rgba(245, 34, 45, 0.20)'
        case 'orange': return 'rgba(250, 140, 22, 0.20)'
        default: return 'rgba(245, 34, 45, 0.20)' 
    }
}
const badgeBorderColor = (tag) => {
    switch (tag) {
        case 'green': return 'rgba(0, 128, 0, 0.8)' 
        case 'red': return 'rgba(245, 34, 45, 0.5)' 
        case 'orange': return 'rgba(250, 140, 22, 0.5)' 
        default: return 'rgba(245, 34, 45, 0.5)' 
    }
}

const userTypeMime = (userType) => {
    return (userType === 'nurse') ? 'Dental Nurse' : 'Hygienist'
}

const ShiftCard = ({ shift, cancel, handleClick }) => {
    const { restfulAPI } = useApp()
    const cardColor = badgeColor(shift.tag);
    const borderColor = badgeBorderColor(shift.tag);
    
    const cardContent = (
        <div className="card-content">
            <p><b>{`${userTypeMime(shift.userType)}`}</b></p>
            <div className="time-price">
                <p>{`${moment(shift.arrivalTime).utc().format('HH:mm')} - ${moment(shift.endTime).utc().format('HH:mm')}`}</p>
                <p style={{marginLeft: 'auto'}}>{`${formatter.format(shift?.preferredPrice)}/hr`}</p>
            </div>
        </div>
    );

    return (
        <div className="shift-card" style={{backgroundColor: cardColor, borderColor: borderColor, borderWidth: '2px', borderStyle: 'solid'}}>
            {shift.tag === "red" ? (
                <Popconfirm
                title={
                    <div style={{ textAlign: "center" }}>
                        There are no offers placed for the following shift:
                        <br />
                        ({userTypeMime(shift.userType)} | {moment(shift.arrivalTime).utc().format('HH:mm')} - {moment(shift.endTime).utc().format('HH:mm')}, {formatter.format(shift?.preferredPrice)}/hr)
                        <br />
                        Please be patient while dental temps review your shift.
                    </div>
                }
                onConfirm={(e) => {
                    const request = axios.post(`/clinic/shift/delete/${shift._id}`)

                    restfulAPI(request).then((model) => {
                        if (model.success) {
                            Modal.info({
                                title: 'Shift Cancelled',
                                content: (
                                    <div>
                                        <p>{`The shift you posted for ${moment(shift.date).format("dddd, MMMM Do YYYY")},at ${moment(shift.arrivalTime).format("h:mm a")} - ${moment(shift.endTime).format("h:mm a")} was cancelled.`}</p>
                                    </div>
                                ),
                                onOk() {
                                    window.location.reload()
                                 },
                            });
                        }
                    })
                }}
                onCancel={cancel}
                okText="Delete Shift"
                cancelText="Cancel"
                    overlayStyle={{ width: "300px", borderRadius: "25px" }} 
                >
                    {cardContent}
                </Popconfirm>
            ) : (
                <Link to={`/clinic/calendar/${shift._id}?tag=${shift?.tag}`}>
                    {cardContent}
                </Link>
            )}
        </div>
    )
}

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

const ShiftsCalendar = () => {
    const [items, setItems] = useState([])
    const navigate = useNavigate()
    const { restfulAPI } = useApp()

    const [filter, dispatch] = useReducer(filterReducer, {
        query: {
            from: moment().startOf('month'),
            to: moment().endOf('month')
        }
    })

    function fetchCalendar(date) {
        dispatch({
            type: 'POPULATE',
            state: {
                ...filter,
                query: {
                    from: date,
                    to: date
                },
            }
        })
    }
    
    const getMonthData = (value) => {
        let shifts = []
        items.forEach((item) => {
            const calendarDate = value.format('MM')
            const date = moment(item.date).format('MM')

            if (calendarDate === date) {
                shifts = item.shifts || []
            }
        })
        return (
            (shifts.length > 0) ?
                <Badge status='success' text='' />
                :
                null
        )
    };

    const monthCellRender = (value) => {
        const data = getMonthData(value);

        return data ? (
            <div className="notes-month">
                {data}
            </div>
        ) : null;
    };



    const DateCellRender = (value) => {

        let shifts = []
        items.forEach((item) => {
            const calendarDate = value.format('YYYY-MM-DD')
            const date = moment(item.date).format('YYYY-MM-DD')

            if (calendarDate === date) {
                shifts = item.shifts || []
            }
        })


        const cancel = (e) => {

        };

        return (
            <ul className="events">
                {shifts.map((shift) => (
                <ShiftCard 
                    shift={shift} 
                    cancel={cancel} 

                />
            ))}
            </ul>
        )
    };

    const calendarHeaderRender = ({ value, type, onChange }) => {
        const handlePrevMonth = () => {
          onChange(value.clone().subtract(1, 'month'));
        };
    
        const handleNextMonth = () => {
          onChange(value.clone().add(1, 'month'));
        };
    
        const handleMonthYearChange = (month, year) => {
          const updatedValue = value.clone().month(month).year(year);
          onChange(updatedValue);
        };
    
        return (
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <Button type="text" onClick={handlePrevMonth} icon={<LeftOutlined />} shape="circle" />
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <Select
                style={{ width: 120, marginRight: 8 }}
                value={value.month()}
                onChange={month => handleMonthYearChange(month, value.year())}
              >
                {moment.months().map((month, index) => (
                  <Option key={index} value={index}>{month}</Option>
                ))}
              </Select>
              <Select
                style={{ width: 80 }}
                value={value.year()}
                onChange={year => handleMonthYearChange(value.month(), year)}
              >
                {Array.from({ length: 10 }, (_, index) => moment().year() + index).map(year => (
                  <Option key={year} value={year}>{year}</Option>
                ))}
              </Select>
            </div>
            <Button type="text" onClick={handleNextMonth} icon={<RightOutlined />} shape="circle" />
          </div>
        )
      };

    const calendarOnChange = (date) => {
        const calendarDate = new moment(date)
        fetchCalendar(calendarDate)
    }

    useEffect(() => {
        const from = filter.query.from.startOf('month').format('YYYY-MM-DD')
        const to = moment(filter.query.to).endOf('month').add(1, 'day').format('YYYY-MM-DD')
        const request = axios.get(`/clinic/shift/calendar?from=${from}&to=${to}`)
        console.log(`/clinic/shift/calendar?from=${from}&to=${to}`);
        restfulAPI(request).then((model) => {
            const data = model.data
            if (data) { // array[Model]
                setItems(data)
            }
        })
    }, [filter])

    return (
        <Fragment>
      <ContentBox>
        <div className="breadcrumb" style={{ marginBottom: '16px' }}>
          <AppBreadCrumb />
        </div>
        <Card title="Calendar" style={{ borderRadius: '10px' }}>
          <Button type="primary" style={{ borderRadius: '15px' }} onClick={() => {
            navigate('/clinic/calendar/post-shift')
          }}>Post a Shift</Button>

          <Row>
            {items && (
              <Calendar
                dateCellRender={DateCellRender}
                monthCellRender={monthCellRender}
                headerRender={calendarHeaderRender}
                onChange={calendarOnChange}
              />
            )}
          </Row>
        </Card>
      </ContentBox>
    </Fragment>
    )
}

export default ShiftsCalendar