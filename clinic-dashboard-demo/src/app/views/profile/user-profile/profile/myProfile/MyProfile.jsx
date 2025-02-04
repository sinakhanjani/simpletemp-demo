
import React, { Fragment } from 'react'
import { styled } from '@mui/system'
import { Card } from 'antd';
import AppBreadCrumb from 'app/AppBreadCrumb';
import useAuth from 'app/hooks/useAuth';
import EditProfile from './EditProfile';
import ActiveProfile from './ActiveProfile';
import { ProfileProvider } from 'app/contexts/ProfileContext';

const ContentBox = styled('div')(({ theme }) => ({
    margin: '30px',
    [theme.breakpoints.down('sm')]: {
        margin: '16px',
    },
}))

const MyProfile = () => {
    const { user } = useAuth();

    return (
        <ProfileProvider>
            <Fragment>
                <ContentBox>
                    <div className="breadcrumb" style={{ marginBottom: '16px' }}>
                        <AppBreadCrumb />
                    </div>
                    <Card title="My Profile" style={{ borderRadius: '10px' }}>
                        {
                            (user.profile.percentage === 100) ?
                                <EditProfile />
                                :
                                <ActiveProfile />
                        }
                    </Card>
                </ContentBox>
            </Fragment>
        </ProfileProvider>
    )
}

export default MyProfile