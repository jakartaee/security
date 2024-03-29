#
# Copyright (c) 2009, 2021 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
#

#############################################################################
#                                                                   	    #
#   JavaTest Environment file for SecurityAPI Compatibility Test Suite      #
#                                                                           #
#   Environment specific properties in this file will likely	 	        #
#   have to be modified prior to running the securityapi TCK.               #
#   Instructions for modifying these properties are contained in this       #
#   file.	                                                                #
#############################################################################

########################################################################
## Javatest batch mode work directory and report directory, and policy for
## handling existing work and report directories.  These properties affects
## runclient and report targets, but not gui target.
## To disable generating test report, unset report.dir, or set it to "none"
## either here or from command line, as in the following command:
##               ant runclient -Dreport.dir="none"
##
# @work.dir     The directory used to store Javatest test results and test
#               information.
# @report.dir   The directory used to store Javatest summary reports of
#               test results.
# @if.existing.work.report.dirs specifies how existing work.dir and
# report.dir will be handled, and it must be one of the following values:
# overwrite     overwrites all content in work.dir and report.dir
# backup        moves all content in work.dir and report.dir to
#               work.dir_time_day_bak and report.dir_time_day_bak,
#               respectively
# append        reuses and preserves the existing work.dir and report.dir
# auto          lets the build files decide which mode to use
#               (overwrite, backup or append).  the value is determined
#               like this:
#                  if.existing.work.report.dirs == auto
#                    if in CTS workspace
#                      if.existing.work.report.dirs = overwrite
#                    else we are in a distribution bundle
#                      if.existing.work.report.dirs = append
#                    end if
#                  else
#                    if.existing.work.report.dirs = value in this file
#                  end if
########################################################################
work.dir=/files/JTWork
report.dir=/files/JTReport
if.existing.work.report.dirs=auto

 
########################################################################
# @javatest.timeout.factor This property specifies the scale factor used by
# Javatest to adjust the time JavaTest will wait for a given test to
# complete before returning failure.  For instance if the default test timeout
# is 5 minutes, this value will be multiplied by 5 minutes to determine
# the total timeout delay.  Note: this value only works with Javatest's
# batch mode (runclient).  When using the Javatest GUI users must change
# this timeout factor in the GUI. Configure -> Edit Configuration -> View
# -> choose Standard Values -> select tab Execution ->  set Time Factor.
########################################################################
javatest.timeout.factor=1.0

########################################################################
## Settings for Vendor securityapi Implementation
#  @web.home The location of the vendor's securityapi implementation.
########################################################################
web.home=

###############################################################
# The following property are added for setting up securityapi tests.
# All values are default to running with securityapi RI.
#
# All above environmental property are
# used for method permission testing; as well as securityapi related API testing.
#
#
## (SJSAS) asadmin. You must change these values as needed
## only if you are testing against SJSAS app server
## (javaee.home is pointing to SJSAS).
#
# @s1as.admin.user     -- The SJSAS asadmin user id
# @s1as.admin.passwd   -- The SJSAS asadmin user password
# @s1as.admin.host     -- The SJSAS host
# @s1as.admin.port     -- The SJSAS port
# @s1as.admin          -- The SJSAS admin command
# @s1as.server         -- The SJSAS server instance being used
# @s1as.domain.dir     -- Points to where your domains are installed.
# @s1as.domain.name    -- The SJSAS domain being used
# @s1as.domain         -- The SJSAS domain path being used
# @s1as.asenv.loc      -- location of asenv.conf or asenv.bat
#########################################################################
javaee.home=${web.home}
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${webServerHost}
s1as.admin.port=4848
s1as.admin=${javaee.home}/bin/asadmin
s1as.domain.dir=${web.home}/domains
s1as.domain.name=domain1
s1as.domain=${web.home}/domains/domain1
s1as.asenv.loc=${javaee.home}/config

###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern

###############################################################
# @alt.dtd.dir DTD location for securityapi specification and RI xml files.
#              Used for xml validation when building tests.
# @alt.schema.dir schema location for securityapi specification and RI xml files.
#                 Used for xml validation when building tests.
###############################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas


##########################################################################
## Classpath properties required by securityapi TCK:
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the securityapi TCK tests
# @securityapi.classes      --  Security API classes, default value using securityapi RI
##########################################################################

