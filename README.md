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

## Features to be added
- To handle Get request
- Add functionality of databases
- Video Streaming capabilities
- Proper handling of request method (POST/GET etc)

## Upcoming feature
- Database functionality

## Current Bug
>There is a unknown bug due to which some files (mainly image & large files) are not sent to the browser completely hence not properly interpreted in the browser as expected.
>The bug is unknown and is patched temporarily.

><del>This bug is temporarly fixed by either any of the two methods :
>- <del>By adding delay of few hundred miliseconds in the thread at _**HttpConnectionWorkerThread**_ class after flushing the _**outputStream**_ (This fix works in all Browsers)
>- <del>By not closing the inputstream/outputstream/socket connection in the _**HttpConnectionWorkerThread**_ class (This fix works only in Chrome, doesnot work in any other browser)

>Update: The bug is caused due to the client/server closing the socket before completing the data transfer causing in broken pipe error at the socket.
>The bug is patched by making the thread sleep for few miliseconds(sleep time depends on the data chunk size) before letting the outputstream/socket to close.

### Note:
> If any solution is found for any of the currnet bugs mentioned, then open a issue and do a pull request with the found solution there.
