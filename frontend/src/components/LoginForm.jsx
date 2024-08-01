import React from 'react';
import {MDBInput, MDBBtn} from 'mdb-react-ui-kit';
import {Providers} from "../tools/consts";

const LoginForm = ({setProvider}) => {
    return (
        <>
            <section className="vh-100 gradient-custom">
                <div className="container py-5 h-100">
                    <div className="row d-flex justify-content-center align-items-center h-100">
                        <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                            <div className="card bg-dark text-white" style={{borderRadius: "1rem"}}>
                                <div className="card-body p-5 text-center">

                                    <div className="mb-md-5 mt-md-4 pb-5">

                                        <h2 className="fw-bold mb-2 text-uppercase">Login</h2>
                                        <p className="text-white-50 mb-5">Please enter your email and password!</p>

                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="email" id="typeEmailX" label="Email"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput type="password" id="typePasswordX" label="Password"
                                                      className="form-control form-control-lg"/>
                                        </div>

                                        <p className="small mb-5 pb-lg-2"><a className="text-white-50" href="#!"
                                                                             onClick={(event) => {
                                                                                 event.preventDefault();
                                                                                 setProvider(Providers.ChangePasswordProvider);
                                                                             }}>Forgot
                                            password?</a></p>

                                        <MDBBtn outline className="btn btn-outline-light btn-lg px-5"
                                                type="submit">Login</MDBBtn>
                                    </div>

                                    <div>
                                        <p className="mb-0">Don't have an account? <a href="#!" onClick={(event) => {
                                            event.preventDefault();
                                            setProvider(Providers.RegisterProvider);
                                        }}
                                                                                      className="text-white-50 fw-bold">Sign
                                            Up</a>
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

export default LoginForm;