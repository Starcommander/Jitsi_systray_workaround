# Jitsi_systray_workaround

This is a workaround to solve the problem of jdk, that systray takes no actions.  
**Jitsi** project: https://github.com/jitsi/jitsi/

osdependent.jar
* Just replaced the systray with a JFrame, with CloseOperation DO_NOTHING_ON_CLOSE.  
* Sources of modified files are included in **osdependent.jar**  
* Works on Jitsi-Version 2.9.5533

osdependent_v2.jar
* Uses JAppIndicator for Linux.
* See: https://github.com/Starcommander/JAppIndicator

Howto:  
Replace (make backup first) the file:  
* /usr/share/jitsi/sc-bundles/osdependent.jar

