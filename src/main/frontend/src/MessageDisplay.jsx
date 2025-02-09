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
        style={{
          position: 'absolute',
          top: '0%',
          left: '0%',
          display: 'flex',
          justifyContent: 'center',
          height: '100vh',
          width: '100vw',
          overflow: 'hidden'
        }}
      >
        <div
          style={{
            position: 'relative',
            width: '100vw',
            height: '100vh',
            maxHeight: 'calc(100vw * 9 / 16)',
            maxWidth: 'calc(100vh * 16 / 9)',
            backgroundPosition: 'center',
            margin: 'auto'
          }}
        >
          <div
            id = 'battleMessages'
            style = {{
              position: 'absolute',
              left: '3.5%',
              top: '72.8%',
              width: '81%',
              height: '22.7%',
              marginLeft: '0.9%',
              marginRight: '0.9%',
              fontSize: 'calc(2vw + 1.125vh)',
              zIndex: 2
            }}
          > 
          </div>
        </div>
      </div>
    </>
  )
}

export default MessageDisplay;