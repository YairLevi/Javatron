import './App.css'
import {useEffect} from "react";

function App() {

  useEffect(() => {
    window["echo"](1,2,3).then(res => console.log(res))
  }, []);

  return (
    <>
      <p>Hello world!</p>
    </>
  )
}

export default App
