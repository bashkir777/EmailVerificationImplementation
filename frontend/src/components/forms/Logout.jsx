import React, {useState} from 'react';
import {MDBBtn} from "mdb-react-ui-kit";
import ErrorMessage from "../../tools/ErrorMessage";
import {LOGOUT_URL} from "../../tools/consts";

const Logout = ({setAuthenticated}) => {
    const [error, setError] = useState(false);
    const [message, setMessage] = useState('');
    const logoutHandler = () => {
        let refreshToken = localStorage.getItem("refreshToken");
        if(refreshToken === null || refreshToken === undefined){
            setError(true);
            setMessage('You have not been authenticated');
            return;
        }
        fetch(LOGOUT_URL, {
            method: "POST",
            body: JSON.stringify({
                "refreshToken": refreshToken
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data => {
                if(data.success){
                    console.log("You have been logged out successfully");
                }
            }).finally(() => {
            localStorage.removeItem("refreshToken");
            localStorage.removeItem("accessToken");
            setAuthenticated(false);
        })


    }

    return (
        <>
            {error && <ErrorMessage message={message} onClose={() => {
                setError(false);
                setMessage("");
            }}/>}
            <MDBBtn outline onClick={logoutHandler}
                    className="btn btn-outline-light btn-lg px-5"
                    type="submit">Logout</MDBBtn>
        </>
    );
};

export default Logout;