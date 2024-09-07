import { useEffect } from "react";

function ActionReceiver({action}) {
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

  useEffect(() => {
    if (action) {
      sendAction();
    }
  });
}

export default ActionReceiver;