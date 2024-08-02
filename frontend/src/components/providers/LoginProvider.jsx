import React, {useState} from 'react';
import LoginForm from "../forms/LoginForm";
import {LoginFlow} from "../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";

const LoginProvider = ({setProvider, setAuthenticated}) => {

    const [flow, setFlow] = useState(LoginFlow.LoginForm);
    const [userData, setUserData] = useState({
        email: '',
        password: ''
    });

    const setPassword = (newPassword) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                password: newPassword
            };
        })
    }

    const setEmail = (newEmail) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                email: newEmail
            };
        })
    }

    return (
        <>
            {flow === LoginFlow.LoginForm
                &&
                <LoginForm setEmail={setEmail} setPassword={setPassword} userData={userData} setFlow={setFlow}
                           setProvider={setProvider}/>}
            {flow === LoginFlow.EmailVerification &&
                <EmailVerificationForm setAuthenticated={setAuthenticated} userData={userData} cancelHandler={
                    () => setFlow(LoginFlow.LoginForm)
                }/>}
        </>
    );
};

export default LoginProvider;