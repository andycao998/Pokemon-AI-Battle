import {useEffect} from 'react'
import ActionReceiver from './ActionReceiver';

function OptionsDisplay({hover, displayMoves, displaySwitches, displayParty}) {
  useEffect(() => {
    document.getElementById('commandFight').addEventListener('mouseover', function() {
      hover('optionFight', true);
    });

    document.getElementById('commandFight').addEventListener('mouseout', function() {
      hover('optionFight', false);
    });

    document.getElementById('commandFight').addEventListener('mousedown', function() {
      displayMoves();
    });

    document.getElementById('commandSwitch').addEventListener('mouseover', function() {
      hover('optionSwitch', true);
    });

    document.getElementById('commandSwitch').addEventListener('mouseout', function() {
      hover('optionSwitch', false);
    });

    document.getElementById('commandSwitch').addEventListener('mousedown', function() {
      displaySwitches();
    })

    document.getElementById('commandParty').addEventListener('mouseover', function() {
      hover('optionParty', true);
    });

    document.getElementById('commandParty').addEventListener('mouseout', function() {
      hover('optionParty', false);
    });

    document.getElementById('commandParty').addEventListener('mousedown', function() {
      displayParty();
    });
  }, [displayParty])

  return(
    <>
      <div
        id = 'optionText'
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
        <div
          style = {{
            width: '40.5vw',
            height: '22.7vh',
            textAlign: 'center',
          }}
        >
          What will your Pokemon do?
        </div>
        
        <div
          id = 'commandFight'
        >
          <img
            id = 'optionFight'
            src = '/src/assets/Options/command_move_1.png'
            style = {{
              position: 'absolute', 
              top: '2%', 
              left: '53%', 
              width: '15vw',
              height: 'auto',
              imageRendering: 'pixelated'
            }}
          >
          </img>

          <div
            style = {{
              position: 'absolute', 
              top: '8%', 
              left: '50%', 
              width: '20vw',
              height: 'auto',
              color: 'white',
              textAlign: 'center',
            }}
          >
            Fight
          </div>
        </div>
        
        <div
          id = 'commandSwitch'
        >
          <img
            id = 'optionSwitch'
            src = '/src/assets/Options/command_switch_1.png'
            style = {{
              position: 'absolute', 
              top: '2%', 
              left: '78%', 
              width: '15vw',
              height: 'auto',
              imageRendering: 'pixelated'
            }}
          >
          </img>

          <div
            style = {{
              position: 'absolute', 
              top: '8%', 
              left: '75%', 
              width: '20vw',
              height: 'auto',
              color: 'white',
              textAlign: 'center'
            }}
          >
            Switch
          </div>
        </div>

        <div
          id = 'commandParty'
        >
          <img
            id = 'optionParty'
            src = '/src/assets/Options/command_party_1.png'
            style = {{
              position: 'absolute', 
              top: '51%', 
              left: '65.5%', 
              width: '15vw',
              height: 'auto',
              imageRendering: 'pixelated'
            }}
          >
          </img>
          
          <div
            style = {{
              position: 'absolute', 
              top: '56%', 
              left: '62.5%', 
              width: '20vw',
              height: 'auto',
              color: 'white',
              textAlign: 'center'
            }}
          >
            Party
          </div>
        </div>
      </div>
    </>
  )
}

export default OptionsDisplay;