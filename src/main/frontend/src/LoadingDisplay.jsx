function LoadingDisplay() {
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
            id = 'loadingBox'
            style = {{
              position: 'absolute',
              left: '3.5%',
              top: '72.8%',
              width: '93%',
              height: '22.8%',
              fontSize: 'calc(2vw + 1.125vh)',
              textAlign: 'center',
              lineHeight: '300%',
              backgroundColor: 'grey',
              opacity: 0.9,
              zIndex: 2
            }}
          >
            Loading...
          </div>
        </div>
      </div>
      
    </>
  )
}

export default LoadingDisplay;