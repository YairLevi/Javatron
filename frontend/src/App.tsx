import './App.css'
import {useEffect, useState} from "react";
import {addTwoNumbers, incrementAndPrint, joinStrings} from "../javatron/methods/TestClass"

function App() {

  useEffect(() => {
//     window["echo"](1,2,3).then(res => console.log(res))
    console.log(window)
  }, []);

  const [state, setState] = useState(0)

  function add() {
    addTwoNumbers(1, 2)
      .then(res => console.log(res))
      .catch(err => console.log(err))
  }

  function F(): Promise<void> {
    return
  }

  async function join() {
    incrementAndPrint()
  }

  return (
    <>
      <p>Hello world!</p>
      <button onClick={add}>click</button>
      <button onClick={incrementAndPrint}>click list</button>
      <button onClick={() => window["test_eval"]()}>test_eval</button>
      <p>{state}</p>
    </>
  )
}

export default App
