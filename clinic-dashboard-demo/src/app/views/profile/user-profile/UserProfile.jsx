
import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography } from 'antd';
import { Breadcrumb } from 'app/components'
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { PageHeader, Tag, Statistic, Descriptions, Row, Divider, Select, Col, Comment, Tooltip, Progress, List, Image, message, Upload, UploadProps } from 'antd';
import moment from 'moment';
import { Link } from 'react-router-dom'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth';
import Icon from '@ant-design/icons';
import PhotoImagePicker from './PhotoImagePicker';
import { UploadOutlined } from '@ant-design/icons';
import FormData from 'form-data'
import useApp from 'app/hooks/useApp'

const { Meta } = Card

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const getBase64 = (img, callback) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
};

const UploadPhoto = (props) => (
    <Upload {...props}>
        <Button style={{borderRadius: '15px' }} icon={<UploadOutlined />}>Upload Photo</Button>
    </Upload>
);

const UserProfile = () => {
    const { user, fetchProfile } = useAuth();
    const navigate = useNavigate();
    const [visible, setVisible] = useState(false);
    const { restfulAPI } = useApp()
    const [imageUrl, setImageUrl] = useState();

    useEffect(() => {
        setImageUrl(user.photoURL)
    }, [user])

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Profile" style={{borderRadius: '10px', width: '70%'}}>
                    <Row >
                        <Col flex="none" >
                            <Card
                                bordered={false}
                                hoverable={false}
                                style={{ width: 260 }}
                                cover={
                                    user.photoURL ?
                                        <Space direction="vertical">
                                             <div style={{ width: 150, height: 150, borderRadius: '50%', overflow: 'hidden' }}>
                                                <Image
                                                    preview={{
                                                        visible,
                                                        onVisibleChange: (value) => {
                                                            setVisible(value);
                                                        },
                                                    }}
                                                    width={200}
                                                    src={imageUrl}
                                                    onClick={() => setVisible(true)}
                                                />
                                            </div>

                                            <UploadPhoto
                                                name="avatar"
                                                // listType="picture-card"
                                                className="avatar-uploader"
                                                showUploadList={false}
                                                // action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                                                beforeUpload={(file) => {
                                                    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';

                                                    if (!isJpgOrPng) {
                                                        message.error('You can only upload JPG/PNG file!');
                                                    }

                                                    const isLt2M = file.size / 1024 / 1024 < 2;

                                                    if (!isLt2M) {
                                                        message.error('Image must smaller than 2MB!');
                                                    }

                                                    return isJpgOrPng && isLt2M;
                                                }}
                                                onChange={(info) => {
                                                    if (info.file.status === 'uploading') {
                                                        return;
                                                    }

                                                    if (info.file.originFileObj) {
                                                        let data = new FormData()
                                                        data.append('profile_photo', info.file.originFileObj, info.file.name)

                                                        const request = axios.post('/clinic/profile/uploadphoto', data)
                                                        restfulAPI(request).then((model) => {
                                                            // Get this url from response in real world.
                                                            getBase64(info.file.originFileObj, (url) => {
                                                                setImageUrl(url);
                                                                fetchProfile()
                                                                message.success(`${info.file.name} file uploaded successfully`);
                                                            })
                                                        })
                                                    }
                                                }} />
                                        </Space>
                                        :
                                        <PhotoImagePicker />
                                }
                            >
                            </Card>
                        </Col>
                        <Col flex="auto">
                        <Space direction="vertical">
                                    <Meta title={user.fullname} description={user.email} />
                                    <Progress percent={user.profile.percentage} size="small" />
                                </Space>
                        </Col>
                        <Col flex="auto">
                                <Space direction="vertical">
                                    {
                                        (user.profile.percentage === 100) &&
                                        <Button type="link" 
                                        style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                        onClick={() => navigate('/clinic/user-profile/my-profile')}>Clinic Profile</Button>
                                    }
                                    <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => navigate('/clinic/user-profile/rates/')}>Clinic Ratings</Button>
                                    
                                    <Button type="link" 
                                    style={{ border: '1px solid', borderColor: '#93d2d79b', borderRadius: '10px' }}
                                    onClick={() => navigate('/clinic/user-profile/note-and-docs')}>Clinic Notes & Documents</Button>
                                </Space>
                        </Col>
                    </Row>
                </Card >
            </ContentBox >
        </Fragment >
    )
}

export default UserProfile


// complete all profile
// complete map
// complete stripe
// complete fcm notificaiton and list
// complete suspend

/*
const UserProfile = () => {

    return (
        <Fragment>
            <ContentBox>
                <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                    <AppBreadCrumb />
                </div>
                <Card title="Profile">

                </Card>
            </ContentBox>
        </Fragment>
    )
}

export default UserProfile
*/