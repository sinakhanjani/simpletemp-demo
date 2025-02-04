import { useTheme } from '@emotion/react';
import { LoadingButton } from '@mui/lab';
import { Card, Checkbox, Grid, TextField, IconButton, InputAdornment  } from '@mui/material';
import { Box, styled } from '@mui/system';
import { Paragraph } from 'app/components/Typography';
import useAuth from 'app/hooks/useAuth';
import { Formik } from 'formik';
import { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import * as Yup from 'yup';
import axios from 'axios.js';
import React from 'react';
import { Button, message, Space, Divider } from 'antd';
import ReactCodeInput from 'react-verification-code-input';
import { Visibility, VisibilityOff } from '@mui/icons-material';


const FlexBox = styled(Box)(() => ({ display: 'flex', alignItems: 'center' }));

const JustifyBox = styled(FlexBox)(() => ({ justifyContent: 'center' }));

const ContentBox = styled(JustifyBox)(() => ({
  height: '100%',
  padding: '32px',
  background: 'rgba(0, 0, 0, 0.01)',
}));

const JWTRegister = styled(JustifyBox)(() => ({
  background: '#1A2038',
  minHeight: '100vh !important',
  '& .card': {
    maxWidth: 800,
    minHeight: 400,
    margin: '1rem',
    display: 'flex',
    borderRadius: 12,
    alignItems: 'center',
  },
}));

// inital login credentials
const initialValues = {
  email: '',
  password: '',
  username: '',
  remember: true,
};

const error = (description) => {
  message.error(description);
};

// form field validation schema
const validationSchema = Yup.object().shape({
  username: Yup.string().required('Clinic name is required!'),
  password: Yup.string()
    .min(6, 'Password must be 8 character length')
    .required('Password is required!'),
  email: Yup.string().email('Invalid Email address').required('Email is required!'),
});

const JwtRegister = () => {
  const theme = useTheme();
  const { codeVerification } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [info, setInfo] = useState(undefined);
  const [showPassword, setShowPassword] = useState(false);

  const register = async (fullname, email, password, callback) => {
    axios.post('/common/register', {
      fullname,
      email,
      password,
      userType: 'clinic'
    }).then((response) => {
      const model = response.data

      callback(model)
    }).catch((e) => {
      error(e.message)
      setLoading(false)
    })
  }

  const handleFormSubmit = async (values) => {
    setLoading(true);

    try {
      register(values.username, values.email, values.password, (model) => {
        if (model?.success) {
          setInfo(values)
          setIsCodeSent(true)
        }
        setLoading(false);
      });

    } catch (e) {
      setLoading(false);
    }
  };

  return (
    <JWTRegister>
      <Card className="card">
        <Grid container style={{ backgroundColor: 'gainsboro' }}>
          <Grid item sm={6} xs={12}>
            <ContentBox>
              <img
                width="100%"
                alt="Register"
                src="/assets/images/illustrations/posting_photo.svg"
              />
            </ContentBox>
          </Grid>

          <Grid item sm={6} xs={12}>
            <Box p={4} height="100%">
              {
                !isCodeSent ?
                  (<Formik
                    onSubmit={handleFormSubmit}
                    initialValues={initialValues}
                    validationSchema={validationSchema}
                  >
                    {({ values, errors, touched, handleChange, handleBlur, handleSubmit }) => (
                      <form onSubmit={handleSubmit}>
                        <TextField
                          fullWidth
                          size="small"
                          type="text"
                          name="username"
                          label="Clinic name"
                          variant="outlined"
                          onBlur={handleBlur}
                          value={values.username}
                          onChange={handleChange}
                          helperText={touched.username && errors.username}
                          error={Boolean(errors.username && touched.username)}
                          sx={{ mb: 3 }}
                        />

                        <TextField
                          fullWidth
                          size="small"
                          type="email"
                          name="email"
                          label="Email"
                          variant="outlined"
                          onBlur={handleBlur}
                          value={values.email}
                          onChange={handleChange}
                          helperText={touched.email && errors.email}
                          error={Boolean(errors.email && touched.email)}
                          sx={{ mb: 3 }}
                        />
                        <TextField
                        fullWidth
                        size="small"
                        name="password"
                        type={showPassword ? 'text' : 'password'}
                        label="Password"
                        variant="outlined"
                        onBlur={handleBlur}
                        value={values.password}
                        onChange={handleChange}
                        helperText={touched.password && errors.password}
                        error={Boolean(errors.password && touched.password)}
                        sx={{ mb: 2 }}
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() => setShowPassword(!showPassword)}
                                edge="end"
                                aria-label="toggle password visibility"
                              >
                                {showPassword ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }}
                      />

                        <Paragraph>
                          By continuing, you agree that you have reviewed and consent to abide by the 
                          <NavLink
                            to="/termsofservice"
                            style={{ color: theme.palette.primary.main, marginLeft: 5 }}
                          >
                            Terms of Service  
                          </NavLink>
                           <span> and</span>
                          <NavLink
                            to="/privacy"
                            style={{ color: theme.palette.primary.main, marginLeft: 5 }}
                          >
                            Privacy Policy
                          </NavLink>
                        </Paragraph>

                        <LoadingButton
                          type="submit"
                          color="primary"
                          loading={loading}
                          variant="contained"
                          sx={{ mb: 2, mt: 3 }}
                        >
                          Sign up
                        </LoadingButton>

                        <Paragraph>
                          Already have an account?
                          <NavLink
                            to="/clinic/session/signin"
                            style={{ color: theme.palette.primary.main, marginLeft: 5 }}
                          >
                            Login
                          </NavLink>
                        </Paragraph>
                      </form>
                    )}
                  </Formik>) :
                  (
                    <div>
                      <Space direction='vertical'>
                        <Divider orientation="left" orientationMargin="0">
                          Verify your email address
                        </Divider>
                        <p>
                          {
                            `We’ve sent a verification code to your email - ${info?.email}`
                          }
                        </p>
                        <ReactCodeInput fieldHeight={40} fieldWidth={40} style={{ fontFamily: 'sans-serif' }} autoFocus={true} onComplete={async (code) => {
                          const success = await codeVerification(info?.email, code, 'clinic')
                          if (success) {
                            navigate('/clinic/')
                          }
                        }} />
                      </Space>

                    </div>
                  )
              }
            </Box>
          </Grid>
        </Grid>
      </Card>
    </JWTRegister>
  );
};

export default JwtRegister;
