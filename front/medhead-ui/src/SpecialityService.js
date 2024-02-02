import axios from 'axios'

export const specialityService = {
  getAllSpecialities
}

const instance = axios.create({
  baseURL: 'https://localhost:9443/api/v1/specialities'
})

function getAllSpecialities() {
  return instance.get("/get/all")
}