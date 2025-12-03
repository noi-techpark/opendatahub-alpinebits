<!--
SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>

SPDX-License-Identifier: CC0-1.0
-->

![Logo of the project](https://raw.githubusercontent.com/idm-suedtirol/odh-alpinebits/master/alpine_bits_rgb.png)

# AlpineBits server library

[AlpineBits](https://www.alpinebits.org/) is an open standard to exchange
touristic data.

This repository provides a library to implement AlpineBits server functionality.
Use it to build your own standalone AlpineBits server, or integrate parts
of it into your existing code base.

[![REUSE Compliance](https://github.com/noi-techpark/odh-alpinebits/actions/workflows/reuse.yml/badge.svg)](https://github.com/noi-techpark/odh-docs/wiki/REUSE#badges)
[![CI](https://github.com/noi-techpark/odh-alpinebits/actions/workflows/ci.yml/badge.svg)](https://github.com/noi-techpark/odh-alpinebits/actions/workflows/ci.yml)

## Table of Contents

- [Getting started](#getting-started)
- [Running the tests](#running-the-tests)
- [Deployment](#deployment)
- [Docker environment](#docker-environment)
- [Information](#information)

## Getting started

These instructions will get you a copy of the project up and running
on your local machine for development and testing purposes. Take a look
at the [Deployment](#deployment) section for notes on how to deploy
the project on a live system.

### Prerequisites

To build the project, the following prerequisites must be met:

- Java JDK 11 or higher (e.g. [OpenJDK](https://openjdk.java.net/))
- [Maven](https://maven.apache.org/) 3.6+

For a ready to use Docker environment with all prerequisites already installed and prepared, you can check out the [Docker environment](#docker-environment) section.

### Installing

Get a copy of the repository, e.g. by cloning it from the following location:

```bash
git clone https://github.com/noi-techpark/opendatahub-alpinebits.git
```

Change directory:

```bash
cd opendatahub-alpinebits/
```

Build the project:

```bash
mvn clean install
```

### Examples

The `examples` folder contains modules with pre-configured AlpineBits server
instances, that are easy to get up and running. The main point of the examples
is to demonstrate the usage of the library to create a functioning AlpineBits
server.

#### Example: Handshaking action (since 2020-10)

The `handshaking` example provides an AlpineBits server instance supporting
the so called Handshaking action.

Change to the `handshaking` directory (assuming you are in the projects root
folder):

```bash
cd examples/handshaking
```

Run the following maven command:

```bash
mvn clean install cargo:run
```

This command starts a local Tomcat 10 instance using
[Cargo](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html) and deploys
the example war file, generated during the installation (see [Installing](#installing)).

The deployment contains an AlpineBits server instance supporting the
Handshaking action and a simple website to facilitate the communication with
the server.

After Tomcat started, Open the website at
[http://localhost:8080/examples-handshaking/index.html](http://localhost:8080/examples-handshaking/index.html).

The website provides a set of links and an output area. If you click on any of
the links, the website will issue a request against the deployed AlpineBits
server instance and display the results.

#### Example: Housekeeping action (until 2018-10)

The `housekeeping` example provides an AlpineBits server instance supporting
the so called Housekeeping actions.

Change to the `housekeeping` directory (assuming you are in the projects root
folder):

```bash
cd examples/housekeeping
```

Run the following maven command:

```bash
mvn clean install cargo:run
```

This command starts a local Tomcat 10 instance using
[Cargo](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html) and deploys
the example war file, generated during the installation (see [Installing](#installing)).

The deployment contains an AlpineBits server instance supporting the
Housekeeping actions and a simple website to facilitate the communication with
the server.

After Tomcat started, Open the website at
[http://localhost:8080/examples-housekeeping/index.html](http://localhost:8080/examples-housekeeping/index.html).

The website provides a set of links and an output area. If you click on any of
the links, the website will issue a request against the deployed AlpineBits
server instance and display the results.

#### Example: Inventory actions

The `inventory` example provides an AlpineBits server instance supporting
the so called Inventory actions. A [H2](http://www.h2database.com)
instance is used for persistence (the database file is written with name
`alpinebits_inventory` to current user's HOME folder).

Change to the `inventory` directory (assuming you are in the projects root
folder):

```bash
cd examples/inventory
```

Run the following maven command:

```bash
mvn clean install cargo:run
```

This command starts a local Tomcat 10 instance using
[Cargo](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html) and deploys
the example war file, generated during the installation (see [Installing](#installing)).

The deployment contains an AlpineBits server instance supporting the
Inventory action. This example doesn't provide a UI, because its implementation
would be rather demanding. At the moment, that effort is not justified.

You can use other tools like `curl` to perform requests against the running server.

#### Example: FreeRooms actions

The `freerooms` example provides an AlpineBits server instance supporting
the so called FreeRooms actions. A [H2](http://www.h2database.com)
instance is used for persistence (the database file is written with name
`alpinebits_freerooms` to current user's HOME folder).

Change to the `freerooms` directory (assuming you are in the projects root
folder):

```bash
cd examples/freerooms
```

Run the following maven command:

```bash
mvn clean install cargo:run
```

This command starts a local Tomcat 10 instance using
[Cargo](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html) and deploys
the example war file, generated during the installation (see [Installing](#installing)).

The deployment contains an AlpineBits server instance supporting the
FreeRooms action. This example doesn't provide a UI, because its implementation
would be rather demanding. At the moment, that effort is not justified.

You can use other tools like `curl` to perform requests against the running server.

## Running the tests

The code in this repository provides unit and integration tests.

The unit tests can be run with the following command:

```bash
mvn clean verify
```

To run the unit and integration tests, use the following command, activating the
maven `it` profile:

```bash
mvn clean verify -P it
```

You can also integrate [sonarqube](https://www.sonarqube.org/) to check the code
quality. Run the following command:

```bash
mvn clean verify sonar:sonar -P it,report
```

## Generate source JARs

To generate the source JARs, use the `sources` maven profile, e.g. run the following command:

```
mvn clean install -P sources
```
 
## Deployment

The project provides a servlet as entry point to the AlpineBits server. If you want,
to use it, the following additional prerequisites must be met:

- Servlet Container 6.x (e.g. [Tomcat 10](https://tomcat.apache.org/download-10.cgi))

Using the provided examples as template (see [Examples](#examples)), you should
be able to configure your AlpineBits server and to deploy the result as a war
onto a servlet container. 

## Docker environment

For the project a Docker environment is already prepared and ready to use with all necessary prerequisites.

These Docker containers are the same as used by the continuous integration servers.

### Installation

Install [Docker](https://docs.docker.com/install/) (with Docker Compose) locally on your machine.

### Start and stop the containers

Before start working you have to start the Docker containers:

```
docker compose up --build --detach
```

After finished working you can stop the Docker containers:

```
docker compose stop
```

### Running commands inside the container

When the containers are running, you can execute any command inside the environment. Just replace the dots `...` in the following example with the command you wish to execute:

```bash
docker compose exec java /bin/bash -c "..."
```

Some examples are:

```bash
docker compose exec java /bin/bash -c "mvn clean install"

# or

docker compose exec java /bin/bash -c "mvn clean verify"

# or

docker compose exec java /bin/bash -c "mvn clean verify -P it"
```

To check the code quality, you can run the following command and then visit the results at [http://localhost:9000/](http://localhost:9000/):

```bash
docker compose exec java /bin/bash -c "mvn clean verify sonar:sonar -Dsonar.host.url=http://sonar:9000 -P it,report"
```

To server the example run the following command and then visit the website at [http://localhost:8080/examples-housekeeping/index.html](http://localhost:8080/examples-housekeeping/index.html):

```bash
docker compose exec java /bin/bash -c "cd examples/housekeeping && mvn cargo:run"
```

## Information

### Support

For support, please contact [Christian Gapp](https://github.com/gappc) or
[IDM Südtirol - Alto Adige](https://www.idm-suedtirol.com/en/home.html).

### Contributing

If you'd like to contribute, please fork the repository and use a feature
branch. Pull requests are warmly welcome.

### Versioning

This project uses [SemVer](https://semver.org/) for versioning. For the versions available,
see the [tags on this repository](https://github.com/idm-suedtirol/odh-alpinebits/tags). 

### License

The code in this project is licensed under the Mozilla Public License 2.0 license.
See the LICENSE file for more information.

### Authors

* **Christian Gapp** - *Initial work* - [gappc](https://github.com/gappc)

### Acknowledgements

- [IDM Südtirol - Alto Adige](https://www.idm-suedtirol.com/en/home.html)

### REUSE

This project is [REUSE](https://reuse.software) compliant, more information about the usage of REUSE in NOI Techpark repositories can be found [here](https://github.com/noi-techpark/odh-docs/wiki/Guidelines-for-developers-and-licenses#guidelines-for-contributors-and-new-developers).

Since the CI for this project checks for REUSE compliance you might find it useful to use a pre-commit hook checking for REUSE compliance locally. The [pre-commit-config](.pre-commit-config.yaml) file in the repository root is already configured to check for REUSE compliance with help of the [pre-commit](https://pre-commit.com) tool.

Install the tool by running:
```bash
pip install pre-commit
```
Then install the pre-commit hook via the config file by running:
```bash
pre-commit install
```
