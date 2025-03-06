# Waste Collection System Simulation

## Description
This project is a distributed application simulating a waste collection and processing system using TCP/IP sockets. It models interactions between various subsystems, including houses, an office, tankers, and a sewage plant. The system demonstrates the process of waste collection, transportation, and disposal in a structured manner.

## System Components

### 1. House (IHouse)
- Represents a single-family home not connected to a sewage system.
- Collects wastewater in an on-site septic tank.
- Sends a service request to the Office when the septic tank reaches a critical level.
- Provides an interface:
  - `int getPumpOut(int max)`: Allows a tanker to pump out waste, taking `max` as the available tanker capacity and returning the actual pumped-out volume.

### 2. Office (IOffice)
- Manages service requests and assigns them to available tankers.
- Interfaces:
  - `int register(string host, string port)`: Registers a tanker, returning its assigned tanker number.
  - `int order(string host, string port)`: Receives waste removal requests from houses and returns `1` for acceptance or `0` for rejection.
  - `void setReadyToServe(int number)`: Allows a tanker to signal its availability for new jobs.

### 3. Tanker (ITanker)
- Registers with the Office and executes waste removal jobs.
- Pumps out waste from houses and transports it to the Sewage Plant.
- Interfaces:
  - `void setJob(string host, string port)`: Receives an assignment from the Office to serve a house.

### 4. Sewage Plant (ISewagePlant)
- Processes waste delivered by tankers.
- Interfaces:
  - `void setPumpIn(int number, int volume)`: Accepts waste from a tanker, storing the volume delivered.
  - `int getStatus(int number)`: Retrieves the total waste delivered by a given tanker.
  - `void setPayoff(int number)`: Settles the transaction for waste disposal, resetting the recorded waste volume.

## Technologies Used
- **Programming Language**: Java
- **Networking**: TCP/IP sockets (ServerSocket and Socket classes)
- **Concurrency**: Multi-threading to simulate parallel subsystems

## How to Run
1. Compile and start each subsystem separately on one or multiple machines.
2. Ensure the Office is running before registering tankers or placing service requests.
3. Houses send service requests when needed, and tankers fulfill them based on Office assignments.
4. Tankers transport waste to the Sewage Plant for processing.

## Future Improvements
- Implement a graphical interface for better visualization.
- Enhance communication efficiency with message queuing.
- Add load-balancing mechanisms for tanker assignments.

## License
This project is open-source and available under the MIT License.

