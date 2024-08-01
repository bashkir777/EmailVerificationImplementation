import LoginForm from "./components/forms/LoginForm";
import {useState} from "react";
import {Providers} from "./tools/consts";
import LoginProvider from "./components/providers/LoginProvider";
import RegisterProvider from "./components/providers/RegisterProvider";
import ChangePasswordProvider from "./components/providers/ChangePasswordProvider";

function App() {
    const[authenticated, setAuthenticated] = useState(false);
    const [provider, setProvider] = useState(Providers.LoginProvider);
    return (
        <>
            {!authenticated
                && provider === Providers.LoginProvider
                && <LoginProvider setProvider={setProvider}
                                  setAuthenticated={setAuthenticated}/>}
            {!authenticated
                && provider === Providers.RegisterProvider
                && <RegisterProvider setProvider={setProvider}
                                     setAuthenticated={setAuthenticated}/>}
            {!authenticated
                && provider === Providers.ChangePasswordProvider
                && <ChangePasswordProvider setProvider={setProvider}/>}
        </>
    );
}

export default App;
