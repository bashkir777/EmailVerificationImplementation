import React, {useState} from 'react';
import RegisterForm from "../forms/RegisterForm";
import {RegisterFlow, VERIFY_OTP_URL} from "../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";

const RegisterProvider = ({setProvider, setAuthenticated}) => {
    const [flow, setFlow] = useState(RegisterFlow.RegisterForm);
    const [userData, setUserData] = useState({
        email: '',
        password: '',
        firstname: '',
        lastname: ''
    });

    const setEmail = (newEmail) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                email: newEmail
            }
        })
    }

    const setPassword = (newPassword) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                password: newPassword
            }
        })
    }

    const setFirstname = (newFirstname) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                firstname: newFirstname
            }
        })
    }

    const setLastname = (newLastname) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                lastname: newLastname
            }
        })
    }

    return (
        <>
            {flow === RegisterFlow.RegisterForm
                &&
                <RegisterForm setProvider={setProvider} userData={userData} setFlow={setFlow} setEmail={setEmail} setPassword={setPassword}
                              setFirstname={setFirstname} setLastname={setLastname}/>}
            {flow === RegisterFlow.EmailVerification
                && <EmailVerificationForm URL={VERIFY_OTP_URL} userData={{email: userData.email}} setAuthenticated={setAuthenticated}
                                          cancelHandler={() => setFlow(RegisterFlow.RegisterForm)}/>}
        </>
    );
};

export default RegisterProvider;