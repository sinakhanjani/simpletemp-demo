
import React, { Fragment, useEffect, useState } from 'react'
import { styled } from '@mui/system'
import axios from 'axios.js'
import { Card, Space, Input, Button, Typography } from 'antd';
import { Breadcrumb } from 'app/components'
import { useParams, useNavigate, useSearchParams } from 'react-router-dom'
import { PageHeader, Tag, Statistic, Descriptions, Row, Divider, Select, Col, Comment, Tooltip, Progress, List, Image, message, Upload, UploadProps, Form } from 'antd';
import moment from 'moment';
import { Link } from 'react-router-dom'
import useAuth from 'app/hooks/useAuth';
import Icon from '@ant-design/icons';
import { UploadOutlined } from '@ant-design/icons';
import FormData from 'form-data'
import useApp from 'app/hooks/useApp'
import AppBreadCrumb from 'app/AppBreadCrumb';
import useProfile from 'app/hooks/useProfile';
import { DoubleArrow } from '@mui/icons-material';
import { Modal } from 'antd';

const { Paragraph } = Typography;
const { Meta } = Card

const ContentBox = styled('div')(({ theme }) => ({
  margin: '30px',
  [theme.breakpoints.down('sm')]: {
    margin: '16px',
  },
}))

const ClinicInformation = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [formLayout, setFormLayout] = useState('horizontal');
  const onFormLayoutChange = ({ layout }) => {
    setFormLayout(layout);
  };
  // button
  const [loadings, setLoadings] = useState([]);
  // properties
  const [phone, setPhone] = useState('');
  const [city, setCity] = useState('');
  const [postalcode, setPostalcode] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [primaryDentistName, setprimaryDentistName] = useState('');
  const [primaryDentistCertificationNumber, setPrimaryDentistCertificationNumber] = useState('');
  const { user, fetchProfile } = useAuth()
  const { changeStep } = useProfile()
  const [center, setCenter] = useState();
  const [searchParams, setSearchParams] = useSearchParams();
  const lat = searchParams.get('lat')
  const lng = searchParams.get('lng')

  const enterLoading = (index) => {
    setLoadings((prevLoadings) => {
      const newLoadings = [...prevLoadings];
      newLoadings[index] = true;
      return newLoadings;
    });

    let data = {
      "city": city,
      "postalcode": postalcode,
      "addressLine1": addressLine1,
      "addressLine2": addressLine2,
      "phone": phone,
      "primaryDentistName": primaryDentistName,
      "primaryDentistCertificationNumber": primaryDentistCertificationNumber,
    }

    if (lat && lng) {
      data.coordinate = { latitude: parseFloat(lat), longitude: parseFloat(lng) }
    }

    axios.post(`/clinic/profile/accountinfo`, data).then((response) => {
      const success = response.data?.success

      if (success) {
        setLoadings((prevLoadings) => {
          const newLoadings = [...prevLoadings];
          newLoadings[index] = false;
          return newLoadings;
        });

        fetchProfile()
        changeStep('clinic-Detail')
        setSearchParams(undefined)
      } else {
        Modal.error({
          title: 'Ops!',
          content: response.data?.message ?? 'Please try again!',
        });
      }
    }).catch((e) => {
      setLoadings((prevLoadings) => {
        const newLoadings = [...prevLoadings];
        newLoadings[index] = false;
        return newLoadings;
      });
      Modal.error({
        title: 'Ops!',
        content: e.message ?? 'Please try again!',
      });
    })
  };

  useEffect(() => {
    const data = user.profile.accountInformation

    if (data?.phone) {
      setPhone(data?.phone)
    } else {
      setPhone('+44')
    }

    setCenter({
      lat: data?.coordinate?.latitude,
      lng: data?.coordinate?.longitude
    })
    setCity(data?.city)
    setPostalcode(data?.postalcode)
    setAddressLine1(data?.addressLine1)
    setAddressLine2(data?.addressLine2)
    setprimaryDentistName(data?.primaryDentistName)
    setPrimaryDentistCertificationNumber(data?.primaryDentistCertificationNumber)
  }, [user])

  return (
    <Card title="Clinic Information" bordered={false}>
      <Form
        layout="vertical"
        form={form}
        initialValues={{
          layout: formLayout,
        }}
        onValuesChange={onFormLayoutChange}
      >
        <Row gutter={[16, 16]}>
          <Col span={8}>
            <Form.Item label={"Phone"}>
              <Paragraph editable={{ onChange: setPhone }} style={{ padding: 4 }}>
                {phone}
              </Paragraph>
            </Form.Item>
          </Col>
          <Col span={8}>
            <Form.Item label={"Postal Code"}>
              <Paragraph editable={{ onChange: setPostalcode }} style={{ padding: 4 }}>
                {postalcode}
              </Paragraph>
            </Form.Item>
            <Form.Item label={"Address Line 1"}>
              <Paragraph editable={{ onChange: setAddressLine1 }} style={{ padding: 4 }}>
                {addressLine1}
              </Paragraph>
            </Form.Item>
            <Form.Item label={"Address Line 2"}>
              <Paragraph editable={{ onChange: setAddressLine2 }} style={{ padding: 4 }}>
                {addressLine2}
              </Paragraph>
            </Form.Item>
            <Form.Item label={"City"}>
              <Paragraph editable={{ onChange: setCity }} style={{ padding: 4 }}>
                {city}
              </Paragraph>
            </Form.Item>
          </Col>
          <Col span={8}>
            <Form.Item label={"Primary Dentist Name"}>
              <Paragraph editable={{ onChange: setprimaryDentistName }} style={{ padding: 4 }}>
                {primaryDentistName}
              </Paragraph>
            </Form.Item>
            <Form.Item label={"Primary Dentist Certification Number"}>
              <Paragraph editable={{ onChange: setPrimaryDentistCertificationNumber }} style={{ padding: 4 }}>
                {primaryDentistCertificationNumber}
              </Paragraph>
            </Form.Item>
          </Col>
        </Row>
        <Button type="primary" style={{ borderRadius: '15px' }} loading={loadings[0]} onClick={() => enterLoading(0)}>
          Save
        </Button>
      </Form>
    </Card>

  )
}

export default ClinicInformation
