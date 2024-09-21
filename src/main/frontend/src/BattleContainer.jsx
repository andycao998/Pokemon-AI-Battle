import React, { useState, useEffect, useRef } from 'react';
import RenderPokemon from './RenderPokemon';
import EventListener from './EventListener';
import MessageRenderer from './MessageRenderer';
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
      .catch(error => {
        console.log(error);
      });

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  };

  useEffect(() => {
    fetchBattleState();

    // return () => {
    //   controllerRef.current.abort();
    // }
  }, []);

  if (battleState.length === 0) {
    return <LoadingDisplay/>;
  }

  return (
    <>
      <EventListener fetchBattleState = {fetchBattleState} />
      <RenderPokemon battleState = {battleState} />
      <MessageRenderer battleState = {battleState} />
    </>
  );
}

export default BattleContainer;