import React, {useState} from 'react';
import LoginForm from "../forms/LoginForm";
import {LoginFlow, RegisterFlow} from "../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";

const LoginProvider = ({setProvider, setAuthenticated}) => {

    const [flow, setFlow] = useState(LoginFlow.LoginForm);
    return (
        <>
            {flow === LoginFlow.LoginForm && <LoginForm setFlow={setFlow} setProvider={setProvider}/>}
            {flow === LoginFlow.EmailVerification && <EmailVerificationForm cancelHandler={
                ()=>setFlow(LoginFlow.LoginForm)
            }/>}
        </>
    );
};

export default LoginProvider;