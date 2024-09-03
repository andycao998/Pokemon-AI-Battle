import {useState, useEffect} from 'react'
import eventBus from './EventBus'
import MessageDisplay from './MessageDisplay';
import MovesDisplay from './MovesDisplay';
import OptionsDisplay from './OptionsDisplay';
import PartyDisplay from './PartyDisplay';

function MessageRenderer({battleState}) {
  const [activeDisplay, setActiveDisplay] = useState(null)

  useEffect(() => {
    const updateBattleState = (data) => {
      const message = data.detail;
      displayMessages(message);
    }

    eventBus.on('Battle Update', updateBattleState);

    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves}/>);
    
    return () => {
      eventBus.off('Battle Update', updateBattleState);
    }
  }, [setActiveDisplay])

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

  const displayOptions = () => {
    setActiveDisplay(<OptionsDisplay hover = {hover} displayMoves = {displayMoves} displayParty = {displayParty}/>);
  }

  const displayMoves = () => {
    setActiveDisplay(<MovesDisplay battleState = {battleState} hover = {hover} displayOptions = {displayOptions}/>);
  }

  const displayParty = () => {
    setActiveDisplay(<PartyDisplay hover = {hover} displayOptions = {displayOptions}/>)
  }

  const displayMessages = (message) => {
    setActiveDisplay(<MessageDisplay message = {message} displayOptions = {displayOptions}/>);
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
      {activeDisplay}
      {/* <MovesDisplay battleState = {battleState} hover = {hover}/>
      <OptionsDisplay hover = {hover} displayMoves = {displayMoves}/> */}
    </>
  )
}

export default MessageRenderer;