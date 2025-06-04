# DemoQuarkus

Sample project to demonstrate basic Quarkus capabilities.

## How to run

Simply run `mvn quarkus:dev`.

Default listened port is `8080`.

## How to use

Current operations :

- `GET /api/demo/clients` : list clients
- `POST /api/demo/clients` : add a client
- `GET /api/demo/clients/{email}` : get a client
- `POST /api/demo/clients/{email}` : update a client
- `DELETE /api/demo/clients/{email}` : delete a client
