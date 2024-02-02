import axios from 'axios'

export const destinationService = {
  searchDestination
}

const instance = axios.create({
  baseURL: 'https://localhost:8443/api/v1/destination'
})

function searchDestination(requestBody) {
  return instance.post(
    "/search",
    JSON.stringify(requestBody),
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "content-type": "application/json",
      },
    }
  )
}