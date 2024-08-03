import React, {useState} from 'react';
import {MDBBtn, MDBInput} from "mdb-react-ui-kit";
import {Providers, REGISTER_URL, RegisterFlow} from "../../tools/consts";
import ErrorMessage from "../../tools/ErrorMessage";
import {validateEmail} from "../../tools/functions";

const RegisterForm = ({setProvider, setFlow, userData, setEmail, setPassword, setFirstname, setLastname}) => {

    const [error, setError] = useState(false);
    const [message, setMessage] = useState('');

    const nextHandler = async () => {
        if (userData.email.length === 0 || userData.password.length === 0
            || userData.firstname.length === 0 || userData.lastname.length === 0) {
            setError(true);
            setMessage("Please fill into all fields");
            return;
        }
        if(userData.password.length < 6){
            setError(true);
            setMessage("Password is too short. Please commit something longer");
            return;
        }
        if(userData.firstname.length < 4){
            setError(true);
            setMessage("Name is too short. Please commit something longer");
            return;
        }
        if(userData.lastname.length < 4){
            setError(true);
            setMessage("Lastname is too short. Please commit something longer");
            return;
        }

        const validEmail = validateEmail(userData.email);
        if(!validEmail) {
            setError(true);
            setMessage("Email is invalid. Please try again");
            return;
        }
        fetch(REGISTER_URL, {
            method: "POST",
            body: JSON.stringify(userData),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data =>{
                if(data.success){
                    setFlow(RegisterFlow.EmailVerification);
                }else{
                    setError(true);
                    setError(data.description);
                }
            })
    }

    return (

        <>
            {error && <ErrorMessage message={message} onClose={() => {
                setError(false);
            }}/>}
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
                                            <MDBInput onChange={(event) => setEmail(event.target.value)} type="email"
                                                      id="typeEmailX" label="Email"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput onChange={(event) => setPassword(event.target.value)}
                                                      type="password" id="typePasswordX" label="Password"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput onChange={(event) => setFirstname(event.target.value)} type="text"
                                                      id="typeFirstnameX" label="Firstname"
                                                      className="form-control form-control-lg"/>
                                        </div>
                                        <div className="form-outline form-white mb-4">
                                            <MDBInput onChange={(event) => setLastname(event.target.value)} type="text"
                                                      id="typeLastnameX" label="Lastname"
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