import {useEffect, useRef} from 'react'
import eventBus from './EventBus'

function SceneRenderer({battleState}) {
  const battleStateRef = useRef(null); // Holds battleState and is updated whenever battleState changes

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
      width: (9.18 * percentage) + '%'
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
      sprite.src = '/src/assets/Pokemon_Sprites/Back/' + String(pokemon).toUpperCase() + '.png';

      // Adjust health bar and health values to new Pokemon switching in
      document.getElementById('playerHP').innerHTML = health;
      document.getElementById('playerHealth').animate(hpAnim(percentage), animTiming);

      startX = '0%';
      endX = '17%';
    }
    else {
      sprite = document.getElementById('botPokemon');
      sprite.src = '/src/assets/Pokemon_Sprites/Front/' + String(pokemon).toUpperCase() + '.png';

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
      const message = data.detail;

      // Choose animation based on server message of what just happened
      if (String(message).includes(' fainted!')) {
        const pokemon = String(message).split(' ')[0]; // Returns the Pokemon name
        faintPokemon(pokemon);
      }
      else if (String(message).includes(' lost ') && String(message).includes(' HP!')) {
        const info = String(message).split(' '); // Returns [{Pokemon}, lost, {number}, HP!] indices 0 and 2
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

        const user = info[0]; // Returns Player or ChatGPT
        const health = info[4];

        sendOutPokemon(pokemon, user, health);
      }
      else if (String(message).includes(' went back to ')) {
        const pokemon = String(message).split(' ')[0];
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
          transform: `scale(${window.innerWidth / 16})`,
          transformOrigin: 'top left',
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

      <div
        id = 'playerHealth'
        style = {{
          position: 'absolute',
          top: '57.86%',
          left: '82.21%',
          width: '9.18%',
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
          width: '9.18%',
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

export default SceneRenderer