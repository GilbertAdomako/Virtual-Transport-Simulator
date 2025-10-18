# Visual Transit Simulator: Project Iteration 1


## The Visual Transit Simulator Software

The Visual Transit Simulator (VTS) models and simulates transit activity around the University of Minnesota campus.
The simulator represents the movement of buses and trains across defined routes, simulating vehicle movement, passenger generation, and stop activity in real time.

The software is composed of two main components:

Simulator Module (Java, backend)

Visualization Module (JavaScript/HTML, frontend)

These components communicate via WebSockets, allowing real-time simulation updates in the browser.
The VTS software is divided into two main modules: the *visualization module* and the *simulator module*. The visualization module displays the state of the simulation in a browser, while the simulator module performs the simulation. The visualization module is a web client application that runs in a browser and it is written in Javascript and HTML. The visualization module code is inside the `<dir>/app/src/main/webapp/web_graphics` directory of this repo (where `<dir>` is the root directory of the repo). The simulator module is a web server application written in Java. The simulator module code is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project` directory. The simulator module is divided into two parts: *model classes* and the *webserver classes*. The model classes model real-world entities (e.g., the concept of a vehicle) and the code is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/model` directory. The webserver classes include the code that orchestrates the simulation and is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory. The visualization module and the simulator module communicate with each other using [websockets](https://www.baeldung.com/java-websockets).

The user of the VTS software interacts with the visualization module using the browser and can specific how long the simulation will run (i.e., how many time units) and how often new vehicles will be added to a route in the simulation. The users also specifies when to start the simulation. The image below depicts the graphical user interface (GUI) of the VTS software.

![GUI of the VTS Software](/images/vts_iteration_1.png)

### VTS Software Details

Core Functions of the VTS
1. Route and Line Management

A line consists of two routes: outbound and inbound.

Routes are defined in a configuration file with stops, geographic coordinates, and passenger generation probabilities.

Vehicles travel from the starting stop to the final stop of the outbound route, then return via the inbound route.

Each vehicle completes one round trip and then exits the simulation.

2. Vehicle Simulation

Supports buses and trains.

Vehicles:

Move along their route each time unit.

Pick up and drop off passengers at stops.

Are generated at configurable intervals.

Each vehicle maintains its own understanding of its route.

Multiple vehicles can serve the same line simultaneously.

3. Passenger Flow

Passengers appear at stops with a probability defined in the configuration file.

Passengers can appear after a vehicle has passed a stop, so multiple vehicles ensure service continuity.

Stops track passenger queues for all vehicles serving a line.

4. Configuration File Control

The file config.txt controls:

Line definitions (name, type, routes).

Stops (latitude, longitude, passenger probability).

Vehicle resources (number of buses/trains available).

This enables flexible scenario simulation without modifying code.

5. CO₂ Emission Tracking

VTS calculates CO₂ emissions per vehicle in real time:

Buses: 4 + 2 × (passenger count) CO₂ units

Trains: 6 + 3 × (passenger count) CO₂ units

This data is sent to the visualization module and displayed interactively.

6. Pause and Resume Simulation

The simulator can be paused and resumed via the GUI.

When resumed, the simulation continues from the last state without restarting.

#### Simulation Configuration
The simulation is based on the `<dir>/app/src/main/resources/config.txt` configuration file. The following excerpt of the configuration file defines a bus line.

```
LINE_START, BUS_LINE, Campus Connector

ROUTE_START, East Bound

STOP, Blegen Hall, 44.972392, -93.243774, .15
STOP, Coffman, 44.973580, -93.235071, .3
STOP, Oak Street at University Avenue, 44.975392, -93.226632, .025
STOP, Transitway at 23rd Avenue SE, 44.975837, -93.222174, .05
STOP, Transitway at Commonwealth Avenue, 44.980753, -93.180669, .05
STOP, State Fairgrounds Lot S-108, 44.983375, -93.178810, .01
STOP, Buford at Gortner Avenue, 44.984540, -93.181692, .01
STOP, St. Paul Student Center, 44.984630, -93.186352, 0

ROUTE_END

ROUTE_START, West Bound

STOP, St. Paul Student Center, 44.984630, -93.186352, .35
STOP, Buford at Gortner Avenue, 44.984482, -93.181657, .05
STOP, State Fairgrounds Lot S-108, 44.983703, -93.178846, .01
STOP, Transitway at Commonwealth Avenue, 44.980663, -93.180808, .01
STOP, Thompson Center & 23rd Avenue SE, 44.976397, -93.221801, .025
STOP, Ridder Arena, 44.978058, -93.229176, .05
STOP, Pleasant Street at Jones-Eddy Circle, 44.978366, -93.236038, .1
STOP, Bruininks Hall, 44.974549, -93.236927, .3
STOP, Blegen Hall, 44.972638, -93.243591, 0

ROUTE_END

LINE_END
```

The configuration line `LINE_START, BUS_LINE, Campus Connector` defines the beginning of the information belonging to a simulated line. The configuration line `ROUTE_START, East Bound` defines a the beginning of the information defining the outbound route. (The outbound route is always defined before the inbound route). The subsequent configuration lines are the stops in the route. Each stop has a name, a latitude, a longitude, and the probability to generate a passenger at the stop. For example, for `STOP, Blegen Hall, 44.972392, -93.243774, .15`, `Blegen Hall` is the name of the stop, `44.972392` is the latitude, `-93.243774` is the longitude, and `.15` (i.e., `0.15`) is the probability to generate a passenger at the stop. The last stop in a route has a probability to generate a passenger always equal to zero.

#### Running the VTS Software
To run the VTS software, you have to **first start the simulator module** and **then start the visualization module**. To start the simulator module, go to `<dir>` and run `./gradlew appRun` (or `./gradlew clean appRun`). To start the visualization module, open a browser and paste this link `http://localhost:7777/project/web_graphics/project.html` in its address bar. To stop the simulator module, press the enter/return key in the terminal where you started the module. To stop the visualization module, close the tab of browser where you started the module. In rare occasions, you might experience some issues in starting the simulator module because a previous instance of the module is still running. To kill old instances, run `ps aux | grep gretty | awk '{print $2}' | xargs -L 1 kill` (un Unix-like operating systems) and this command will terminate previous instances. (The command is killing the process of the web server container running the simulator module.) The command works on CSE lab machines.

#### Simulation Workflow
Because the VTS software is a web application, the software does not have a `main` method. When you load the visualization module in the browser, the visualization module opens a connection to the simulator module (using a websocket). The opening of the connection triggers the execution of the `WebServerSession.onOpen` method in the simulator module. When you click `Start` in the GUI of the visualization module, the module starts sending messages/commands to the simulator module. The messages/commands exchanged by the two modules are [JSON objects](https://www.w3schools.com/js/js_json_objects.asp). You can see the messages/commands created by the visualization module inside `<dir>/app/src/main/webapp/web_graphics/sketch.js`. The simulator module processes messages received by the visualization model inside the `WebServerSession.onMessage` method. The simulator module sends messages to the visualization module using the `WebServerSession.sendJson` method. Finally, once you start the simulation you can restart it only by reloading the visualization module in the browser (i.e., reloading the web page of the visualization module).