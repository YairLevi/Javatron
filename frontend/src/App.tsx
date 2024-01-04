import './App.css'
import {useEffect, useState} from "react";
import {addTwoNumbers, incrementAndPrint, joinStrings} from "../javatron/methods/TestClass"
import { addListener } from "../javatron/ipc/events";
import { invoke } from "../javatron/methods/Custom";

function App() {
  useEffect(() => {
    console.log(window)
    addListener("test_invoke", () => console.log("it was called!!"))
    addListener("test_invoke", () => console.log("it was called again"))
  }, [])

  return (
    <>
      <p>Hello world!</p>
      <button onClick={incrementAndPrint}>click list</button>
      <button onClick={() => window["echo"]()}>test_eval</button>
      <button onClick={invoke}>Click to test invoke</button>
      <p>aragamana is aogeoahmoemh</p>
    </>
  )
}

export default App
