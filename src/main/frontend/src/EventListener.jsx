import React, {useEffect, useRef} from 'react';
import eventBus from './EventBus'

// Event bus implementation to emit events to InterfaceRenderer
function EventListener({fetchBattleState}) {
  const eventSource = useRef(null)
  const eventQueue = useRef([]); // Queue to hold events
  const readQueue = useRef(false); // Flag to check if the queue is being processed
  const delay = 1500; // Delay between messages

  useEffect(() => {
    if (eventSource.current === null) {
      eventSource.current = new EventSource('http://localhost:8080/subscribe', {withCredentials: true});
    }

    eventSource.current.onmessage = function(event) {
      console.log('Message from server:', event.data);
      eventQueue.current.push(event.data);
      emitEvents();
    };

    eventSource.current.onerror = function(event) {
      console.error('EventSource failed:', event);
      eventSource.current.close();
      eventSource.current = null;
    };

    // Attempt to clean up
    window.addEventListener("beforeunload", () => {
      sessionStorage.removeItem("battleStarted");
      navigator.sendBeacon('http://localhost:8080/ai/battle/end'), {withCredentials: true};
    });
      
    return () => {
      eventSource.current.close();
      eventSource.current = null;
    };
  }, []);

  const emitEvents = () => { 
    if (!readQueue.current && eventQueue.current.length > 0) {
      readQueue.current = true;
      const message = eventQueue.current.shift();
      console.log(message);

      readEvent(message);

      // Delay messages to allow animations to finish before next message
      setTimeout(() => {
        readQueue.current = false;
        emitEvents();

        if (eventQueue.current.length == 0) {
          fetchBattleState();

          // Slightly longer delay on last message to allow previous to finish first
          setTimeout(() => {
            eventBus.emit('Battle Update', 'Finished Turn');
          }, delay + 250);
        } 
      }, delay);

    }
  }

  const readEvent = (message) => {
    if (message.includes(' went back to ')) {
      console.log('A pokemon is switching out...');
      fetchBattleState();
      eventBus.emit('Battle Update', message);
    } 
    else if (message.includes(' sent out ')) {
      console.log('A pokemon is switching in...');
      fetchBattleState();
      eventBus.emit('Battle Update', message);
    } 
    else if (message.includes(' used ')) {
      console.log('A pokemon used a move...');
      fetchBattleState();
      eventBus.emit('Battle Update', message);
    } 
    else if (message.includes(' lost ') && message.includes(' HP!')) {
      console.log('A Pokemon took damage...');
      eventBus.emit('Battle Update', message);
    } 
    else if (message.includes(' fainted!')) {
      console.log('A Pokemon fainted...');
      eventBus.emit('Battle Update', message);
    } 
    else if (message === 'Turn End') {
      console.log('Updating turn data...');
      fetchBattleState();
      eventBus.emit('Battle Update', message);
    }
    else {
      eventBus.emit('Battle Update', message);
    }
  }

  return (<></>)
}

export default EventListener;