# DirectChat 1.0

DirectChat is a simple command-line chat application written in Java, designed for communication between multiple clients through a central server. The application uses Maven for project management and Picocli for command-line interface (CLI) handling.

## Resolve dependencies
``./mvnw dependency:resolve``

## Build the project
``./mvnw package``

## Usage
### Server

To start the server, use the following command:

``java -jar target/dai-bleuer-lopez-practical-work-2-1.0-SNAPSHOT.jar server``

or

``java -jar target/dai-bleuer-lopez-practical-work-2-1.0-SNAPSHOT.jar server -p <port>``

Replace <port> with the desired port number (default is 1234).

### Client

To connect a client to the server, use the following command:

``java -jar target/dai-bleuer-lopez-practical-work-2-1.0-SNAPSHOT.jar client``

or

``java -jar target/dai-bleuer-lopez-practical-work-2-1.0-SNAPSHOT.jar client -s <server-address> -p <server-port>``

Replace <server-address> with the server's IP address, and <server-port> with the port number the server is running on (default is 1234).

## Commands
### Server Commands

``-p, --port: Specifies the port number for the server to listen on.``

### Client Commands

``-s, --server: Specifies the server's IP address.``

``-p, --port: Specifies the server's port number.``

## Example Scenario

1. Server Startup:

    ``java -jar target/DirectChat.jar server -p 1234``

    This starts the server on port 1234.


2. Client Connection:

    ``java -jar target/DirectChat.jar client -s 127.0.0.1 -p 1234``

    This connects a client to the server running on 127.0.0.1:1234.


3. Chatting:

    - Upon connecting, the client will be prompted to enter their name.
    - The client can then send messages to the server and receive messages from other connected clients.
    - Special commands include:
        - -l: List connected users.
        - -dm <username> <message>: Send a direct message to a specific user.
        - -q: Close the connection and exit the chat.
