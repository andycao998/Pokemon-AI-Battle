import {useEffect} from 'react'

function MessageDisplay({message, displayOptions, displaySwitches}) {
  // Message passed down from InterfaceRenderer (which captures SSEs)
  useEffect(() => {
    updateMessage();
  }, [message])

  const updateMessage = () => {
    if (message === 'Finished Turn') {
      setNextDisplay();
      return;
    }

    document.getElementById('battleMessages').innerHTML = message;
  }

  const setNextDisplay = () => {
    const currentPlayerPokemonHp = String(document.getElementById('playerHP').innerHTML).split('/')[0];
    if (document.getElementById('playerPokemon').dataset.switching === 'true' || currentPlayerPokemonHp == 0) { // If switching due to choice or forcibly from a Pokemon fainting
      displaySwitches(true);
    }
    else {
      displayOptions();
    }
  }

  return(
    <>
      <div
        id = 'battleMessages'
        style = {{
          position: 'absolute',
          left: '8.6%',
          top: '72.8%',
          width: '81vw',
          height: '22.7vh',
          marginLeft: '0.9vw',
          marginRight: '0.9vw',
          fontSize: 48
        }}
      > 
      </div>
    </>
  )
}

export default MessageDisplay;