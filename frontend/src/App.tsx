import './App.css'
import {useEffect, useState} from "react";
import {addTwoNumbers, incrementAndPrint, joinStrings} from "../javatron/methods/classes.TestClass"

function App() {
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
