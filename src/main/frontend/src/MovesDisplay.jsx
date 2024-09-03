import {useEffect} from 'react'

function MovesDisplay({battleState, hover, displayOptions}) {
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
  })

  const setBorderAndText = (moveNum, name, type) => {
    const moveBorder = document.getElementById('move' + moveNum + 'Img');
    const moveDiv = document.getElementById('move' + moveNum + 'Text');

    moveBorder.src = 'src/assets/Move_Borders/move_' + String(type).toLowerCase() + '_1.png';
    moveDiv.textContent = name;
  }

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
      document.getElementById('move' + i).addEventListener('mouseover', function() {
        hover('move' + i + 'Img', true);
        updateSelectedMoveDetails(i);
      });
  
      document.getElementById('move' + i).addEventListener('mouseout', function() {
        hover('move' + i + 'Img', false);
      });
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

  return (
    <>
      <div
        id = 'move1'
      >
        <img
          id = 'move1Img'
          src = '/src/assets/Move_Borders/move_normal_1.png'
          style = {{ 
            position: 'absolute', 
            top: '73.5%', 
            left: '9.5%', 
            width: '21vw',
            height: 'auto',
            imageRendering: 'pixelated'
          }}
        />

        <div 
          id = 'move1Text'
          style = {{
            position: 'absolute',
            top: '73.5%', 
            left: '9.5%', 
            width: '21vw',
            height: '7vh',
            lineHeight: '9.5vh',
            verticalAlign: 'middle',
            fontSize: 32,
            textAlign: 'center'
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
            top: '73.5%', 
            left: '32%', 
            width: '21vw',
            height: 'auto',
            imageRendering: 'pixelated'
          }}
        />

        <div 
          id = 'move2Text'
          style = {{
            position: 'absolute',
            top: '73.5%',
            left: '32%',
            width: '21vw',
            height: '7vh',
            lineHeight: '9.5vh',
            verticalAlign: 'middle',
            fontSize: 32,
            textAlign: 'center'
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
            left: '9.5%', 
            width: '21vw',
            height: 'auto',
            imageRendering: 'pixelated'
          }}
        />

        <div 
          id = 'move3Text'
          style = {{
            position: 'absolute',
            top: '84.5%',
            left: '9.5%',
            width: '21vw',
            height: '7vh',
            lineHeight: '9.5vh',
            verticalAlign: 'middle',
            fontSize: 32,
            textAlign: 'center'
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
            left: '32%', 
            width: '21vw',
            height: 'auto',
            imageRendering: 'pixelated'
          }}
        />

        <div 
          id = 'move4Text'
          style = {{
            position: 'absolute',
            top: '84.5%',
            left: '32%',
            width: '21vw',
            height: '7vh',
            lineHeight: '9.5vh',
            verticalAlign: 'middle',
            fontSize: 32,
            textAlign: 'center'
          }}
        >
        </div>
      </div>

      <img
        src = '/src/assets/overlay_message.png'
        style = {{
          position: 'absolute',
          top: '72%',
          left: '53%',
          width: '38vw',
          height: '23.2vh',
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
          width: '38vw',
          height: '23.2vh',
          fontSize: 48,
          textAlign: 'center',
          lineHeight: '90%'
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
          width: '8vw',
          height: 'auto',
          imageRendering: 'pixelated'
        }}
      >
      </img>

      <div
        id = 'movePp'
        style = {{
          position: 'absolute',
          top: '88%',
          left: '53%',
          width: '38vw',
          height: '12vh',
          fontSize: 40,
          textAlign: 'center',
          lineHeight: '90%'
        }}
      >
        No Move Selected...
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
    </>
  )
}

export default MovesDisplay;