import {useEffect, useRef} from "react";

// POST move choice or Pokemon switch choice to backend
function ActionReceiver({action, pokemon}) {
  const controllerRef = useRef();

  const sendAction = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new move fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch('http://localhost:8080/ai/battle/move', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action: action }),
      signal: signal
    })
      .then(response => response.json())
      .then(data => console.log('Action successfully sent:', data))
      .catch(error => console.log(error));

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  };

  const sendPokemon = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new switch fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch('http://localhost:8080/ai/battle/switch', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action: action }),
      signal: signal
    })
    .then(response => response.json())
    .then(data => {
      console.log('Action successfully sent:', data);
    })
    .catch(error => {
      console.log(error);
    });

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  }

  useEffect(() => {
    if (pokemon && action) {
      sendPokemon();
    }
    else {
      sendAction();
    }

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  }, []);
}

export default ActionReceiver;