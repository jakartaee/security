///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.
///////////////////////////////////////////////////////////////////////

[[GCLIZ]]


[[b-packaging-the-test-applications-in-servlet-compliant-war-files-with-vi-specific-information]]
B Packaging the Test Applications in Servlet-Compliant WAR Files With VI-Specific Information
---------------------------------------------------------------------------------------------

The {TechnologyShortName} {TechnologyVersion} specification specifies
how {TechnologyShortName} applications are to be published in a Java SE
environment, JAX-WS endpoint, or Servlet–compliant Web container.

{TechnologyShortName} TCK test application classes that are to be
published in a Java SE environment are located under
`$TS_HOME/classes`.

The {TechnologyShortName} TCK comes with prebuilt test WAR files for
deployment on Jakarta EE {JakartaEEVersion} CI , which provides a Servlet–compliant Web
container. The WAR files are {TechnologyRI}-specific, with {TechnologyRI}'s servlet
class and {TechnologyRI}'s servlet defined in the `web.xml` deployment
descriptor. To run the TCK tests against the VI in a Servlet–compliant
Web container, the tests need to be repackaged to include the
VI-specific servlet, and the VI-specific servlet must be defined in the
deployment descriptor.

The {TechnologyShortName} TCK makes it easier for the vendor by
including template WAR files that contain all of the necessary files
except for the VI-specific servlet adaptor class. The
{TechnologyShortName} TCK provides a tool to help with the repackaging
task.


