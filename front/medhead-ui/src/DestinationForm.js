import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Stack from "react-bootstrap/Stack";
import { specialityService } from './SpecialityService'
import { destinationService } from './DestinationService'

function DestinationForm({ isLoggedIn }) {
  const [selectedLocation, setLocation] = useState("");
  const [selectedSpeciality, setSpeciality] = useState("");
  const [state, setState] = useState("");
  const [specialities, setSpecialities] = useState([]);
  // const [groupedSpecialties, setGroupedSpecialties] = useState([]);

  const [hideResponseCmp, setHideResponseCmp] = useState(true);
  const [destinationResponse, setDestinationResponse] = useState(null);

  const locations = [
    { id: "london1", viewValue: "London 1", lat: 51.5067053, lng: -0.1347252 },
    { id: "london2", viewValue: "London 2", lat: 51.5281872, lng: -0.0851167 },
    { id: "leicter", viewValue: "Leicter", lat: 52.633098, lng: -1.13828 },
    { id: "oxford", viewValue: "Oxford", lat: 51.7517696, lng: -1.2587185 },
  ];

  useEffect(() => {
    setState("loading");

    specialityService.getAllSpecialities()
      .then((specialities) => {
        setState("success");
        setSpecialities(specialities.data);

        // setGroupedSpecialties(
        //   specialities.data.reduce((groups, specialty) => {
        //     // If the group already exists in the accumulator, push the current item to it
        //     // Otherwise, create a new group with the current item
        //     if (groups[specialty.specialityGroup]) {
        //       groups[specialty.specialityGroup].push({
        //         code: specialty.code,
        //         specialityName: specialty.specialityName,
        //       });
        //     } else {
        //       groups[specialty.specialityGroup] = [
        //         {
        //           code: specialty.code,
        //           specialityName: specialty.specialityName,
        //         },
        //       ];
        //     }
        //     return groups;
        //   }, {})
        // );
      })
      .catch((error) => {
        setState("error");
        console.error("Error:", error);
      });
      //console.log("groupedSpecialties => " + JSON.stringify(groupedSpecialties));

      //console.log("Token => ", localStorage.token);

  }, [isLoggedIn]);

  const handleLocationChange = (e) => {
    setLocation(e.target.value);
  };

  const handleSpecialityChange = (e) => {
    setSpeciality(e.target.value);
  };

  const handleSubmit = (event) => {

    event.preventDefault();

    const location = locations.find(
      (location) => location.id === selectedLocation
    );

    const requestBody = {
      location: {
        lat: location.lat,
        lng: location.lng,
      },
      speciality: specialities.find(
        (speciality) => speciality.code === selectedSpeciality
      ).specialityName,
    };

    destinationService.searchDestination(JSON.stringify(requestBody))
      .then((response) => {
        //console.log(response.data);
        setDestinationResponse(response.data);
        setHideResponseCmp(false);
      })
      .catch((error) => {
        console.log("OUTPUT : destination-service : ", JSON.stringify(error));
      });
  };

  if (!isLoggedIn) {
    return (
      <div className="App">
        <header className="App-header">
          <div className="container">
            <p className="text-dark text-center">Log in please !</p>
          </div>
        </header>
      </div>
    );
  } else {
    return (
      <div className="App">
        <header className="App-header">
          {/* Request */}
          <div className="container">
            {/* Locations */}
            <Form.Select
              aria-label="Location"
              className="my-2"
              value={selectedLocation}
              onChange={handleLocationChange}
            >
              <option>-- Select location --</option>
              {locations.map((location) => (
                <option value={location.id}>{location.viewValue}</option>
              ))}
            </Form.Select>

            {/* Specialities */}
            <Form.Select
              aria-label="Speciality"
              className="my-2"
              value={selectedSpeciality}
              onChange={handleSpecialityChange}
            >
              <option>-- Select speciality --</option>
              {specialities.map((speciality) => (
                <option value={speciality.code}>
                  {speciality.specialityName}
                </option>
              ))}
              {/*Object.entries(groupedSpecialties).map(([key, value]) => (
                <option disabled>{key}</option>
              ))*/}
            </Form.Select>

            <Stack gap={2} className="my-2 col-md-12 mx-auto">
              <Button
                type="submit"
                variant="primary"
                onClick={handleSubmit}
                disabled={
                  state !== "success" ||
                  selectedLocation === "" ||
                  selectedSpeciality === ""
                }
              >
                Search destianation
              </Button>
            </Stack>
          </div>

          {/* Response */}
          <div
            className="container"
            id="destinationResponse"
            hidden={hideResponseCmp}
          >
            {destinationResponse !== null ? (
              <div className="cmp">
                <h2>{destinationResponse.hospitalName}</h2>
                <h3>{destinationResponse.hospitalAddress.numberAndStreet}</h3>
                <h3>{destinationResponse.hospitalAddress.locality}</h3>
                <h3>{destinationResponse.hospitalAddress.postCode}</h3>
                <h3>{destinationResponse.hospitalAddress.country}</h3>
              </div>
            ) : null}
          </div>
        </header>
      </div>
    );
  }
}

export default DestinationForm;
