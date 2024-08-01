import React from 'react';
import {MDBBtn, MDBInput} from "mdb-react-ui-kit";
import {Providers, RegisterFlow} from "../../tools/consts";

const RegisterForm = ({setProvider, setFlow}) => {

    const nextHandler = () => {
        //send register request + if response is 200 go to email verification
        setFlow(RegisterFlow.EmailVerification)
    }

    return (

        <>
            <section className="vh-100 gradient-custom">
                <div className="container py-5 h-100">
                    <div className="row d-flex justify-content-center align-items-center h-100">
                        <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                            <div className="card bg-dark text-white" style={{borderRadius: "1rem"}}>
                                <div className="card-body p-5 text-center">

                                    <div className="mb-md-5 mt-md-4 pb-2">

                                        <h2 className="fw-bold mb-2 text-uppercase">Register</h2>
                                        <p className="text-white-50 mb-4">Please fill into all fields!</p>

                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="email" id="typeEmailX" label="Email"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="password" id="typePasswordX" label="Password"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="text" id="typeFirstnameX" label="Firstname"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="text" id="typeLastnameX" label="Lastname"
                                                      className="form-control form-control-lg"/>
                                        </div>

                                        <MDBBtn outline className="btn btn-outline-light btn-lg px-5 mt-3"
                                                type="submit" onClick={nextHandler}>Next</MDBBtn>
                                    </div>
                                    <div>
                                        <p className="mb-0">Want to go back to login form? <a href="#!"
                                                                                              onClick={(event) => {
                                                                                                  event.preventDefault();
                                                                                                  setProvider(Providers.LoginProvider);
                                                                                              }}
                                                                                              className="text-white-50 fw-bold">Back</a>
                                        </p>
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

export default RegisterForm;