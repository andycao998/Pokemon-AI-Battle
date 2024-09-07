import { useEffect } from "react";

function ActionReceiver({action, pokemon}) {
  const sendAction = () => {
    fetch('http://localhost:8080/ai/battle/move', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action: action }),
    })
    .then(response => response.json())
    .then(data => {
      console.log('Action successfully sent:', data);
      // Handle any response logic here
    })
    .catch((error) => {
      console.error('Error sending action:', error);
    });
  };

  const sendPokemon = () => {
    fetch('http://localhost:8080/ai/battle/switch', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action: action }),
    })
    .then(response => response.json())
    .then(data => {
      console.log('Action successfully sent:', data);
      // Handle any response logic here
    })
    .catch((error) => {
      console.error('Error sending action:', error);
    });
  }

  useEffect(() => {
    if (pokemon && action) {
      sendPokemon();
    }
    else {
      sendAction();
    }
  }, []);
}

export default ActionReceiver;