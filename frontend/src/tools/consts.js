export const Providers = {
    LoginProvider: 'login-provider',
    RegisterProvider: 'register-provider',
    ChangePasswordProvider: 'change-password-provider',
}
export const RegisterFlow = {
    RegisterForm: 'register-form',
    EmailVerification: 'email-verification'
}

export const LoginFlow = {
    LoginForm: 'login-form',
    EmailVerification: 'email-verification'
}
export const ResetPasswordFlow = {
    ResetPasswordForm: 'reset-password-provider',
    EmailVerification: 'email-verification'
}

const AUTH_URL = '/api/v1/auth';
export const LOGIN_URL = AUTH_URL + '/login'
export const LOGOUT_URL = AUTH_URL + '/logout'
export const REGISTER_URL = AUTH_URL + '/register'
export const VERIFY_OTP_URL = AUTH_URL + '/verify-otp'
export const RESET_PASSWORD_URL = AUTH_URL + '/reset-password'
export const SEND_OTP_URL = AUTH_URL + '/send-otp/'