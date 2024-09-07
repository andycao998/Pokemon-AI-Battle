import PartyDisplay from "./PartyDisplay";
import ActionReceiver from "./ActionReceiver";
import {useEffect, useState} from "react";

function SwitchDisplay({hover}) {
  const [switchTo, setSwitchTo] = useState(null);

  useEffect(() => {
    //setClickEvent();

    document.getElementById('returnButton').style.visibility = 'hidden';
  });

  const setClickEvent = () => {
    for (let i = 1; i <= 6; i++) {
      document.getElementById('party' + i).addEventListener('mousedown', function() {
        const switchName = eval('partyState.pokemon' + i + 'Name');
        setSwitchTo(switchName);
      });
    }
  }

  return(
    <>
      <PartyDisplay hover = {hover} displayOptions = {null} switchable = {true}/>

      {switchTo && <ActionReceiver action = {switchTo}/>}
    </>
  )
}

export default SwitchDisplay;