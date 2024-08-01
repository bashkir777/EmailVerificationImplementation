import React, {useEffect, useState, useRef} from 'react';
import ErrorMessage from "../../tools/ErrorMessage";
import {MDBBtn} from "mdb-react-ui-kit";

const EmailVerificationForm = () => {
    const [otp, setOtp] = useState(new Array(6).fill(""));
    const inputRefs = useRef([]);
    const digits = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    const [readyToSubmit, setReadyToSubmit] = useState(false);
    const [error, setError] = useState(false);
    const [message, setErrorMessage] = useState('');

    useEffect(() => {
        if (inputRefs.current[0]) {
            inputRefs.current[0].focus();
        }
    }, []);

    function validateOTP() {
        for (let char of otp) {
            if (char === "" || !(char in digits)) {
                return false;
            }
        }
        return true;
    }

    function otpToString() {
        let res = "";
        for (let char of otp) {
            res += char;
        }
        return res;
    }

    const submitHandler = () => {

    };


    const handleKeyDown = (element, index, event) => {
        if (event.key in digits) {
            setOtp((prevState) => {
                let newOtp = [...prevState];
                newOtp[index] = event.key;
                return newOtp;
            })
            if (index < 5) {
                element.nextSibling.focus();
            }
        } else if (event.key === "Backspace") {
            if (element.value === "" && index !== 0) {
                element.previousSibling.focus();
            } else {
                setOtp((prevState) => {
                    let newOtp = [...prevState];
                    newOtp[index] = "";
                    return newOtp;
                })
            }
        }

    };

    return (
        <section className="vh-100 gradient-custom">
            {error && <ErrorMessage message={message} onClose={() => setError(false)}/>}
            <div className="container pt-5">
                <div
                    className="container d-flex justify-content-center align-items-center w-50 px-4 py-5 text-center bg-dark text-white rounded-top-4 h2 mb-0 mt-5">
                    <div className="row rounded-3 ms-2 mx-0">
                        <div className="col text-center  px-0">
                            Enter the 6 digits just sent to your email
                        </div>
                    </div>
                </div>
                <div className="container-sm px-3 pt-2 pb-5 text-center bg-dark text-white rounded-bottom-4 h2 w-50 h-100">
                    <div className="row rounded-3 m-0">
                        <div className="col text-center">
                            <div className="form-group d-flex justify-content-center">
                                {otp.map((data, index) => (
                                    <input
                                        className="otp-input form-control mx-1 text-center fs-2"
                                        type="text"
                                        name="otp"
                                        maxLength="1"
                                        key={index}
                                        value={data}
                                        onChange={() => {
                                        }}
                                        onKeyDown={e => handleKeyDown(e.target, index, e)}
                                        onFocus={e => e.target.select()}
                                        ref={el => inputRefs.current[index] = el}
                                    />
                                ))}
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <MDBBtn outline className=" col btn btn-outline-danger btn-lg px-5 mt-5 offset-1 col-4"
                                type="submit" onClick={submitHandler}>Cancel</MDBBtn>
                        <MDBBtn outline className=" col btn btn-outline-light btn-lg px-5 mt-5 offset-2 col-4"
                                type="submit" onClick={submitHandler}>Submit</MDBBtn>
                    </div>

                </div>
            </div>
            );
        </section>)
};

export default EmailVerificationForm;