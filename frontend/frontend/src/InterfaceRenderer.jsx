import {useState, useEffect, useRef} from 'react'
import eventBus from './EventBus'
import MessageDisplay from './MessageDisplay';
import MovesDisplay from './MovesDisplay';
import OptionsDisplay from './OptionsDisplay';
import PartyDisplay from './PartyDisplay';
import ActionReceiver from './ActionReceiver';
import LoadingDisplay from './LoadingDisplay';

function InterfaceRenderer({battleState}) {
  const [activeDisplay, setActiveDisplay] = useState(null); // Holds which screen should be shown
  const switching = useRef(false); // Used to send the 'SWITCH' command to backend whenever the user decides to switch on current turn
  const battleStateRef = useRef(battleState);

  useEffect(() => {
    const updateBattleState = (data) => {
      const message = data.detail;
      displayMessages(message); // Display battle events in separate display
    }

    eventBus.on('Battle Update', updateBattleState); // Listens to server sent events from backend

    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves} displaySwitches = {displaySwitches} displayParty = {displayParty}/>);
    
    return () => {
      eventBus.off('Battle Update', updateBattleState);
    }
  }, []);

  useEffect(() => {
    battleStateRef.current = battleState;
  }, [battleState])

  // Lifted function that is passed to displays with interactables
  const hover = (id, state) => {
    const element = document.getElementById(id);

    if (document.querySelector('#' + id) == null) {
      return '';
    }

    let source = '';
    
    if (state === true) {
      source = String(element.src).replace('1.png', '2.png'); // Non-hover state = 1, hover state = 2
    }
    else {
      source = String(element.src).replace('2.png', '1.png');
    }

    element.src = source;
  }

  const displayOptions = () => {
    switching.current = false; // If in options, switch command hasn't been pressed
    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves} displaySwitches = {displaySwitches} displayParty = {displayParty}/>);
  }

  const displayMoves = () => {
    setActiveDisplay(<MovesDisplay battleState = {battleStateRef.current} hover = {hover} displayOptions = {displayOptions}/>);
  }

  const displayParty = () => {
    setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions} switchable = {false}/>)
  }

  const displaySwitches = (forced) => {
    switching.current = !forced; // Switch display can be reached from clicking switch command (non-forced) on options screen or from message screen (forced)

    if (!forced) {
      setActiveDisplay(<LoadingDisplay/>); // Wait if not forced to allow for AI to select option; will be called forcibly afterwards
    }
    else {
      setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions} switchable = {true}/>)
    }
  }

  const displayMessages = (message) => {
    setActiveDisplay(<MessageDisplay message = {message} displayOptions = {displayOptions} displaySwitches = {displaySwitches}/>);
  }

  return(
    <>
      {switching.current && <ActionReceiver action = {'SWITCH'}/>} {/*If switching, send 'SWITCH' command to backend first before any Pokemon is chosen*/}

      {activeDisplay}
    </>
  )
}

export default InterfaceRenderer;