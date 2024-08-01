export const Providers = {
    LoginProvider: 'login-provider',
    RegisterProvider: 'register-provider',
    ChangePasswordProvider: 'change-password-provider',
}
export const RegisterFlow = {
    RegisterForm: 'register-form',
    EmailVerification: 'email-verification'
}


const AUTH_URL = '/api/v1/auth';
export const LOGIN_URL = AUTH_URL + '/login'
export const REGISTER_URL = AUTH_URL + '/register'
export const VERIFY_OTP_URL = AUTH_URL + '/verify-otp'