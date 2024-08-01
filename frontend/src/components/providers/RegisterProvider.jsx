import React from 'react';
import RegisterForm from "../forms/RegisterForm";

const RegisterProvider = ({setProvider, setAuthenticated}) => {
    return (
        <>
            <RegisterForm setProvider={setProvider}/>
        </>
    );
};

export default RegisterProvider;