local.classes=${ts.home}/classes${pathsep}${securityapi.classes}${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/javatest.jar:${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/securityapitck.jar${pathsep}${db.lib}${pathsep}${ts.harness.classpath}

ts.classpath=${local.classes}${pathsep}${ts.harness.classpath}

ts.harness.classpath=${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/securityapitck.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-io-2.4.jar${pathsep}${ts.home}/lib/commons-lang3-3.3.2.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar${pathsep}${ts.home}/lib/commons-collections-3.2.1.jar${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/cssparser-0.9.25.jar${pathsep}${ts.home}/lib/htmlunit-2.15.jar${pathsep}${ts.home}/lib/htmlunit-core-js-2.15.jar${pathsep}${ts.home}/lib/httpcore-4.4.9.jar${pathsep}${ts.home}/lib/httpclient-4.5.5.jar${pathsep}${ts.home}/lib/httpmime-4.5.5.jar${pathsep}${ts.home}/lib/nekohtml-1.9.21.jar${pathsep}${ts.home}/lib/xercesImpl-2.11.0.jar${pathsep}${ts.home}/lib/xalan-2.7.2.jar${pathsep}${ts.home}/lib/serializer-2.7.2.jar${pathsep}${ts.home}/lib/relaxngDatatype.jar${pathsep}${ts.home}/lib/sac-1.3.jar${pathsep}${ts.home}/lib/saxpath.jar${pathsep}${ts.home}/lib/jaxen-1.1.6.jar${pathsep}${ts.home}/lib/jaxen-core-1.0.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ts.home}/lib/xml-apis-1.4.01.jar${pathsep}${ts.home}/lib/jakarta.activation-api.jar${pathsep}${ts.home}/lib/jaxb-api.jar${pathsep}${ts.home}/lib/jaxb-core.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar

securityapi.classes=${web.home}/modules/jakarta.servlet-api.jar${pathsep}${web.home}/modules/jakarta.security.enterprise-api.jar${pathsep}${web.home}/modules/jakarta.security.auth.message-api.jar${pathsep}${web.home}/modules/jakarta.annotation-api.jar${pathsep}${web.home}/modules/jakarta.inject-api.jar${pathsep}${web.home}/modules/jakarta.enterprise.cdi-api.jar${pathsep}${web.home}/modules/jakarta.faces.jar${pathsep}${web.home}/modules/jakarta.interceptor-api.jar${pathsep}${web.home}/modules/jakarta.ejb-api.jar${pathsep}${web.home}/modules/jakarta.authentication-api.jar${pathsep}${ts.home}/lib/unboundid-ldapsdk.jar


########################################################################
## Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test 
#                        clients which are not run inside an
#                        application client container.  For example,
#                        any URL clients or standalone java clients
#                        would be executed with this command.  Some
#                        test directories which make use of this command
#                        are securityapi and jsp.
########################################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
                    CLASSPATH=${JAVA_HOME}/lib/rt.jar${pathsep}\
                    ${LOCAL_CLASSES} \
                    HOME="${user.home}" \
                    windir=${windir} \
                    SYSTEMROOT=${SYSTEMROOT} \
                    ${JAVA_HOME}/bin/java \
                    -Dbytecheck=true \
                    -Djava.protocol.handler.pkgs=javax.net.ssl \
                    -Djavax.net.ssl.keyStore=${ts.home}/bin/certificates/clientcert.jks \
                    -Djavax.net.ssl.keyStorePassword=changeit \
                    -Djavax.net.ssl.trustStore=${web.home}/domains/domain1/config/cacerts.jks \
                    ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_unix                                              
########################################################################
env.ts_unix.menu=true

########################################################################
# Environment ts_win32						       
########################################################################
env.ts_win32.menu=true

##########################################################################
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
# @sigTestClasspath: This property specifies the location of securityapi
#                    implementation classes used for the signature test
##########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${securityapi.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

########################################################################
## These properties are used by the harness.  "harness.log.port"
## specifies the port that server components use to send logging
## output back to JavaTest.  If the default port # is not available
## on the machine running JavaTest, then you can set it here.
##
#
# @harness.temp.directory directory location used by the
#                         harness to store temporary files
#
# @harness.log.port  the port the harness listens on for log mesages
#                    from remote clients
#
# @harness.log.traceflag  used to turn on/off verbose debugging output
#                         for the tests.
#
# @harness.executeMode  used to run the harness in the following modes
#                       of execution:
#    0 - default (deploy, run, undeploy)
#    1 - deploy only
#    2 - run only
#    3 - undeploy only
#    4 - deploy and run only
#
# @harness.socket.retry.count - denotes the number of time we should
#          attempt to create a server socket when intilizing a test
#          client.  The socket is used for logging purposes.
#
# @harness.log.delayseconds  Number of seconds to delay to allow
#                            reporting from remote clients to finish.
#
########################################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.traceflag=true
harness.executeMode=2
harness.socket.retry.count=10
harness.log.delayseconds=1 
harness.log.mode=silent

##########################################################################
# Added for convenience to use CTS utilities for deploy in securityapi RI
##########################################################################
impl.vi=glassfish
impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy
 
###############################################################
# These properties must be set to tell the Javatest harness the
# class names of your porting class implementations. By default
# the property settings below point to Sun RI specific classes.  
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.HttpsURLConnection.class.1 - VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface 
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

##############################################################
# @certLoginUserAlias  User alias for certificate based login.
# This property is used in mutual authentication to pickup the
# certificate based on the user alias.
##############################################################
certLoginUserAlias=cts

#########################################################################
## These properties must specify the host and port of the web server,
## in which the securitys are deployed.
#
# @webServerHost  hostname for the Vendor's Java EE Web Server
# @webServerPort  port number of the Vendor's Java EE Web Server
# @securityClientThreads  The securityClientThreads property configures
#                        the number of threads used by the client for
#                        the SingleThreadModel security test. If the
#                        container implementation supports pooling of
#                        SingleThreadModel servlets, set the value of
#                        securityClientThreads to twice the value of
#                        the default security instance pool size.  If
#                        the container implementation only maintains
#                        a single instance of a SingleTheadModel
#                        security, leave the default value of 2.
#####################################################################
webServerHost=localhost
webServerPort=8080
securedWebServicePort=8181
securityClientThreads=2

#########################################################################
## The following properties must be set before running any securityapi
## related tests.  The properties user, password, authuser, authpassword,
## and nobodyuser must be set.
##
## The value for user, password, authuser, and authpassword need to be set
## exactly as they are set in the container/server.
#
# @user           User defined to exercise rolemapping feature
# @password       Associated password for the user
# @authuser       User defined to exercise rolemapping feature
# @authpassword   Associated password for the authuser
#########################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe

###############################################################
# @securedWebServicePort must be set to run secbasicssl test.
#                        Set this property with your application
#                        server's secured webservice port.
###############################################################
securedWebServicePort=

###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS WILL NOT HAVE TO SET LIVE BELOW
###################################################################
###################################################################
###################################################################

##
## The directory seperator for the platform.  User should not change
## this property.
##
dirsep=/

##build level
##2: compile and build component archives (e.g., jar's, war's)
##default is set to 3
build.level=2

##########################################################################
# Needed for building/packaging
##########################################################################
deliverable.class=com.sun.ts.lib.deliverable.securityapi.SecurityAPIDeliverable

######################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries
## is set to true)
######################################################################
tslib.name=securityapitck

ts_home=${ts.home}

org.omg.CORBA.ORBClass=foo

##########################################################################
# @security_waittime: Time in seconds to wait after HttpSession expires 
#                    to access the session
# @security_async_wait: Time in seconds to wait between sending asynchronous messages 
# @logical.hostname.security
#              This used to identify the the name of a logical host that
#              processes security requests. security requests may be directed to
#              a logical host using various physical or virtual host names or
#              addresses, and a message processing runtime may be composed of
#              multiple logical hosts (This is required to properly identify
#              the security profiles  AppContextId hostname.)
#              If a logical.hostname.security does not exist, you can set this
#              to the default hostname (eg webServerHost).
##########################################################################
security_waittime=10
security_async_wait=4
logical.hostname.security=server

##############################################################
# @jdbc.db - The name of the currently configured DB.
#       Defines which database will be used for certification.
#       This property will be used to determine the corresponding sql
#   to initialize and can be one of the following:
#
#       - derby
#       - mysql
#       - sybase
#       - db2
#       - mssqlserver
#       - oracle
#       - postgresql
#
# The sql for the tables and stored procedures are contained in:
#
#  $TS_HOME/[jdbc.db]/sql/[jdbc.db].ddl.persistence.sql
#  $TS_HOME/[jdbc.db]/sql/[jdbc.db].ddl.persistence.sprocs.sql
#
# If using a database other than above, you need to create
# your own DDL files but can use these files for reference of what
# tables are required.
#
#
################################################################
jdbc.db=derby
jdbc.poolName=${derby.poolName}
jdbc.maxpoolsize=64
jdbc.steadypoolsize=32
jdbc.jndi.name=jdbc/securityAPIDB

###############################################################
# @jdbc.lib.class.path - This property is used by the
#                        db.lib property to point to
#                        where the JDBC drivers live.
###############################################################
jdbc.lib.class.path=${web.home}/../javadb/lib

#######################################################
# Database specific properties
#
#  db.name - Name of the database (optional)
#  db.server - The server on which the database is running (optional)
#  db.port - The port on which the database is accepting connections (optional)
#  db.driver - JDBC driver implementation class
#  db.lib - The package for JDBC driver class
#  db.url - JDBC URL specification to allow access to the target backend
#  db.user - A valid user for the target backend
#  db.password - The password for the specified user
#
# As one example of some possible property settings; assuming we are
# running against a Glassfish install, one might use the following property settings:
#
#    db.name=securityAPIDB
#    db.server=localhost
#    db.port=1527
#    db.url=jdbc:derby://${db.server}:${db.port}/${db.name};create=true
#    db.driver=org.apache.derby.jdbc.ClientDriver
#    db.lib=${jdbc.lib.class.path}/derbyclient.jar
#    db.user=cts1
#    db.password=cts1
#
########################################################
db.name=${derby.dbName}
db.server=${derby.server}
db.port=${derby.port}
db.url=${derby.url}
db.driver=${derby.driver}
db.lib=${derby.classes}
db.user=${derby.user}
db.password=${derby.passwd}


##############################################################
## The following section is for CTS Database configuration
## For each database that you will test, configure the following:
##   Here is an example using derby:  Users will need to
##   replace derby with the appropriate DB name.
# @derby.dbName       -- Database Name
# @derby.server       -- Database Server
# @derby.port         -- Database Server port
# @derby.dml.file     -- DML file used for CTS test cases
# @derby.user         -- User Id configured
# @derby.passwd       -- User password configured
# @derby.url          -- URL to the cts database
# @derby.driver       -- DriverManager driver
# @derby.classes      -- CLASSPATH to JDBC driver classes
# @derby.poolName     -- Name of pool configured in the Java EE
#                          RI (do not change!)
# @derby.dataSource   -- DataSource driver
# @derby.properties   -- Any additional JDBC driver required
#                          properties
# @derby.startup.delay-- delay to wait for DB to start

###############################################################
#
## Info for Cloudscape 10/Derby
#
derby.dbName=derbyDB
derby.server=localhost
derby.port=1527
derby.user=cts1
derby.passwd=cts1
derby.url=jdbc:derby://${derby.server}:${derby.port}/${derby.dbName};create=true
derby.driver=org.apache.derby.jdbc.ClientDriver
derby.home=${javaee.home}/../javadb
derby.system.home=${derby.home}/databases
derby.classpath=${ts.home}/lib/dbprocedures.jar${pathsep}${derby.home}/lib/derbynet.jar
derby.classes=${derby.home}/lib/derbyclient.jar${pathsep}${derby.home}/lib/derbyshared.jar${pathsep}${derby.home}/lib/derbytools.jar
derby.poolName=cts-derby-pool
derby.dataSource=org.apache.derby.jdbc.ClientDataSource
derby.properties=DatabaseName\=\"${derby.dbName}\":user\=${derby.user}:password\=${derby.passwd}:serverName\=${derby.server}:portNumber=${derby.port}
derby.startup.delay=5

##########################################################################
### Properties used by ldap installation
# @ldap.server                        -- The type of ldap server.  unboundid is supported by now
# @ldap.install.server                -- Install ldap server by CTS script or not
# @ldap.ldif.file                     -- The ldif file containing data seeded in ldap server
#
# @unboundid.installation.package     -- The package for installing Unboundid ldap server
#
############################################################################
ldap.server=unboundid
ldap.install.server=true
ldap.ldif.file=${ts.home}/bin/ldap.ldif

unboundid.installation.package=${ts.home}/lib/unboundid-ldapsdk.jar

##########################################################################
# Default client used for tests
##########################################################################
test.client=Client.java

