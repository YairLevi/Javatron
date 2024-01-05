import './App.css'
import {useEffect} from "react";
import {addListener} from "../javatron/events";
import {addTwoNumbers, incrementAndPrint} from "../javatron/methods/Person";

function App() {
  useEffect(() => {
    console.log(window) // for debugging

    addListener("event", () => {
      console.log('the event was triggered!')
    })
  }, [])

  async function bindCallback() {
    const result = await addTwoNumbers(3, 2)
    console.log('result of bind callback: ', result)
  }

  async function triggerEventOnBackend() {
    console.log('calling increment() on the java now... it will trigger an event on the front end.')
    incrementAndPrint()
  }

  return (
    <div style={{flexDirection: "column", display: "flex", gap: "1rem"}}>
      <h2>This is a Javatron test</h2>
      <button onClick={bindCallback}>Execute a bind callback</button>
      <button onClick={triggerEventOnBackend}>Trigger event</button>
    </div>
  )
}

export default App
