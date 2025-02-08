function LoadingDisplay() {
  return(
    <>
      <div
        style={{
          position: 'absolute',
          top: '0%',
          width: '100vw',
          height: 'calc(100vw * 9 / 16)', // Maintain 16:9 aspect ratio
          maxHeight: '100vh',
          maxWidth: 'calc(100vh * 16 / 9)', // Prevents overflow
        }}
      >
        <div
          id = 'loadingBox'
          style = {{
            position: 'absolute',
            left: '10.2%',
            top: '72.8%',
            width: '92.9%',
            height: '22.7%',
            fontSize: 64,
            textAlign: 'center',
            lineHeight: '300%',
            backgroundColor: 'grey',
            opacity: 0.9,
            transform: `scale(${window.innerWidth / 1920})`,
            transformOrigin: 'top left'
          }}
        >
          Loading...
        </div>
      </div>
    </>
  )
}

export default LoadingDisplay;