import axios from 'axios'

export const userService = {
  loginUser
}

const instance = axios.create({
  baseURL: 'http://localhost:10000/api/v1/users'
})

function loginUser(userCredentials) {
  return instance.post(
    "/login",
    JSON.stringify(userCredentials),
    {
      headers: {
        "content-type": "application/json",
      },
    }
  )
}