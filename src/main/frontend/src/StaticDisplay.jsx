import BattleBackground from './assets/swamp_bg.png'
import PlayerBase from './assets/playerbaseForest.png'
import BotBase from './assets/enemybaseForest.png'
import PlayerHealth from './assets/databox_normal.png'
import BotHealth from './assets/databox_normal_foe.png'
import PlayerOverlay from './assets/overlay_message.png'

function StaticDisplay() {
  return (
    <>
      <div
        style = {{
          display: 'flex', 
          justifyContent: 'center', 
          alignItems: 'center',
          height: '100vh',
          backgroundColor: 'black' 
        }}>

        <div
          style = {{
            backgroundImage: `url(${BattleBackground})`,
            backgroundSize: 'contain',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center',
            height: '100vh',
            width: '100vw',
            imageRendering: 'pixelated'
          }}>

            <img
              src = {PlayerBase}
              style = {{
                position: 'absolute',
                top: '57%', 
                left: '2%', 
                width: '50vw',
                imageRendering: 'pixelated'
              }}
            />

            <img 
              src = {BotBase} 
              style = {{ 
                position: 'absolute', 
                top: '5%', 
                left: '60%', 
                width: '30vw',
                imageRendering: 'pixelated'
              }}
            />

            <img 
              src = {PlayerHealth}
              style = {{ 
                position: 'absolute', 
                top: '50%', 
                right: '5.9%', 
                width: '25vw',
                imageRendering: 'pixelated'
              }}
            />

            <img
              src = {BotHealth}
              style = {{
                position: 'absolute',
                top: '8%',
                left: '5.9%',
                width: '25vw',
                imageRendering: 'pixelated'
              }}
            />

            <img
              src = {PlayerOverlay}
              style = {{
                position: 'absolute',
                top: '67.5%',
                left: '5.5%',
                width: '89vw',
                height: '32vh',
                imageRendering: 'pixelated'
              }}
            ></img>
        </div>
      </div>
    </>
  )
}

export default StaticDisplay;