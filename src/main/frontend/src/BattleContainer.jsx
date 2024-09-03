import React, { useState, useEffect } from 'react';
import RenderPokemon from './RenderPokemon';
import EventListener from './EventListener';
import MessageRenderer from './MessageRenderer';

function BattleContainer() {
  const [battleState, setBattleState] = useState([]);

  const fetchBattleState = () => {
    fetch('http://localhost:8080/ai/battle/state', {
      headers: {
        Accept: 'application/json'
      }
    })
      .then(response => response.json())
      .then(data => setBattleState(data));
  };

  useEffect(() => {
    fetchBattleState();
  }, []);

  if (battleState.length === 0) {
    return <div>Loading...</div>; // Or any other placeholder
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