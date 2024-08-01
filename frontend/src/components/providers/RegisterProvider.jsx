import React, {useState} from 'react';
import RegisterForm from "../forms/RegisterForm";
import {RegisterFlow} from "../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";

const RegisterProvider = ({setProvider, setAuthenticated}) => {
    const [flow, setFlow] = useState(RegisterFlow.RegisterForm);
    return (
        <>
            {flow === RegisterFlow.RegisterForm
                && <RegisterForm setProvider={setProvider} setFlow={setFlow}/>}
            {flow === RegisterFlow.EmailVerification
                && <EmailVerificationForm setAuthenticated={setAuthenticated}
                                          cancelHandler={()=>setFlow(RegisterFlow.RegisterForm)}/>}
        </>
    );
};

export default RegisterProvider;