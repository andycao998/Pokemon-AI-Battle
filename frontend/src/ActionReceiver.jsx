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

    fetch(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}/ai/battle/move`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({id: sessionStorage.getItem('id'), action: action}),
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

    fetch(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}/ai/battle/switch`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({id: sessionStorage.getItem('id'), action: action}),
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