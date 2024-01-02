/*
window {
  javatron {
    event {
      callbacks: [
        func1(),
        func2(),
      ],
      handler: () => {
        window.javatron[event].callbacks.forEach(c => c())
      }
    }
  }
}
 */

export function addListener(event: string, callback: Function) {
  if (!window[event]) {
    window[event] = [callback]
  } else {
    window[event].push(callback)
  }
}

export function removeListener(event: string, callback: Function) {
  if (!window[event]) {
    return
  }
  window[event] = window[event].filter(ev => ev != callback);
}