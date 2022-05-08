# JAVA-HTTP-Server
### A high performance multi-threaded HTTP webserver built with JAVA implementing sockets and not using any web or other server libraries.

[![powered by](https://img.shields.io/badge/Powered%20by-JAVA-blue?style=for-the-badge&logo=java&logoColor=red&color=e67300&labelColor=3244a8)](https://www.java.com/)
[![powered by](https://img.shields.io/badge/Managed%20using-MAVEN-blue?style=for-the-badge&logo=Apache&logoColor=E4682A&color=812878&labelColor=26235B)](https://maven.apache.org/)
[![powered by](https://img.shields.io/badge/Java%20HTTP%20Server-HTTP/1.1-blue?style=for-the-badge&logo=AIOHTTP&logoColor=black&color=blue&labelColor=ba0d0d)](https://datatracker.ietf.org/doc/html/rfc2616)

<p align="center" width="100%">
    <a href="#java-http-server"><img width="100%" src="https://i.ibb.co/t8sQLph/imgonline-com-ua-twotoone-R2-Oj-YEGX146-E2-PEs-removebg-preview.png"></a>
</p>

This project is under development and needs further improvement

#### Project is maintained using Maven
#### IDE used for project development is VS-Code

## Server Performance 
I tested the server in my local machine having following configuration, attached below is the result.

[Benchmark Result](https://github.com/AnolChakraborty/java-multi_threaded-http-server/blob/main/Benchmark%20Result.pdf)

##### &nbsp;&nbsp;&nbsp;Configuration of testing machine:
- CPU: AMD Ryzen 5 5600H Hexa core CPU @ 4.2GHz
- GPU: Radeon RX550M 4GB
- RAM: 12GB DDR4 @ 3200MHz
- Storage: 512GB M.2 SSD
- OS: Windows 11
- JAVA version: Open JDK v18 Windows 64bit
- Benchmarking tool: [Webstress](https://www.paessler.com/tools/webstress)


## Features added till now
- Multi-threaded request processing capability
- Can serve basic webpages
- Can handle javascript and css along with other files
- Can serve multiple webpages from different directories
- Can handle wrong webpage and resource requests
- Can handle icons & images and other media _(there is a bug here)_
- Can handle post request as well as AJAX calls
- Cookie based session handling.
- JSON data processing and parsing to and from the server to client
- Use of appropiate response codes
- Database integration with MySQL

## Features to be added
- To handle Get request
- Video Streaming capabilities
- Proper handling of request method (POST/GET etc)

## Upcoming feature
- Database functionality

## Current Bug
>Currently no bug exists.
