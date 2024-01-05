import './App.css'
import {useEffect, useState} from "react";
import {Person} from "../javatron/types";
import {DoSomething} from "../javatron/methods/Person"

function App() {
  useEffect(() => {
    console.log(window)
    // addListener("test_invoke", () => console.log("it was called!!"))
    // addListener("test_invoke", () => console.log("it was called again"))

  }, [])

  return (
    <>
      <p>Hello world!</p>
      {/*<button onClick={incrementAndPrint}>click list</button>*/}
      {/*<button onClick={() => window["echo"]()}>test_eval</button>*/}
      {/*<button onClick={invoke}>Click to test invoke</button>*/}
      <p>aragamana is aogeoahmoemh</p>
    </>
  )
}

export default App
