import './App.css'
import {useEffect} from "react";

function App() {

  useEffect(() => {
//     window["echo"](1,2,3).then(res => console.log(res))
    console.log(window)
  }, []);

    function add() {
        window["TestClass_addTwoNumbers"](1, 2)
        .then(res => console.log(res, typeof res))
        .catch(err => console.log(err))
    }

    function join() {
        window["TestClass_joinStrings"]("This", "is")
        .then(res => console.log(res, typeof res))
        .catch(err => console.log(err))
    }

  return (
    <>
      <p>Hello world!</p>
      <button onClick={add}>click</button>
      <button onClick={join}>click list</button>
    </>
  )
}

export default App
