import {useEffect} from 'react'

function MessageDisplay({message, displayOptions}) {
  useEffect(() => {
    updateMessage();
  }, [message])

  const updateMessage = () => {
    if (message === 'Finished Turn') {
      displayOptions();
      return;
    }

    //console.log(message);
    document.getElementById('battleMessages').innerHTML = message;
    //console.log(document.getElementById('battleMessages').innerHTML);

    // setTimeout(() => {
    //   displayOptions();
    // }, 2000);
  }

  return(
    <>
      <div
        id = 'battleMessages'
        style = {{
          position: 'absolute',
          left: '8.6%',
          top: '72.8%',
          width: '81vw',
          height: '22.7vh',
          marginLeft: '0.9vw',
          marginRight: '0.9vw',
          fontSize: 48
        }}
      >
        hey
      </div>
    </>
  )
}

export default MessageDisplay;