import React, {useEffect, useRef} from 'react';
import eventBus from './EventBus'

function EventListener({fetchBattleState}) {
  const eventSource = useRef(null)
  const eventQueue = useRef([]); // Queue to hold events
  const readQueue = useRef(false); // Flag to check if the queue is being processed
  const delay = 1500; // Delay in milliseconds between messages

  useEffect(() => {
    if (eventSource.current === null) {
      eventSource.current = new EventSource('http://localhost:8080/subscribe');
    }

    eventSource.current.onmessage = function(event) {
      console.log("Message from server:", event.data);
      eventQueue.current.push(event.data);
      emitEvents();

      // switchOut(event.data);
      // switchIn(event.data);
      // useMove(event.data);
      // checkDamage(event.data);
      // checkFainted(event.data);
      // updateEndOfTurn(event.data);
    };

    // const switchOut = (message) => {
    //   if (String(message).includes(' went back to ')) {
    //     console.log('A pokemon is switching out...');
    //     eventBus.emit('Battle Update', message);
    //   }
    // }

    // const switchIn = (message) => {
    //   if (String(message).includes(' sent out ')) {
    //     console.log('A pokemon is switching in...');
    //     fetchBattleState();
    //     eventBus.emit('Battle Update', message);
    //   }
    // }

    // const useMove = (message) => {
    //   if (String(message).includes(' used ')) {
    //     console.log('A pokemon used a move...');
    //     eventBus.emit('Battle Update', message);
    //   }
    // }

    // const checkDamage = (message) => {
    //   if (String(message).includes(' lost ') && String(message).includes(' HP!')) {
    //     console.log('A Pokemon took damage...');
    //     eventBus.emit('Battle Update', message);
    //   }
    // }
  
    // const checkFainted = (message) => {
    //   if (String(message).includes(' fainted!')) {
    //     console.log('A Pokemon fainted...');
    //     fetchBattleState();
    //     eventBus.emit('Battle Update', message);
    //   }
    // }

    // const updateEndOfTurn = (message) => {
    //   if (message === 'Turn End') {
    //     console.log('Updating turn data...');
    //     fetchBattleState();
    //     eventBus.emit('Battle Update', message);
    //   }
    // }

    eventSource.current.onerror = function(event) {
      console.error("EventSource failed:", event);
      eventSource.current.close();
      eventSource.current = null;
    };
      
    return () => {
      eventSource.current.close();  // Clean up the EventSource on unmount
      eventSource.current = null;
    };
  }, []);

  const emitEvents = () => {
    //console.log(readQueue);
    //console.log(eventQueue.current.length);  
    if (!readQueue.current && eventQueue.current.length > 0) {
      readQueue.current = true;
      const message = eventQueue.current.shift();
      console.log(message);

      readEvent(message);

      setTimeout(() => {
        readQueue.current = false;
        emitEvents();

        if (eventQueue.current.length == 0) {
          fetchBattleState();

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
      //fetchBattleState();
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

export default EventListener