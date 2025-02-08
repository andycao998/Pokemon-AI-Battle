import BattleBackground from './assets/swamp_bg.png';
import PlayerBase from './assets/playerbaseForest.png';
import BotBase from './assets/enemybaseForest.png';
import PlayerHealth from './assets/databox_normal.png';
import BotHealth from './assets/databox_normal_foe.png';
import PlayerOverlay from './assets/overlay_message.png';

function StaticDisplay() {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        width: '100vw',
        backgroundColor: 'black', // Black fills extra space around the centered image
        overflow: 'hidden',
      }}
    >
      {/* Background Container (Keeps 16:9 Aspect Ratio) */}
      <div
        style={{
          position: 'relative',
          width: '100vw',
          height: 'calc(100vw * 9 / 16)', // Maintain 16:9 aspect ratio
          maxHeight: '100vh',
          maxWidth: 'calc(100vh * 16 / 9)', // Prevents overflow
          backgroundImage: `url(${BattleBackground})`,
          backgroundSize: 'contain', // Ensures full image is shown, no cropping
          backgroundRepeat: 'no-repeat',
          backgroundPosition: 'center',
          imageRendering: 'pixelated',
        }}
      >
        {/* UI Elements Positioned RELATIVE to Background */}
        <img
          src={PlayerBase}
          style={{
            position: 'absolute',
            top: '58.4%',
            left: '4%',
            width: '50%',
            imageRendering: 'pixelated',
          }}
        />

        <img
          src={BotBase}
          style={{
            position: 'absolute',
            top: '7%',
            left: '64%',
            width: '30%',
            imageRendering: 'pixelated',
          }}
        />

        <img
          src={PlayerHealth}
          style={{
            position: 'absolute',
            top: '50%',
            right: '0%',
            width: '30%',
            imageRendering: 'pixelated',
          }}
        />

        <img
          src={BotHealth}
          style={{
            position: 'absolute',
            top: '8%',
            width: '30%',
            imageRendering: 'pixelated',
          }}
        />

        <img
          src={PlayerOverlay}
          style={{
            position: 'absolute',
            top: '67.5%',
            width: '100%',
            height: '32%',
            imageRendering: 'pixelated',
          }}
        />
      </div>
    </div>
  );
}

export default StaticDisplay;