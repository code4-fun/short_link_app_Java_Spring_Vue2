import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'
import store from "@/store/index"

let stompClient = null

export function connect(email) {
  const socket = new SockJS('https://localhost:443/web-sock')
  stompClient = Stomp.over(socket)
  stompClient.connect({user: email}, () => {
    stompClient.subscribe('/user/queue/followed-link', (message) => {
      changeFollows(JSON.parse(message.body))
    })
  })
}

export function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect()
  }
}

function changeFollows(message) {
  store.dispatch('refreshFollows', message)
}