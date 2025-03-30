import {useEffect, useState, useRef} from 'react'
import StaticDisplay from './StaticDisplay.jsx'
import BattleContainer from './BattleContainer.jsx'
import LoadingDisplay from './LoadingDisplay.jsx';

function App() {
  const [battleReady, setBattleReady] = useState(false);
  const controllerRef = useRef();

  // Frontend notifies backend to start a battle instance
  const startBattle = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new state fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}/ai/battle/start?id=${sessionStorage.getItem('id')}`, {
      method: 'POST',
      headers: {
        Accept: 'application/json',
      },
      cache: 'no-cache',
      signal: signal
    })
      .then((response) => response.text())
      .then((data) => {setBattleReady(true)})
      .catch((error) => console.log(error));

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  };

  const updateSessionStorage = (battleExists) => {
    console.log(battleExists);
    if (!battleExists) {
      sessionStorage.removeItem('battleStarted');
    }

    let started = sessionStorage.getItem('battleStarted');
    console.log(started)

    // Prepare new battle if current session doesn't already have a battle (main.jsx StrictMode)
    if (!started) {
      startBattle();
      sessionStorage.setItem('battleStarted', 'true');
    }
  } 

  const checkExistingBattle = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new state fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}/ai/battle/session?id=${sessionStorage.getItem('id')}`, {
      method: 'GET',
      headers: {
        Accept: 'application/json'
      },
      signal: signal
    })
      .then((response) => response.json())
      .then((battleExists) => updateSessionStorage(battleExists))
      .catch((error) => console.log(error));

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  }

  const generateId = () => {
    const id = Date.now().toString() + Math.random().toString(6).substring(2);
    sessionStorage.setItem('id', id);
  }

  useEffect(() => {
    generateId();
    checkExistingBattle();
  }, []);

  // Display loading screen until backend finishes setting up battle instance
  if (!battleReady) {
    return (
      <div>
        <StaticDisplay/>
        <LoadingDisplay/>
      </div>
    )
  }

  return (
    <div>
      <StaticDisplay/>
      <BattleContainer/>
    </div>
  )
}

export default App;
