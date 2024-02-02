import "./App.css";
import { useState } from 'react';
import LoginForm from "./LoginForm";
import DestinationForm from "./DestinationForm"

function App() {

  const [isLoggedIn, setLoggedIn] = useState(false);

  return(
    <main>
      <LoginForm isLoggedIn={isLoggedIn} onLogin={() => setLoggedIn(true)}/>
      <DestinationForm isLoggedIn={isLoggedIn} />
    </main>
  );

}

export default App;
