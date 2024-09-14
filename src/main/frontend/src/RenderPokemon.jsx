import {useEffect, useRef} from 'react'
import eventBus from './EventBus'

function RenderPokemon({battleState}) {
  const battleStateRef = useRef(null);

  const getElementFromPokemon = (pokemon) => {
    let sprite = document.getElementById('playerPokemon');

    if (String(sprite.src).includes(String(pokemon).toUpperCase())) {
      sprite = 'playerPokemon';
    }
    else {
      sprite = 'botPokemon';
    }

    return sprite;
  }

  const faintPokemon = (pokemon) => {
    const sprite = getElementFromPokemon(pokemon);

    setTimeout(() => {
      document.getElementById(sprite).animate(faintTransition(), hpTiming);
      document.getElementById(sprite).visibility = 'hidden';
      document.getElementById(sprite).transform = 'translateY(-25%)';
      document.getElementById(sprite).opacity = 1; 
    }, 1000)
  }

  const dealDamage = (pokemon, amount) => {
    const sprite = getElementFromPokemon(pokemon);
    let healthBar = '';
    let hp = '';
    //let hpValue = '';

    if (sprite === 'playerPokemon') {
      healthBar = 'playerHealth';
      hp = document.getElementById('playerHP');
      //hpValue = [battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp];
    }
    else {
      healthBar = 'botHealth';
      hp = document.getElementById('botHP');
      //hpValue = [battleState.botPokemonCurrentHp, battleState.botPokemonMaxHp];
    }

    const hpValue = String(hp.textContent).split('/');
    const currentHp = hpValue[0];
    console.log(currentHp);
    const maxHp = hpValue[1];

    let newHp = currentHp - amount;
    console.log(newHp);
    let percentage = newHp / maxHp;

    if (percentage < 0) {
      percentage = 0;
      newHp = 0;
    }
    //const width = (9.18 * percentage) + '%';
    const bar = document.getElementById(healthBar);
    //const currentPercentage = currentHp / maxHp;

    setTimeout(() => {
      bar.animate(hpShifting(percentage), hpTiming);
      bar.backgroundColor = getHealthColor(currentHp, maxHp);
      hp.textContent = newHp + '/' + maxHp;
    }, 1500);
  }

  const useMove = (pokemon) => {
    const sprite = getElementFromPokemon(pokemon);

    const userLeft = document.getElementById(sprite).style.left;
    const userTop = document.getElementById(sprite).style.top;

    let opposingLeft = '';
    let opposingTop = '';

    if (sprite === 'playerPokemon') {
      opposingLeft = document.getElementById('botPokemon').style.left;
      opposingTop = document.getElementById('botPokemon').style.top;
    }
    else {
      opposingLeft = document.getElementById('playerPokemon').style.left;
      opposingTop = document.getElementById('playerPokemon').style.top;
    }

    setTimeout(() => {
      document.getElementById(sprite).animate(moveAnim([userLeft, userTop], [opposingLeft, opposingTop]), {duration: 750, easing: 'ease-in-out'}); 
    }, 1000)
  }

  const sendOutPokemon = (pokemon, user, health) => {
    let sprite = '';
    let startX = '';
    let endX = '';

    const hpParts = health.split('/');
    const percentage = hpParts[0] / hpParts[1];

    if (user === 'Player') {
      sprite = document.getElementById('playerPokemon');
      sprite.src = '/src/assets/Pokemon_Sprites/Back/' + String(pokemon).toUpperCase() + '.png';

      document.getElementById('playerHP').innerHTML = health;
      document.getElementById('playerHealth').animate(hpShifting(percentage), hpTiming);

      startX = '0%';
      endX = '17%';
    }
    else {
      sprite = document.getElementById('botPokemon');
      sprite.src = '/src/assets/Pokemon_Sprites/Front/' + String(pokemon).toUpperCase() + '.png';

      document.getElementById('botHP').innerHTML = health;
      document.getElementById('botHealth').animate(hpShifting(percentage), hpTiming);

      startX = '79%';
      endX = '62%';
    }

    sprite.opacity = 0;
    //sprite.visibility = 'visible';

    sprite.dataset.switching = 'false';
    sprite.animate(switchInAnim(startX, endX), {duration: 750, easing: 'ease-out', fill: 'forwards'});
  }

  const retrievePokemon = (pokemon) => {
    let sprite = getElementFromPokemon(pokemon);

    let startX = '';
    let endX = '';

    if (sprite === 'playerPokemon') {
      startX = '17%';
      endX = '0%';
    }
    else {
      startX = '62%';
      endX = '79%';
    }

    document.getElementById(sprite).dataset.switching = 'true';
    document.getElementById(sprite).animate(switchOutAnim(startX, endX), {duration: 750, easing: 'ease-out', fill: 'forwards'});
    //document.getElementById(sprite).visibility = 'hidden';
  }

  useEffect(() => {
    const updateBattleState = (data) => {
      //console.log('Data received: ', data);

      const message = data.detail;

      if (String(message).includes(' fainted!')) {
        const pokemon = String(message).split(' ')[0];
        faintPokemon(pokemon);
      }
      else if (String(message).includes(' lost ') && String(message).includes(' HP!')) {
        const info = String(message).split(' ');
        dealDamage(info[0], info[2]);
      }
      else if (String(message).includes(' used ')) {
        const pokemon = String(message).split(' ')[0];
        useMove(pokemon);
      }
      else if (String(message).includes(' sent out ')) {
        const info = String(message).split(' ');

        let pokemon = info[3];
        pokemon = pokemon.substring(0, pokemon.length - 1);

        const user = info[0];
        const health = info[4];

        sendOutPokemon(pokemon, user, health);
      }
      else if (String(message).includes(' went back to ')) {
        const pokemon = String(message).split(' ')[0];
        retrievePokemon(pokemon);
      }
    } 

    //console.log(battleState);
    eventBus.on('Battle Update', updateBattleState);

    return () => {
      eventBus.off('Battle Update', updateBattleState);
    }
  })

  useEffect(() => {
    battleStateRef.current = battleState;
  }, [battleState])

  const getHealthColor = (currentHp, maxHp) => {
    const percentage = currentHp / maxHp;

    if (percentage < 0.2) {
      return 'red';
    }
    else if (percentage < 0.5) {
      return 'yellow';
    }
    else {
      return 'green';
    }
  }

  const getPlayerHp = () => {
    console.log(battleStateRef.current);
    const playerHp = battleStateRef.current.playerPokemonCurrentHp;
    const playerMaxHp = battleStateRef.current.playerPokemonMaxHp;
    console.log(playerHp);
    document.getElementById('playerHP').innerHTML = playerHp + '/' + playerMaxHp;
    document.getElementById('playerHealth').style.width = (9.18 * (playerHp / playerMaxHp)) + '%';
  }
  // const checkFainted = (currentHp) => {
  //   document.getElementById('playerPokemon').top = '23%';
  //   document.getElementById('playerPokemon').opacity = 1;

  //   if (currentHp != 0) {
  //     return false;
  //   }

  //   if (document.querySelector('#playerPokemon') == null) {
  //     return false;
  //   }

  //   setTimeout(() => {
  //     document.getElementById('playerPokemon').animate(faintTransition(), hpTiming); 
  //   }, 1000)

  //   return true;
  // }
  const switchOutAnim = (startX, endX) => [
    {
      left: startX
    },
    {
      left: endX,
      opacity: 0
    }
  ]

  const switchInAnim = (startX, endX) => [
    {
      left: startX
    },
    {
      left: endX,
      opacity: 1
    }
  ]

  const moveAnim = (userPos, opposingPos) => [
    {
      left: userPos[0],
      top: userPos[1]
    },
    {
      left: opposingPos[0],
      top: opposingPos[1]
    },
    {
      left: userPos[0],
      top: userPos[1]
    }
  ]

  const hpShifting = (percentage) => [
    {
      transformOrigin: 'left',
    },
    {
      transformOrigin: 'left',
      width: (9.18 * percentage) + '%',
    }
  ]

  const hpTiming = {
    duration: 1500,
    easing: 'ease-out',
    fill: 'forwards'
  }

  const faintTransition = () => [
    {
      // transformOrigin: 'left',
      // width: '100%'
      transform: 'translateY(0)',
      opacity: '1'
    },
    {
      // transformOrigin: 'left',
      // width: (9.18 * 0.5) + '%',
      transform: 'translateY(25%)',
      opacity: '0'
    }
    // {
    //   top: '40%'
    // }
  ]

  const setHealthFillPercentage = (currentHp, maxHp) => {
    const percentage = currentHp / maxHp; 

    if (document.querySelector('#playerHealth')) {
      document.getElementById('playerHealth').animate(hpShifting(percentage), hpTiming);
    } 
    else {
      return '9.18%';
    }

    //checkFainted(currentHp);
    
    return (9.18 * percentage) + '%';
  }

  return(
    <>
      <img
        id = 'playerPokemon'
        data-switching = 'false'
        src = {'/src/assets/Pokemon_Sprites/Back/' + battleState.playerPokemonSpriteUrl}
        style = {{ 
          position: 'absolute', 
          top: '23%', 
          left: '17%', 
          width: '100%',
          height: 'auto',
          maxWidth: '480px',
          maxHeight: '480px',
          imageRendering: 'pixelated'
        }}
      />

      <div 
        style = {{
          position: 'absolute',
          top: '50%',
          left: '73%',
          fontSize: 48
        }}
      >
        {battleState.playerPokemonName}
      </div>

      <div
        id = 'playerHP'
        style = {{
          position: 'absolute',
          top: '59%',
          left: '82.8%',
          fontSize: 32
        }}
      >
        {battleState.playerPokemonMaxHp}/{battleState.playerPokemonMaxHp}
      </div>

      {/* <div>
        <div
          id = 'playerHealth'
          style = {{
            position: 'absolute',
            top: '57.86%',
            left: '82.21%',
            width: setHealthFillPercentage(battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp),
            height: '1.07%',
            backgroundColor: getHealthColor(battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp)
          }}
        >
        </div>
      </div> */}

      <div
        id = 'playerHealth'
        style = {{
          position: 'absolute',
          top: '57.86%',
          left: '82.21%',
          width: '9.18%',//setHealthFillPercentage(battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp),
          height: '1.07%',
          backgroundColor: getHealthColor(battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp)
        }}
      >
      </div>
      
      <div
        style = {{
          position: 'absolute',
          top: '59%',
          left: '73%',
          fontSize: 32
        }}
      >
        Lv. {battleState.playerPokemonLevel}
      </div>

      <img 
        id = 'botPokemon'
        data-switching = 'false'
        src = {'/src/assets/Pokemon_Sprites/Front/' + battleState.botPokemonSpriteUrl}
        style = {{
          position: 'absolute', 
          top: '0%', 
          left: '62%', 
          width: '100%',
          height: 'auto',
          maxWidth: '480px',
          maxHeight: '480px',
          imageRendering: 'pixelated'
        }}
      />

      <div 
        style = {{
          position: 'absolute',
          top: '8%',
          left: '6.5%',
          fontSize: 48
        }}
      >
        {battleState.botPokemonName}
      </div>

      <div
        id = 'botHP'
        style = {{
          position: 'absolute',
          top: '59%',
          left: '82.8%',
          fontSize: 32,
          visibility: 'hidden'
        }}
      >
        {battleState.botPokemonMaxHp}/{battleState.botPokemonMaxHp}
      </div>

      <div
        id = 'botHealth'
        style = {{
          position: 'absolute',
          top: '15.78%',
          left: '17.26%',
          width: '9.18%',//setHealthFillPercentage(battleState.botPokemonCurrentHp, battleState.botPokemonMaxHp),
          height: '1.07%',
          backgroundColor: getHealthColor(battleState.botPokemonCurrentHp, battleState.botPokemonMaxHp)
        }}
      >
      </div>

      <div 
        style = {{
          position: 'absolute',
          top: '14.3%',
          left: '6.5%',
          fontSize: 32
        }}
      >
        Lv. {battleState.botPokemonLevel}
      </div>
    </>
  )
}

export default RenderPokemon