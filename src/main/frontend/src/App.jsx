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

    fetch('http://localhost:8080/ai/battle/start', {
      method: 'POST',
      credentials: 'include',
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

    fetch('http://localhost:8080/ai/battle/session', {
      method: 'GET',
      credentials: 'include',
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

  useEffect(() => {
    checkExistingBattle();
  }, []);

  // Display loading screen until backend finishes setting up battle instance
  if (!battleReady) {
    return (<LoadingDisplay/>)
  }

  return (
    <div>
      <StaticDisplay/>
      <BattleContainer/>
    </div>
  )
}

export default App;
