import axios from "axios";

export const destinationService = {
  searchDestination,
};

const instance = axios.create({
  baseURL: "/api/v1/destination",
});

function searchDestination(requestBody) {
  return instance.post("/search", requestBody, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      "content-type": "application/json",
    },
  });
}
