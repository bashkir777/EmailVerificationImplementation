import React from 'react';
import LoginForm from "../LoginForm";

const LoginProvider = ({setProvider, setAuthenticated}) => {

    return (
        <>
            <LoginForm setProvider={setProvider}/>
        </>
    );
};

export default LoginProvider;