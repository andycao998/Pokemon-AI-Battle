import React, { useState, useEffect, useRef } from 'react';
import SceneRenderer from './SceneRenderer';
import EventListener from './EventListener';
import InterfaceRenderer from './InterfaceRenderer';
import LoadingDisplay from './LoadingDisplay';

function BattleContainer() {
  const [battleState, setBattleState] = useState([]);
  const controllerRef = useRef();

  const fetchBattleState = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new state fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch('http://localhost:8080/ai/battle/state', {
      headers: {
        Accept: 'application/json'
      },
      signal: signal
    })
      .then(response => response.json())
      .then(data => setBattleState(data))
      .catch(error => console.log(error));

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  };

  useEffect(() => {
    fetchBattleState();
  }, []);

  if (battleState.length === 0) {
    return <LoadingDisplay/>;
  }

  return (
    <>
      <EventListener fetchBattleState = {fetchBattleState} />
      <SceneRenderer battleState = {battleState} />
      <InterfaceRenderer battleState = {battleState} />
    </>
  );
}

export default BattleContainer;