import {useEffect} from 'react'

function OptionsDisplay({hover, displayMoves, displaySwitches, displayParty}) { // Display switching occurs in InterfaceRenderer; receives callbacks to switch displays from buttons
  // Default screen where three commands can be chosen: Fight (use move), Switch (display with switch option), and Party (display only) 
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
      displaySwitches(false);
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
            id = 'optionText'
            style = {{
              position: 'absolute',
              left: '3.5%',
              top: '72.8%',
              width: '91.2%',
              height: '22.9%',
              marginLeft: '0.9%',
              marginRight: '0.9%',
              fontSize: 'calc(2vw + 1.125vh)',
              zIndex: 2
            }}
          >
            <div
              style = {{
                width: '40.5vw',
                height: '22.7vh',
                textAlign: 'center',
                zIndex: 2
              }}
            >
              What will your Pokemon do?
            </div>
            
            <div
              id = 'commandFight'
            >
              <img
                id = 'optionFight'
                src = '/assets/Options/command_move_1.png'
                style = {{
                  position: 'absolute', 
                  top: '7%', 
                  left: '53%', 
                  width: '15%',
                  height: 'auto',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                style = {{
                  position: 'absolute', 
                  top: '8%', 
                  left: '50.5%', 
                  width: '20%',
                  height: 'auto',
                  color: 'white',
                  textAlign: 'center',
                  zIndex: 2
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
                src = '/assets/Options/command_switch_1.png'
                style = {{
                  position: 'absolute', 
                  top: '7%', 
                  left: '78%', 
                  width: '15%',
                  height: 'auto',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                style = {{
                  position: 'absolute', 
                  top: '8%', 
                  left: '75.5%', 
                  width: '20%',
                  height: 'auto',
                  color: 'white',
                  textAlign: 'center',
                  zIndex: 2
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
                src = '/assets/Options/command_party_1.png'
                style = {{
                  position: 'absolute', 
                  top: '54%', 
                  left: '65.5%', 
                  width: '15%',
                  height: 'auto',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>
              
              <div
                style = {{
                  position: 'absolute', 
                  top: '55%', 
                  left: '63%', 
                  width: '20%',
                  height: 'auto',
                  color: 'white',
                  textAlign: 'center',
                  zIndex: 2
                }}
              >
                Party
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default OptionsDisplay;