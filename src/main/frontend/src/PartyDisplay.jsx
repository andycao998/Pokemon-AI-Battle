import {useEffect, useState} from 'react'
import ActionReceiver from './ActionReceiver';

function PartyDisplay({hover, displayOptions, switchable}) {
  const [partyState, setPartyState] = useState([]);
  const [switchTo, setSwitchTo] = useState(null);

  const fetchPartyState = () => {
    fetch('http://localhost:8080/ai/battle/party', {
      headers: {
        Accept: 'application/json'
      }
    })
      .then(response => response.json())
      .then(data => setPartyState(data));
  };

  useEffect(() => {
    fetchPartyState();

    if (switchable) {

    }
    // if (!switchable) {
    //   document.getElementById('returnButton').style.visibility = 'hidden';
    // }
  }, [])

  useEffect(() => {
    if (partyState) {
      updatePartyCards();
      setHoverEvents();
    }
  }, [partyState]);

  const updatePartyCards = () => {
    for (let i = 1; i <= 6; i++) {
      const name = eval('partyState.pokemon' + i + 'Name');
      const hp = eval('partyState.pokemon' + i + 'CurrentHp') + '/' + eval('partyState.pokemon' + i + 'MaxHp');

      document.getElementById('partySlot' + i).innerHTML = name + '<br>' + hp;
    }
  }

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

      if (!switchable) {
        continue;
      }

      document.getElementById('party' + i).addEventListener('mousedown', function() {
        const switchName = eval('partyState.pokemon' + i + 'Name');
        setSwitchTo(switchName);
      });
    }

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
        id = 'party1'
      >
        <img
          id = 'partyImg1'
          src = '/src/assets/pokemon_box_1.png'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '10%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>
        
        <div
          id = 'partySlot1'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '10%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
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
            left: '24%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>

        <div
          id = 'partySlot2'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '24%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
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
            left: '38%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>

        <div
          id = 'partySlot3'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '38%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
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
            left: '52%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>

        <div
          id = 'partySlot4'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '52%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
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
            left: '66%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>

        <div
          id = 'partySlot5'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '66%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
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
            left: '80%',
            width: '10vw',
            imageRendering: 'pixelated'
          }}
        >
        </img>

        <div
          id = 'partySlot6'
          style = {{
            position: 'absolute',
            top: '75%',
            left: '80%',
            width: '10vw',
            fontSize: 24,
            textAlign: 'center',
            lineHeight: 1.6,
            color: 'white'
          }}
        >
        </div>
      </div>

      <div
        style = {{
          position: 'absolute',
          left: '8.6%',
          top: '83%',
          width: '81vw',
          height: '12vh',
          marginLeft: '0.9vw',
          marginRight: '0.9vw',
          textAlign: 'center',
          fontSize: 24,
          lineHeight: 1.2
        }}
      >
        <div
          id = 'detailsBasic'
          style = {{
            position: 'absolute',
            width: '27vw',
            height: '12vh',
            marginTop: '0.4vh'
          }}
        >
          Name: <br></br> CurrentHp/MaxHp <br></br> Status: <br></br> Type:
        </div>

        <div
          id = 'detailsStats'style = {{
            position: 'absolute',
            width: '27vw',
            height: '12vh',
            left: '27vw',
            marginTop: '0.7vh',
            lineHeight: 0.9
          }}
        >
          Attack: <br></br> Defense: <br></br> Sp. Attack: <br></br> Sp. Defense: <br></br> Speed:
        </div>

        <div
          id = 'detailsMoves'
          style = {{
            position: 'absolute',
            width: '27vw',
            height: '12vh',
            left: '54vw',
            marginTop: '0.4vh'
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
          width: '4vw',
          height: 'auto',
          imageRendering: 'pixelated'
        }}
      ></img>

      {/* {switchable && <ActionReceiver action = {'SWITCH'}/>} */}
      {switchTo && switchable && <ActionReceiver action = {switchTo} pokemon = {true}/>}
    </>
  )
}

export default PartyDisplay;