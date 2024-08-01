import React from 'react';
import LoginForm from "../forms/LoginForm";

const LoginProvider = ({setProvider, setAuthenticated}) => {

    return (
        <>
            <LoginForm setProvider={setProvider}/>
        </>
    );
};

export default LoginProvider;