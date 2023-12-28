import {useEffect, useState} from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { func } from "../javatron/binds";

class Note {
  title: string
  content: string

  constructor(title: string, content: string) {
    this.title = title;
    this.content = content;
  }

  save() {
    // save to database
  }
}

function App() {
  const [count, setCount] = useState(0)

    async function run() {
      const res = await func(new Note("Ttt", "Hello world"))
      console.log(res as Note)
    }
    useEffect(() => {
        run()
    })

  return (
    <>
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
