import { Box, Button, Card, Grid, styled, TextField } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { message, Space, Divider } from 'antd';
import ReactCodeInput from 'react-verification-code-input';
import useAuth from 'app/hooks/useAuth';
import axios from 'axios.js';

const FlexBox = styled(Box)(() => ({
  display: 'flex',
  alignItems: 'center',
}));

const JustifyBox = styled(FlexBox)(() => ({
  justifyContent: 'center',
}));

const ContentBox = styled(Box)(({ theme }) => ({
  padding: 32,
  background: theme.palette.background.default,
}));

const ForgotPasswordRoot = styled(JustifyBox)(() => ({
  background: '#1A2038',
  minHeight: '100vh !important',
  '& .card': {
    maxWidth: 800,
    margin: '1rem',
    borderRadius: 12,
  },
}));

const ForgotPassword = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('clinic@simpletemp.com');
  const [password, setPassword] = useState(undefined);
  const [confirmPassword, setConfirmPassword] = useState(undefined);

  const [resetStep, setResetStep] = useState(0);
  const { changePassword } = useAuth();
  const [passwordToken, setPasswordToken] = useState(undefined);

  const verifyReq = () => {
    return new Promise(function (resolve, reject) {
      axios.post('/common/sendcode', {
        email,
        userType: 'clinic',
      }).then((response) => {
        const success = response.data.success

        if (success) {
          resolve(success)
        } else {
          reject(false)
        }
      }).catch((e) => {
        message.error(e.message);
        reject(false)
      })
    })
  }

  const secondVerifyReq = function (code) {
    return new Promise(function (resolve, reject) {
      axios.post('/common/verifyCode', {
        email,
        verificationCode: code,
        userType: 'clinic',
      }).then((response) => {
        const success = response.data.success
        const passwordToken = response.data.data.passwordToken

        if (success) {
          setPasswordToken(passwordToken)
          resolve(success)
        } else {
          reject(success)
        }
      }).catch((e) => {
        message.error(e.message);
        reject(false)
      })
    })
  }

  const handleFormSubmit = async (e) => {
    e.preventDefault()

    verifyReq().then((success) => {
      if (success) {
        setResetStep(1)
      }
    }).catch((e) => {
      message.error(e.message);
    })
  };

  const thirdHandleFormSubmit = async (e) => {
    e.preventDefault()

    if (password) {
      if (password === confirmPassword) {
        const success = await changePassword(email, password, passwordToken, 'clinic')
        if (success) {
          navigate('/clinic')
        }
      } else {
        message.error('Passwords are not the same');
      }
    } else {
      message.error('Please enter the password');
    }
  };

  const CPComponent = (step) => {
    switch (step) {
      case 0:
        return (
          <div>
            <Divider orientation="left" orientationMargin="0">
              Reset Password
            </Divider>
            <p>
              {`Enter the email associated with your account and we’ll send an email with instructions to reset your password.`}
            </p>
            <TextField
              type="email"
              name="email"
              size="small"
              label="Email"
              value={email}
              variant="outlined"
              onChange={(e) => setEmail(e.target.value)}
              sx={{ mb: 3, width: '100%' }}
            />

            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Button  variant="contained" color="primary" type="submit" onClick={handleFormSubmit} style={{borderRadius: '15px'}}> 
              Send Verification Code
            </Button>
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
              <Button
                color="primary"
                variant="outlined"
                onClick={() => navigate(-1)}
                sx={{ mt: 2 }}
                style={{borderRadius: '15px'}}
              >
              Go Back
              </Button>
            </div>
          </div>
        )
      case 1:
        return <div>
          <Space direction='vertical'>
            <Divider orientation="left" orientationMargin="0">
              Verify your email address
            </Divider>
            <p>
              {`We’ve sent a verification code to your email - ${email}`}
            </p>
            <ReactCodeInput fieldHeight={40} fieldWidth={40} style={{ fontFamily: 'sans-serif' }} autoFocus={true} onComplete={async (code) => {
              const success = await secondVerifyReq(code)
              if (success) {
                setResetStep(2)
              }
            }} />
          </Space>

        </div>
      case 2:
        return (
          <div>
            <Divider orientation="left" orientationMargin="0">
              Create new password
            </Divider>
            <p>
              {`Your new password must be different from previous used passwords.`}
            </p>
            <TextField
              fullWidth
              size="small"
              name="password"
              type="password"
              label="Password"
              variant="outlined"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              sx={{ mb: 3, width: '100%' }}
            />
            <TextField
              fullWidth
              size="small"
              name="password"
              type="password"
              label="Confirm Password"
              variant="outlined"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              sx={{ mb: 3, width: '100%' }}
            />

            <Button fullWidth variant="contained" color="primary" type="submit" onClick={thirdHandleFormSubmit}>
              Reset Password
            </Button>
            <Button
              fullWidth
              color="primary"
              variant="outlined"
              onClick={() => navigate('/clinic/session/forgot-password')}
              sx={{ mt: 2 }}
            >
              Go Back
            </Button>
          </div>
        )
      default: break
    }
  }

  return (
    <ForgotPasswordRoot>
      <Card className="card">
        <Grid container style={{ backgroundColor: 'gainsboro' }}>
          <Grid item xs={12}>
            <JustifyBox p={4}>
              <img width="300" src="/assets/images/illustrations/dreamer.svg" alt="" />
            </JustifyBox>

            <ContentBox>
              <form>
                {CPComponent(resetStep)}
              </form>
            </ContentBox>
          </Grid>
        </Grid>
      </Card>
    </ForgotPasswordRoot>
  );
};

export default ForgotPassword;
