import React, {useState} from 'react';
import {MDBInput, MDBBtn} from 'mdb-react-ui-kit';
import {LOGIN_URL, LoginFlow, Providers} from "../../tools/consts";
import ErrorMessage from "../../tools/ErrorMessage";
import {validateEmail} from "../../tools/functions";

const LoginForm = ({userData, setEmail, setPassword, setProvider, setFlow}) => {

    const[error, setError] = useState(false);
    const[message, setMessage] = useState('');

    const loginHandler = () => {
        const validEmail = validateEmail(userData.email);
        if(!validEmail) {
            setError(true);
            setMessage("Email is invalid. Please try again");
            return;
        }

        fetch(LOGIN_URL, {
            method: 'POST',
            body: JSON.stringify(userData),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data => {
                if(data.success){
                    setFlow(LoginFlow.EmailVerification);
                }else{
                    setError(true);
                    setMessage('Invalid email or password. Please try again')
                }
            })
    }

    return (
        <>
            {
                error && <ErrorMessage message={message} onClose={() => {
                    setError(false);
                    setMessage('');}
                }/>

            }
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
                                            <MDBInput onChange={(event) => setEmail(event.target.value)} type="email"
                                                      id="typeEmailX" label="Email"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput onChange={(event) => setPassword(event.target.value)} type="password"
                                                      id="typePasswordX" label="Password"
                                                      className="form-control form-control-lg"/>
                                        </div>

                                        <p className="small mb-5 pb-lg-2"><a className="text-white-50" href="#!"
                                                                             onClick={(event) => {
                                                                                 event.preventDefault();
                                                                                 setProvider(Providers.ChangePasswordProvider);
                                                                             }}>Forgot
                                            password?</a></p>

                                        <MDBBtn outline onClick={loginHandler}
                                                className="btn btn-outline-light btn-lg px-5"
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