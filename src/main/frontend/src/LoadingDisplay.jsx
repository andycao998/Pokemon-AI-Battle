function LoadingDisplay() {
  return(
    <>
      <div
        id = 'loadingBox'
        style = {{
          position: 'absolute',
          left: '8.6%',
          top: '72.8%',
          width: '82.8vw',
          height: '22.7vh',
          fontSize: 64,
          textAlign: 'center',
          lineHeight: '300%',
          backgroundColor: 'grey',
          opacity: 0.9
        }}
      >
        Loading...
      </div>
    </>
  )
}

export default LoadingDisplay;