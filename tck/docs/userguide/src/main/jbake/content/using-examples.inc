///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

1.  Start the tests using the following command:
+
--
[source,oac_no_warn]
----
mvn clean verify
----
--

[[GCMCU]]

===== Example 5-1 {TechnologyShortName} TCK Signature Tests

To run the {TechnologyShortName} TCK signature tests, enter the
following commands:

[source,subs="attributes"]
----
cd {sigTestDirectoryExample}
mvn clean verify
----

[[GCMBV]]


===== Example 5-2 Start JavaTests in jakarta-security-tck/security-tck

To run the Javatests:

[source,subs="attributes"]
----
cd {javatTestDirectoryExample}
mvn clean verify
----

[[GCMCA]]


===== Example 5-3 Subset of Test Directories

To run a subset of test directories, enter the following commands:

[source,subs="attributes"]
----
cd {subsetTestDirectoryExample}
mvn clean verify
----
