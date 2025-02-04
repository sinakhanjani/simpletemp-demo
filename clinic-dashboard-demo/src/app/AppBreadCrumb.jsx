import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Breadcrumb } from 'antd';

const breadcrumbNameMap = {
    // Main
    '/clinic': 'Clinic',
    '/clinic/dashboard': 'Dashboard',
    // Notification
    '/clinic/notification': 'Notification',
    // Setting
    '/clinic/setting': 'Setting',
    //Profile
    '/clinic/user-profile': 'Clinic Profile',
    '/clinic/user-profile/my-profile': 'My Profile',
    '/clinic/user-profile/note-and-docs': 'Note and Document',
    '/clinic/user-profile/clinic-information': 'Clinic Information',
    '/clinic/user-profile/my-profile/pin-location': 'Pin Location',
    '/clinic/user-profile/clinic-detail': 'Clinic Detail',
    '/clinic/user-profile/manage-payment': 'Manage Payment',
    '/clinic/user-profile/rates/': 'Rates',
    // Support and FAQ
    '/clinic/faq': 'FAQ',
    '/clinic/ticket-list': 'Ticket List',
    '/clinic/ticket-list/:ticketId': 'Ticket',
    '/clinic/send-ticket': 'Send Ticket',
    // Calendar
    '/clinic/calendar': 'Calendar',
    '/clinic/calendar/post-shift': 'Post a Shift',
    '/clinic/calendar/:shiftId': 'Shift Detail',
    // Invoice & Dispute Invoice
    '/clinic/invoice': 'List - Invoice',
    '/clinic/invoice/:invoiceId': 'Invoice Detail',
    '/clinic/invoice/dispute-invoice': 'Dispute Invoice',
    // Rate to DentalTemp
    
    // Dashboard - Home
}

const AppBreadCrumb = () => {
    const location = useLocation();
    const pathSnippets = location.pathname.split('/').filter((i) => i);
    const extraBreadcrumbItems = pathSnippets.map((_, index) => {
        const url = `/${pathSnippets.slice(0, index + 1).join('/')}`;
        return (
            <Breadcrumb.Item key={url}>
                <Link to={url}>{breadcrumbNameMap[url]}</Link>
            </Breadcrumb.Item>
        );
    });

    const breadcrumbItems = [
        <Breadcrumb.Item key="Dashboard">
            <Link to="/clinic/dashboard">Home</Link>
        </Breadcrumb.Item>,
    ].concat(extraBreadcrumbItems);

    return (
        <Breadcrumb>{breadcrumbItems}</Breadcrumb>
    )
}

export default AppBreadCrumb;