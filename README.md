# JAVA-HTTP-Server
### A HTTP webserver built with pure JAVA implementing sockets and not using any web or http libraries.
This project is under development and needs further improvement

## Features added till now
- Can serve basic webpages
- Can handle javascript and css along with other files
- Can serve multiple webpages from different directories
- Can handle wrong webpage and resource requests
- Can handle icons & images and other media _(there is a bug here)_

## Features to be added
- To handle Post & Get request
- Add functionality of databases
- Use of correct response codes
- Video Streaming capabilities
- Proper handling of response and request header & its parameters

## Upcoming feature
- To receive data by post method and process them accordingly

## Current Bug
>There is a unknown bug due to which some files (mainly image & large files) are not sent to the browser completely hence not properly interpreted in the browser as expected.
>The bug is unknown and is patched temporarily

>This bug is temporarly fixed by either any of the two methods :
>- By adding delay of few hundred miliseconds in the thread at _**HttpConnectionWorkerThread**_ class after flushing the _**outputStream**_ (This fix works in all Browsers)
>- By not closing the inputstream/outputstream/socket connection in the _**HttpConnectionWorkerThread**_ class (This fix works only in Chrome, doesnot work in any other browser)
