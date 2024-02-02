import "./App.css";
import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { userService } from './UserService'

function LoginForm({ isLoggedIn, onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("unknown");

  const handleSubmit = (event) => {
    event.preventDefault();

    const userCredentials = {
      login: username,
      password: password,
    };

    userService.loginUser(userCredentials)
      .then((response) => {
        localStorage.setItem("token", response.headers.getAuthorization()); // Store the token
        setFirstName(response.data.firstName);
      })
      .catch((error) => {
        console.log("OUTPUT : user-service : ", JSON.stringify(error));
      });

    onLogin();
  };

  if (firstName !== "unknown") {
    return (
      <p className="m-3">
        Welcome <span className="fs-5 fw-bolder fst-italic">{firstName}</span>
      </p>
    );
  }
  return (
    <Form onSubmit={handleSubmit}>
      <Row className="m-2">
        <Col sm={3}>
          <Form.Control
            column
            sm="4"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            className="m-2"
            required
          />
        </Col>
        <Col sm={3}>
          <Form.Control
            column
            sm="4"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            className="m-2"
            required
          />
        </Col>
        <Col sm={2}>
          <Button type="submit" variant="primary" className="m-2">
            Login
          </Button>
        </Col>
      </Row>
    </Form>
  );
}

export default LoginForm;
