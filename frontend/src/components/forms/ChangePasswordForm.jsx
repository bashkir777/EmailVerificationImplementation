import React, {useState} from 'react';
import {MDBInput, MDBBtn} from 'mdb-react-ui-kit';
import ErrorMessage from "../../tools/ErrorMessage";
import {RegisterFlow as ChangePasswordFlow, SEND_OTP_URL} from "../../tools/consts";

const ChangePasswordForm = ({userData, backToLogin, setFlow, setEmail, setNewPassword}) => {

    const [error, setError] = useState(false);
    const [message, setMessage] = useState('');
    const nextHandler = () =>{
        fetch(SEND_OTP_URL + userData.email, {
            method:'GET',
        }).then(response => response.json())
            .then(data => {
                if(data.success){
                    setFlow(ChangePasswordFlow.EmailVerification);
                }else{
                    setError(true);
                    setMessage("Invalid email. Please try again.")
                }
            })
    }

    return (
        <>
            {
                error && <ErrorMessage message={message} onClose={() => {
                    setError(false);
                    setMessage('');
                }
                }/>

            }
            <section className="vh-100 gradient-custom">
                <div className="container py-5 h-100">
                    <div className="row d-flex justify-content-center align-items-center h-100">
                        <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                            <div className="card bg-dark text-white" style={{borderRadius: "1rem"}}>
                                <div className="card-body p-5 text-center">

                                    <div className="mb-md-3 mt-md-4 pb-3">

                                        <h2 className="fw-bold mb-2 text-uppercase">Change password</h2>
                                        <p className="text-white-50 mb-5">Please enter your email and new password!</p>

                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="email"
                                                      onChange={(event)=>setEmail(event.target.value)}
                                                      id="typeEmailX" label="Email"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="password"
                                                      onChange={(event)=>setNewPassword(event.target.value)}
                                                      id="typePasswordX" label="New password"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="container">

                                            <div className="row mx-2">
                                                <MDBBtn outline
                                                        onClick={()=>backToLogin()}
                                                        className=" col btn btn-outline-danger btn-lg px-5 mt-5  col-5"
                                                        type="submit">Cancel</MDBBtn>
                                                <MDBBtn outline
                                                        onClick={nextHandler}
                                                        className="col btn btn-outline-light btn-lg px-5 mt-5 offset-2 col-5"
                                                        type="submit">Next</MDBBtn>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default ChangePasswordForm;