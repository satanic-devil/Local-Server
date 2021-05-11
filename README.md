# Local-Server
Local Server implemented in Java. Simple HTTP Server.

## Introduction
This is a simple HTTP Server that will serve files from the directories it is ran.

## How to compile
Run the following command
```bash
  javac LocalServer.java
```

## How to run
Run the following command
```bash
  java LocalServer
```
![Example Output of Running the Server](https://github.com/satanic-devil/output-files/blob/main/local-server-cmd.png?raw=true)

The Server runs on port *9000*

## How to Access the server
1. Open browser on any device connected to the server/computer
2. Get the address of the server/computer running the program
3. Type the address in the address bar followed by ***":9000"***

![Example Output of accessing the service in browser](https://github.com/satanic-devil/output-files/blob/main/local-server-browser.png?raw=true)

## Bugs/ Problems/ Vulnerabilities
1. The Server is single threaded hence won't serve multiple devices simultaneously
2. You are able to access the files from other folder which aren't subdirectories of the folder in which the program is ran and even from other drives by modifying the URL
