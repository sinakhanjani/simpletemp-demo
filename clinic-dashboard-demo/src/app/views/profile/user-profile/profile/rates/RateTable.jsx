import React, { useEffect } from 'react'
import { Table, Input, Button, Space, Tag, Rate } from 'antd';
import { Link } from 'react-router-dom'
import useRate from 'app/hooks/useRate'

const RateTable = ({ data }) => {
    const columns = [
        {
            title: 'Fullname',
            dataIndex: 'fullname',
            key: 'fullname',
            width: '10%',
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            width: '11%',
        },
        {
            title: 'Rate',
            dataIndex: 'rate',
            key: 'rate',
            width: '10%',
            render: (rate, record) => <Rate disabled defaultValue={rate} />,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            width: '10%',
        },
    ];

    return (<>
        <Table columns={columns} dataSource={data} scroll={{ x: true }}/>
    </>)
}

export default RateTable