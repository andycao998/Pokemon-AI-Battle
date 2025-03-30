import {useEffect, useRef} from 'react'
import eventBus from './EventBus'

function SceneRenderer({battleState}) {
  const battleStateRef = useRef(null); // Holds battleState and is updated whenever battleState changes

  const formatPokemonName = (pokemon) => {
    const formattedName = String(pokemon).replace(/[^0-9a-z]/gi, '').toUpperCase();
    console.log(formattedName);
    return formattedName
  }

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
      transform: 'none',
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

  const hpAnim = (percentage) => [
    {
      transformOrigin: 'left'
    },
    {
      transformOrigin: 'left',
      width: (11.1 * percentage) + '%'
    }
  ]

  const faintAnim = () => [
    {
      transform: 'translateY(0)',
      opacity: '1'
    },
    {
      transform: 'translateY(25%)',
      opacity: '0'
    }
  ]

  const animTiming = {
    duration: 1500,
    easing: 'ease-out',
    fill: 'forwards'
  }

  // Helper function to distinguish which side a Pokemon belongs to
  const getElementFromPokemon = (pokemon) => {
    let sprite = document.getElementById('playerPokemon');

    if (String(sprite.src).includes(formatPokemonName(pokemon))) {
      sprite = 'playerPokemon';
    }
    else {
      sprite = 'botPokemon';
    }

    return sprite;
  }

  const faintPokemon = (pokemon) => {
    const sprite = getElementFromPokemon(pokemon);

    document.getElementById(sprite).animate(faintAnim(), animTiming);

    // Set properties delayed after animation finishes
    setTimeout(() => {
      document.getElementById(sprite).visibility = 'hidden';
      document.getElementById(sprite).transform = 'translateY(25%)';
      document.getElementById(sprite).opacity = 1; 
    }, 2000)
  }

  const dealDamage = (pokemon, amount) => {
    const sprite = getElementFromPokemon(pokemon);
    let healthBar = '';
    let hp = '';

    if (sprite === 'playerPokemon') {
      healthBar = 'playerHealth';
      hp = document.getElementById('playerHP');
    }
    else {
      healthBar = 'botHealth';
      hp = document.getElementById('botHP');
    }

    const hpValue = String(hp.textContent).split('/');
    const currentHp = hpValue[0];
    const maxHp = hpValue[1];

    let newHp = currentHp - amount;
    let percentage = newHp / maxHp;

    if (percentage < 0) {
      percentage = 0;
      newHp = 0;
    }

    const bar = document.getElementById(healthBar);

    setTimeout(() => {
      bar.animate(hpAnim(percentage), animTiming);
      bar.backgroundColor = getHealthColor(currentHp, maxHp);
      hp.textContent = newHp + '/' + maxHp;
    }, 1500);
  }

  // WIP: All moves use the same animation of the user moving towards the opposing's position (create more later)
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

    const hpParts = String(health).split('/');
    const percentage = parseInt(hpParts[0]) / parseInt(hpParts[1]);

    // Get fixed screen positions of start and end of animation
    if (user === 'Player') {
      sprite = document.getElementById('playerPokemon');
      sprite.src = '/assets/Pokemon_Sprites/Back/' + formatPokemonName(pokemon) + '.png';

      // Adjust health bar and health values to new Pokemon switching in
      document.getElementById('playerHP').innerHTML = health;
      document.getElementById('playerHealth').animate(hpAnim(percentage), animTiming);

      startX = '0%';
      endX = '17%';
    }
    else {
      sprite = document.getElementById('botPokemon');
      sprite.src = '/assets/Pokemon_Sprites/Front/' + formatPokemonName(pokemon) + '.png';

      document.getElementById('botHP').innerHTML = health;
      document.getElementById('botHealth').animate(hpAnim(percentage), animTiming);

      startX = '79%';
      endX = '62%';
    }

    sprite.opacity = 0; // Hide sprite until a new Pokemon switches in

    sprite.dataset.switching = 'false'; // Used to determine if interface should transition to party menu or default options menu (false = options)
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

    document.getElementById(sprite).dataset.switching = 'true'; // Used to determine if interface should transition to party menu or default options menu (true = party)
    document.getElementById(sprite).animate(switchOutAnim(startX, endX), {duration: 750, easing: 'ease-out', fill: 'forwards'});
  }

  useEffect(() => {
    const updateBattleState = (data) => {
      const message = String(data.detail);

      // Choose animation based on server message of what just happened
      if (message.includes(' fainted!')) {
        const pokemon = message.slice(0, message.indexOf(' fainted!')); // Formatted as '{Pokemon} fainted!'

        faintPokemon(pokemon);
      }
      else if (message.includes(' lost ') && String(message).includes(' HP!')) {
        const index = message.indexOf(' lost ');
        const pokemon = message.slice(0, index); // Formatted as '{Pokemon} lost {number} HP!'
        const numHp = message.slice(index + 6, message.indexOf(' HP!')); // ' lost ' is 6 characters long

        dealDamage(pokemon, numHp);
      }
      else if (message.includes(' used ')) {
        const pokemon = message.slice(0, message.indexOf(' used ')); // Formatted as '{Pokemon} used {Move}!'; Move is currently unused because only one move anim exists

        useMove(pokemon);
      }
      else if (message.includes(' sent out ')) {
        const index = message.indexOf(' sent out ');
        const user = message.slice(0, index); // Formatted as '{Player/ChatGPT} sent out {Pokemon}! {Health}'
        const pokemon = message.slice(index + 10, message.indexOf('!')); // ' sent out ' is 10 characters long
        const health = message.slice(message.indexOf('! ') + 1);

        sendOutPokemon(pokemon, user, health);
      }
      else if (message.includes(' went back to ')) {
        const pokemon = message.slice(0, message.indexOf(' went back to '));

        retrievePokemon(pokemon);
      }
    } 

    eventBus.on('Battle Update', updateBattleState);

    return () => {
      eventBus.off('Battle Update', updateBattleState);
    }
  })

  // WIP: May not be necessary
  useEffect(() => {
    battleStateRef.current = battleState;
  }, [battleState])

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
          <img
            id = 'playerPokemon'
            data-switching = 'false'
            src = {'/assets/Pokemon_Sprites/Back/' + battleState.playerPokemonSpriteUrl}
            style = {{ 
              position: 'absolute', 
              top: '28%', 
              left: '22%', 
              width: '25%',
              height: 'auto',
              imageRendering: 'pixelated'
            }}
          />

          <div 
            style = {{
              position: 'absolute',
              top: '50%',
              left: '75%',
              fontSize: 'calc(2vw + 1.125vh)'
            }}
          >
            {battleState.playerPokemonName}
          </div>

          <div
            id = 'playerHP'
            style = {{
              position: 'absolute',
              top: '59.5%',
              left: '86.5%',
              fontSize: 'calc(1.33vw + 0.75vh)'
            }}
          >
            {battleState.playerPokemonMaxHp}/{battleState.playerPokemonMaxHp}
          </div>

          <div
            id = 'playerHealth'
            style = {{
              position: 'absolute',
              top: '58.2%',
              left: '85.7%',
              width: '11.1%',
              height: '1.3%',
              backgroundColor: getHealthColor(battleState.playerPokemonCurrentHp, battleState.playerPokemonMaxHp)
            }}
          >
          </div>
          
          <div
            style = {{
              position: 'absolute',
              top: '59.5%',
              left: '75.2%',
              fontSize: 'calc(1.33vw + 0.75vh)'
            }}
          >
            Lv. {battleState.playerPokemonLevel}
          </div>

          <img 
            id = 'botPokemon'
            data-switching = 'false'
            src = {'/assets/Pokemon_Sprites/Front/' + battleState.botPokemonSpriteUrl}
            style = {{
              position: 'absolute', 
              top: '5%', 
              left: '67%', 
              width: '25%',
              height: 'auto',
              imageRendering: 'pixelated'
            }}
          />

          <div 
            style = {{
              position: 'absolute',
              top: '8%',
              left: '1%',
              fontSize: 'calc(2vw + 1.125vh)'
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
              fontSize: 'calc(1.33vw + 0.75vh)',
              visibility: 'hidden'
            }}
          >
            {battleState.botPokemonMaxHp}/{battleState.botPokemonMaxHp}
          </div>

          <div
            id = 'botHealth'
            style = {{
              position: 'absolute',
              top: '16.1%',
              left: '13.6%',
              width: '11.1%',
              height: '1.3%',
              backgroundColor: getHealthColor(battleState.botPokemonCurrentHp, battleState.botPokemonMaxHp)
            }}
          >
          </div>

          <div 
            style = {{
              position: 'absolute',
              top: '14.3%',
              left: '1.1%',
              fontSize: 'calc(1.33vw + 0.75vh)'
            }}
          >
            Lv. {battleState.botPokemonLevel}
          </div>
        </div>
      </div>
    </>
  )
}

export default SceneRenderer