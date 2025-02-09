import {useEffect, useState} from 'react'
import ActionReceiver from './ActionReceiver';
import LoadingDisplay from './LoadingDisplay';

function MovesDisplay({battleState, hover, displayOptions}) {
  const [move, setMove] = useState(null);

  // Move info updated every time battleState is updated (refresh new Pokemon's moves visually)
  useEffect(() => {
    const move1 = String(battleState.playerPokemonMove1).split(', ');
    const move2 = String(battleState.playerPokemonMove2).split(', ');
    const move3 = String(battleState.playerPokemonMove3).split(', ');
    const move4 = String(battleState.playerPokemonMove4).split(', ');

    setBorderAndText(1, move1[0], move1[1]);
    setBorderAndText(2, move2[0], move2[1]);
    setBorderAndText(3, move3[0], move3[1]);
    setBorderAndText(4, move4[0], move4[1]);

    setHoverEvents();
  }, [battleState])

  const setBorderAndText = (moveNum, name, type) => {
    const moveBorder = document.getElementById('move' + moveNum + 'Img');
    const moveDiv = document.getElementById('move' + moveNum + 'Text');

    moveBorder.src = 'src/assets/Move_Borders/move_' + String(type).toLowerCase() + '_1.png'; // Move border is the colored box indicating move type (Ex: red = Fire type)
    moveDiv.textContent = name;
  }

  // Update move details when hovered over
  const updateSelectedMoveDetails = (moveNum) => {
    let move = '';

    if (moveNum == 1) {
      move = String(battleState.playerPokemonMove1).split(', ');
    }
    else if (moveNum == 2) {
      move = String(battleState.playerPokemonMove2).split(', ');
    }
    else if (moveNum == 3) {
      move = String(battleState.playerPokemonMove3).split(', ');
    }
    else {
      move = String(battleState.playerPokemonMove4).split(', ');
    }

    const name = move[0];
    const category = move[4];
    const type = move[1];
    const currentPp = move[2];
    const totalPp = move[3];

    document.getElementById('moveNameCategory').textContent = name + ': ' + category;
    document.getElementById('moveType').src = '/src/assets/Type_Icons/icon_' + String(type).toLowerCase() + '.png';
    document.getElementById('movePp').textContent = currentPp + '/' + totalPp + ' PP';
  }

  const setHoverEvents = () => {
    for (let i = 1; i <= 4; i++) {
      const moveInfo = String(eval('battleState.playerPokemonMove' + i)).split(', ');
      const moveName = moveInfo[0];
      const movePp = moveInfo[2];

      // If move has 0 PP (can't be used anymore), don't update on hover
      if (movePp == 0) {
        continue;
      }

      document.getElementById('move' + i).addEventListener('mouseover', function() {
        hover('move' + i + 'Img', true);
        updateSelectedMoveDetails(i);
      });
  
      document.getElementById('move' + i).addEventListener('mouseout', function() {
        hover('move' + i + 'Img', false);
      });

      document.getElementById('move' + i).addEventListener('mousedown', function() {
        setMove(moveName);
      }); 
    }

    document.getElementById('returnButton').addEventListener('mouseover', function() {
      hover('returnButton', true);
    });

    document.getElementById('returnButton').addEventListener('mouseout', function() {
      hover('returnButton', false);
    });

    // Return back to options menu
    document.getElementById('returnButton').addEventListener('mousedown', function() {
      displayOptions();
    });
  }

  return (
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
            id = 'move1'
          >
            <img
              id = 'move1Img'
              src = '/src/assets/Move_Borders/move_normal_1.png'
              style = {{ 
                position: 'absolute', 
                top: '75%', 
                left: '5%', 
                width: '21%',
                height: 'auto',
                zIndex: 2,
                imageRendering: 'pixelated'
              }}
            />

            <div 
              id = 'move1Text'
              style = {{
                position: 'absolute',
                top: '74.5%', 
                left: '5%', 
                width: '21%',
                height: '7%',
                lineHeight: '9.5vh',
                verticalAlign: 'middle',
                fontSize: 'calc(1.33vw + 0.75vh)',
                textAlign: 'center',
                zIndex: 2,
              }}
            >
            </div>
          </div>

          <div
            id = 'move2'
          >
            <img
              id = 'move2Img'
              src = '/src/assets/Move_Borders/move_normal_1.png'
              style = {{ 
                position: 'absolute', 
                top: '75%', 
                left: '26.5%', 
                width: '21%',
                height: 'auto',
                zIndex: 2,
                imageRendering: 'pixelated'
              }}
            />

            <div 
              id = 'move2Text'
              style = {{
                position: 'absolute',
                top: '74.5%',
                left: '26.5%',
                width: '21%',
                height: '7%',
                lineHeight: '9.5vh',
                verticalAlign: 'middle',
                fontSize: 'calc(1.33vw + 0.75vh)',
                textAlign: 'center',
                zIndex: 2
              }}
            >
            </div>
          </div>

          <div
            id = 'move3'
          >
            <img
              id = 'move3Img'
              src = '/src/assets/Move_Borders/move_normal_1.png'
              style = {{ 
                position: 'absolute', 
                top: '84.5%', 
                left: '5%', 
                width: '21%',
                height: 'auto',
                zIndex: 2,
                imageRendering: 'pixelated'
              }}
            />

            <div 
              id = 'move3Text'
              style = {{
                position: 'absolute',
                top: '84%',
                left: '5%',
                width: '21%',
                height: '7%',
                lineHeight: '9.5vh',
                verticalAlign: 'middle',
                fontSize: 'calc(1.33vw + 0.75vh)',
                textAlign: 'center',
                zIndex: 2
              }}
            >
            </div>
          </div>

          <div
            id = 'move4'
          >
            <img
              id = 'move4Img'
              src = '/src/assets/Move_Borders/move_normal_1.png'
              style = {{ 
                position: 'absolute', 
                top: '84.5%', 
                left: '26.5%', 
                width: '21%',
                height: 'auto',
                zIndex: 2,
                imageRendering: 'pixelated'
              }}
            />

            <div 
              id = 'move4Text'
              style = {{
                position: 'absolute',
                top: '84%',
                left: '26.5%',
                width: '21%',
                height: '7%',
                lineHeight: '9.5vh',
                verticalAlign: 'middle',
                fontSize: 'calc(1.33vw + 0.75vh)',
                textAlign: 'center',
                zIndex: 2
              }}
            >
            </div>
          </div>

          <img
            src = '/src/assets/overlay_message.png'
            style = {{
              position: 'absolute',
              top: '72%',
              left: '48.5%',
              width: '47%',
              height: '23.3%',
              zIndex: 2,
              imageRendering: 'pixelated'
            }}
          >
          </img>

          <div
            id = 'moveNameCategory'
            style = {{
              position: 'absolute',
              top: '76.5%',
              left: '53%',
              width: '38%',
              height: '23.2%',
              fontSize: 'calc(2vw + 1.125vh)',
              textAlign: 'center',
              lineHeight: '90%',
              zIndex: 2
            }}
          >
            No Move Selected...
          </div>

          <img
            id = 'moveType'
            src = '/src/assets/Type_Icons/icon_null.png'
            style = {{
              position: 'absolute',
              top: '82.25%',
              left: '68%',
              width: '8%',
              height: 'auto',
              zIndex: 2,
              imageRendering: 'pixelated'
            }}
          >
          </img>

          <div
            id = 'movePp'
            style = {{
              position: 'absolute',
              top: '87.5%',
              left: '53%',
              width: '38%',
              height: '12%',
              fontSize: 'calc(1.778vw + 1vh)',
              textAlign: 'center',
              lineHeight: '90%',
              zIndex: 2
            }}
          >
            No Move Selected...
          </div>

          <img
            id = 'returnButton'
            src = '/src/assets/cancel_1.png'
            style = {{
              position: 'absolute',
              top: '67%',
              left: '96%',
              width: '4%',
              height: 'auto',
              zIndex: 2,
              imageRendering: 'pixelated'
            }}
          >
          </img>

          {move && <LoadingDisplay/>} {/*Apply loading screen until AI chooses its action*/}
          {move && <ActionReceiver action = {move} pokemon = {false}/>} {/*Send move to receiver that forwards choice to backend*/}
        </div>
      </div>
    </>
  )
}

export default MovesDisplay;