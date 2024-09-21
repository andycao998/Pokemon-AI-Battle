import { useRef } from 'react';
import {useState, useEffect} from 'react'
import eventBus from './EventBus'
import MessageDisplay from './MessageDisplay';
import MovesDisplay from './MovesDisplay';
import OptionsDisplay from './OptionsDisplay';
import PartyDisplay from './PartyDisplay';
import SwitchDisplay from './SwitchDisplay';
import ActionReceiver from './ActionReceiver';
import LoadingDisplay from './LoadingDisplay';

function MessageRenderer({battleState}) {
  const [activeDisplay, setActiveDisplay] = useState(null);
  const switching = useRef(false);
  const battleStateRef = useRef(battleState);

  useEffect(() => {
    const updateBattleState = (data) => {
      const message = data.detail;
      displayMessages(message);
    }

    eventBus.on('Battle Update', updateBattleState);

    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves} displaySwitches = {displaySwitches} displayParty = {displayParty}/>);
    //setDefaultDisplay();
    //console.log(battleState);
    
    return () => {
      eventBus.off('Battle Update', updateBattleState);
    }
  }, []);

  useEffect(() => {
    //console.log(battleState);
    battleStateRef.current = battleState;
  }, [battleState])

  const hover = (id, state) => {
    const element = document.getElementById(id);

    if (document.querySelector('#' + id) == null) {
      return '';
    }

    let source = '';
    
    if (state === true) {
      source = String(element.src).replace('1.png', '2.png');
    }
    else {
      source = String(element.src).replace('2.png', '1.png');
    }

    element.src = source;
  }

  // const setDefaultDisplay = () => {
  //   if (String(document.getElementById('playerHP').innerHTML).split('/')[0] == 0) {
  //     displaySwitches();
  //   }
  //   else {
  //     console.log(document.getElementById('playerHP').innerHTML.split('/')[0]);
  //     displayOptions();
  //   }
  // }

  const displayOptions = () => {
    switching.current = false;
    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves} displaySwitches = {displaySwitches} displayParty = {displayParty}/>);
  }

  const displayMoves = () => {
    //switching.current = false;
    setActiveDisplay(<MovesDisplay battleState = {battleStateRef.current} hover = {hover} displayOptions = {displayOptions}/>);
  }

  const displayParty = () => {
    //switching.current = false;
    setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions} switchable = {false}/>)
  }

  const displaySwitches = (forced) => {
    switching.current = !forced;

    if (!forced) {
      setActiveDisplay(<LoadingDisplay/>);
    }
    else {
      setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions} switchable = {true}/>)// setActiveDisplay(<SwitchDisplay hover = {hover}/>)
    }
    // setTimeout(() => {
    //   setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions} switchable = {true}/>)// setActiveDisplay(<SwitchDisplay hover = {hover}/>)
    // }, 1500);
  }

  const displayMessages = (message) => {
    setActiveDisplay(<MessageDisplay message = {message} displayOptions = {displayOptions} displaySwitches = {displaySwitches}/>);
  }

  return(
    <>
      {/* <div
        id = 'battleText'
        style = {{
          position: 'absolute',
          left: '8.6%',
          top: '72.8%',
          width: '81vw',
          height: '22.7vh',
          marginLeft: '0.9vw',
          marginRight: '0.9vw',
          fontSize: 48,
          visibility: 'hidden'
        }}
      >
        What will your Pokemon do?
      </div> */}
      {switching.current && <ActionReceiver action = {'SWITCH'}/>}
      {console.log(switching.current)}

      {activeDisplay}
      {/* <MovesDisplay battleState = {battleState} hover = {hover}/>
      <OptionsDisplay hover = {hover} displayMoves = {displayMoves}/> */}
    </>
  )
}

export default MessageRenderer;