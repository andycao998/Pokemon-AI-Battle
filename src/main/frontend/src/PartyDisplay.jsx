import {useEffect, useState, useRef} from 'react'
import ActionReceiver from './ActionReceiver';
import LoadingDisplay from './LoadingDisplay';

function PartyDisplay({hover, displayOptions, switchable}) { // Switchable distinguishes between party display (read only) and party display (option to switch)
  const [partyState, setPartyState] = useState([]);
  const [switchTo, setSwitchTo] = useState(null); // Pokemon to switch to (if switchable is true)
  const [visible, setVisibility] = useState(switchable ? 'none' : 'block'); // Toggles visibility for party display (switch display) to disallow fast unintended input
  const controllerRef = useRef();

  // Fetch PartyStateDto contents (only for the Player side)
  const fetchPartyState = () => {
    if (controllerRef.current) {
      controllerRef.current.abort('Aborting: new party fetch request');
    }

    controllerRef.current = new AbortController();
    const signal = controllerRef.current.signal;

    fetch('http://localhost:8080/ai/battle/party', {
      credentials: 'include',
      headers: {
        Accept: 'application/json'
      },
      signal: signal
    })
      .then(response => response.json())
      .then(data => setPartyState(data))
      .catch(error => {
        console.log(error);
      });

    return () => {
      controllerRef.current.abort('Aborting: closed');
    }
  };

  useEffect(() => {
    fetchPartyState();

    // Delay before allowing switch display to be shown
    if (switchable) {
      setTimeout(() => {
        setVisibility('block');
      }, 1000);
    }
  }, [])

  // Update party display with Player's Pokemon and set cards hoverable
  useEffect(() => {
    if (partyState) {
      updatePartyCards();
      setHoverEvents();
    }
  }, [partyState]);

  const updatePartyCards = () => {
    // 6 team members each displaying name and HP
    for (let i = 1; i <= 6; i++) {
      const name = eval('partyState.pokemon' + i + 'Name');
      const hp = eval('partyState.pokemon' + i + 'CurrentHp') + '/' + eval('partyState.pokemon' + i + 'MaxHp');

      document.getElementById('partySlot' + i).innerHTML = name + '<br>' + hp;
    }
  }

  // Content that is shown when a card is hovered over
  const updatePartyDetails = (index) => {
    const name = eval('partyState.pokemon' + index + 'Name');

    const hp = eval('partyState.pokemon' + index + 'CurrentHp') + '/' + eval('partyState.pokemon' + index + 'MaxHp') + ' HP';

    let status = eval('partyState.pokemon' + index + 'Status');
    if (status === '') {
      status = 'Status: None';
    }
    else {
      status = 'Status: ' + status;
    }

    const type = eval('partyState.pokemon' + index + 'Type');

    const attack = 'Attack: ' + eval('partyState.pokemon' + index + 'Attack');
    const defense = 'Defense: ' + eval('partyState.pokemon' + index + 'Defense');
    const spAttack = 'Sp. Attack: ' + eval('partyState.pokemon' + index + 'SpAttack');
    const spDefense = 'Sp. Defense: ' + eval('partyState.pokemon' + index + 'SpDefense');
    const speed = 'Speed: ' + eval('partyState.pokemon' + index + 'Speed');

    const move1 = String(eval('partyState.pokemon' + index + 'Move1')).split(', ')[0];
    const move2 = String(eval('partyState.pokemon' + index + 'Move2')).split(', ')[0];
    const move3 = String(eval('partyState.pokemon' + index + 'Move3')).split(', ')[0];
    const move4 = String(eval('partyState.pokemon' + index + 'Move4')).split(', ')[0];
    
    document.getElementById('detailsBasic').innerHTML = name + '<br>' + hp + '<br>' + status + '<br>' + type;
    document.getElementById('detailsStats').innerHTML = attack + '<br>' + defense + '<br>' + spAttack + '<br>' + spDefense + '<br>' + speed;
    document.getElementById('detailsMoves').innerHTML = move1 + '<br>' + move2 + '<br>' + move3 + '<br>' + move4;
  }

  const setHoverEvents = () => {
    for (let i = 1; i <= 6; i++) {
      document.getElementById('party' + i).addEventListener('mouseover', function() {
        hover('partyImg' + i, true);
        updatePartyDetails(i);
      });
  
      document.getElementById('party' + i).addEventListener('mouseout', function() {
        hover('partyImg' + i, false);
      });

      // Only allow switching through click when on party display with switchable option
      if (!switchable) {
        continue;
      }

      document.getElementById('party' + i).addEventListener('mousedown', function() {
        const switchName = eval('partyState.pokemon' + i + 'Name');
        setSwitchTo(switchName);
      });
    }

    // Disallow returning to options screen if on switch display (backend switching takes two commands including 'SWITCH', which is auto sent when switch display is opened)
    if (switchable) {
      document.getElementById('returnButton').style.visibility = 'hidden';
      return;
    }

    document.getElementById('returnButton').addEventListener('mouseover', function() {
      hover('returnButton', true);
    });

    document.getElementById('returnButton').addEventListener('mouseout', function() {
      hover('returnButton', false);
    });

    document.getElementById('returnButton').addEventListener('mousedown', function() {
      displayOptions();
    });
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
            style = {{
              display: visible
            }}
          >
            <div  
              id = 'party1'
            >
              <img
                id = 'partyImg1'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '9%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>
              
              <div
                id = 'partySlot1'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '9%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div  
              id = 'party2'
            >
              <img
                id = 'partyImg2'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '23%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                id = 'partySlot2'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '23%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div  
              id = 'party3'
            >
              <img
                id = 'partyImg3'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '37%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                id = 'partySlot3'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '37%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div  
              id = 'party4'
            >
              <img
                id = 'partyImg4'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '51%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                id = 'partySlot4'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '51%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div  
              id = 'party5'
            >
              <img
                id = 'partyImg5'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '65%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                id = 'partySlot5'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '65%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div  
              id = 'party6'
            >
              <img
                id = 'partyImg6'
                src = '/src/assets/pokemon_box_1.png'
                style = {{
                  position: 'absolute',
                  top: '75%',
                  left: '79%',
                  width: '12%',
                  zIndex: 2,
                  imageRendering: 'pixelated'
                }}
              >
              </img>

              <div
                id = 'partySlot6'
                style = {{
                  position: 'absolute',
                  top: '75.5%',
                  left: '79%',
                  width: '12%',
                  fontSize: 'calc(1.06vw + 0.6vh)',
                  textAlign: 'center',
                  lineHeight: 1.5,
                  color: 'white',
                  zIndex: 2
                }}
              >
              </div>
            </div>

            <div
              style = {{
                position: 'absolute',
                left: '13%',
                top: '83.5%',
                width: '81%',
                height: '12%',
                marginLeft: '0.9%',
                marginRight: '0.9%',
                textAlign: 'center',
                fontSize: 'calc(0.88vw + 0.5vh)',
                lineHeight: 1.2,
                zIndex: 2
              }}
            >
              <div
                id = 'detailsBasic'
                style = {{
                  position: 'absolute',
                  width: '27%',
                  height: '12%',
                  marginTop: '0.4%',
                  zIndex: 2
                }}
              >
                Name: <br></br> CurrentHp/MaxHp <br></br> Status: <br></br> Type:
              </div>

              <div
                id = 'detailsStats'style = {{
                  position: 'absolute',
                  width: '27%',
                  height: '12%',
                  left: '27%',
                  marginTop: '0.7%',
                  lineHeight: 0.9,
                  zIndex: 2
                }}
              >
                Attack: <br></br> Defense: <br></br> Sp. Attack: <br></br> Sp. Defense: <br></br> Speed:
              </div>

              <div
                id = 'detailsMoves'
                style = {{
                  position: 'absolute',
                  width: '27%',
                  height: '12%',
                  left: '54%',
                  marginTop: '0.4%',
                  zIndex: 2
                }}
              >
                Move1: <br></br> Move2: <br></br> Move3: <br></br> Move4:
              </div>
            </div>

            <img
              id = 'returnButton'
              src = '/src/assets/cancel_1.png'
              style = {{
                position: 'absolute',
                top: '68%',
                left: '90%',
                width: '4%',
                height: 'auto',
                zIndex: 2,
                imageRendering: 'pixelated'
              }}
            />
          </div>

          {switchTo && switchable && <LoadingDisplay/>} {/*If valid switch option and in switch display, set a loading screen to wait for AI's output first*/}
          {switchTo && switchable && <ActionReceiver action = {switchTo} pokemon = {true}/>} {/*Valid switch... send the chosen option to a receiver that will forward to backend*/}
        </div>
      </div>
    </>
  )
}

export default PartyDisplay;