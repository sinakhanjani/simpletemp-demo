import React, { useEffect } from 'react'
import { Table, Tag } from 'antd';
import { Link } from 'react-router-dom'

const TicketTable = ({ data }) => {
    const columns = [
        {
            title: 'Ticket | ID',
            dataIndex: 'ticketID',
            key: 'ticketID',
            width: '10%',
            render: (text, record) => <Link to={`/clinic/ticket-list/${record._id}`}>{`TID-${text}`}</Link>,
        },
        {
            title: 'Subject',
            dataIndex: 'subject',
            key: 'subject',
            width: '10%',
            // ...getColumnSearchProps('email'),
        },
        {
            title: 'Type',
            dataIndex: 'department',
            key: 'department',
            width: '10%',
            // ...getColumnSearchProps('email'),
            render: (item) => (
                <Tag color={departmentColor(item)} key={item}>
                    {item.toUpperCase()}
                </Tag>
            )
        },
        {
            title: 'Priority',
            dataIndex: 'priority',
            key: 'priority',
            width: '10%',
            // ...getColumnSearchProps('email'),
            render: (item) => (
                <Tag color={priorityColor(item)} key={item}>
                    {item.toUpperCase()}
                </Tag>
            )
        },
        {
            title: 'Status',
            dataIndex: 'state',
            key: 'state',
            width: '10%',
            // ...getColumnSearchProps('email'),
            render: (item) => (
                <Tag color={sateColor(item)} key={item}>
                    {item.toUpperCase()}
                </Tag>
            )
        },
    ];

    const sateColor = (item) => {
        if (item === 'open') {
            return 'blue'
        } else if (item === 'pending') {
            return 'orange'
        } else {
            return 'red'
        }
    }

    const priorityColor = (item) => {
        if (item === 'Low') {
            return 'blue'
        } else if (item === 'Medium') {
            return 'orange'
        } else if (item === 'High') {
            return 'red'
        }
    }

    const departmentColor = (item) => {
        if (item === 'Technical') {
            return 'blue'
        } else if (item === 'Financial') {
            return 'gray'
        } else if (item === 'Complaint') {
            return 'orange'
        } else if (item === 'Help') {
            return 'red'
        }
    }

    return (<>
        <Table columns={columns} dataSource={data} scroll={{ x: true }} />
    </>)
}

export default TicketTable