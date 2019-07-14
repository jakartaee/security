# Jakarta Security

This repository contains the code for Jakarta Security.

[Online JavaDoc](https://javadoc.io/doc/javax.security.enterprise/javax.security.enterprise-api/)

Building
--------

Jakarta Security can be built by executing the following from the project root:

``mvn clean package``

The API jar can then be found in /api/target.

Making Changes
--------------

To make changes, fork this repository, make your changes, and submit a pull request.

About Jakarta Security
-------------

Jakarta Security defines a standard for creating secure Jakarta EE applications in modern application paradigms.
It defines an overarching (end-user targeted) Security API for Jakarta EE Applications.

Jakarta Security builds on the lower level Security SPIs defined by Jakarta Authentication and Jakarta Authorization,
which are both not end-end targeted.