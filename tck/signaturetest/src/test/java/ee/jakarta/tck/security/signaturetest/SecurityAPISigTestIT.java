/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.security.signaturetest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


/**
 * The SecurityAPISigTest class provides signature tests for the Java EE TCK.
 * This class extends SigTestEE which contains the signature test code. This
 * class is responsible for providing implementations of the abstract method
 * defined in SigTestEE, namely the getPackages method.
 */
public class SecurityAPISigTestIT extends SigTestEE {

  public SecurityAPISigTestIT(){
    setup();
  }


  public static final String EJB_VEHICLE = "ejb";

  public static final String SERVLET_VEHICLE = "servlet";

  public static final String JSP_VEHICLE = "jsp";

  public static final String APP_CLIENT_VEHICLE = "appclient";

  public static final String NO_VEHICLE = "standalone";

  /*
   * Defines the packages that are included when running signature tests for any
   * container (the default packages). This includes the appclient, ejb, jsp,
   * and servlet containers.
   */
  private static final String[] DEFAULT_PKGS = { "jakarta.security.enterprise",
      "jakarta.security.enterprise.authentication.mechanism.http",
      "jakarta.security.enterprise.credential",
      "jakarta.security.enterprise.identitystore"};

  /*
   * Defines additional packages that are included when running signature tests
   * for the ejb, jsp and servlet containers.
   */
  private static final String[] EJB_SERVLET_JSP_PKGS = {};

  /*
   * Defines additional packages that are included when running signature tests
   * for the jsp and servlet containers.
   */
  private static final String[] SERVLET_JSP_PKGS = {};

  private static final String[] NO_CONTAINER_PKGS = {
      "jakarta.security.enterprise",
      "jakarta.security.enterprise.authentication.mechanism.http",
      "jakarta.security.enterprise.credential",
      "jakarta.security.enterprise.identitystore"};

  /**
   * Adds the default packages and the command line flags to the specified list
   * for each package defined in the list of default packages to check during
   * signature tests. Note: The specified list is modified as a result of this
   * method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addDefaultPkgs(List<String> sigArgsList) {
    for (int i = 0; i < DEFAULT_PKGS.length; i++) {
      sigArgsList.add(DEFAULT_PKGS[i]);
    }
  }

  /**
   * Adds the ejb, servlet, and jsp packages and the command line flags to the
   * specified list for each package defined in the list of ejb, servlet, and
   * jsp packages to check during signature tests. Note: The specified list is
   * modified as a result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addEjbServletJspPkgs(List<String> sigArgsList) {
    for (int i = 0; i < EJB_SERVLET_JSP_PKGS.length; i++) {
      sigArgsList.add(EJB_SERVLET_JSP_PKGS[i]);
    }
  }

  /**
   * Adds the servlet, and jsp packages and the command line flags to the
   * specified list for each package defined in the list of servlet, and jsp
   * packages to check during signature tests. Note: The specified list is
   * modified as a result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addServletJspPkgs(List<String> sigArgsList) {
    for (int i = 0; i < SERVLET_JSP_PKGS.length; i++) {
      sigArgsList.add(SERVLET_JSP_PKGS[i]);
    }
  }

  /**
   * Adds the pkgs for tests to be run in NO Container (ie standalone) packages
   * to check during signature tests. Note: The specified list is modified as a
   * result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addNoContainerPkgs(List<String> sigArgsList) {
    for (int i = 0; i < NO_CONTAINER_PKGS.length; i++) {
      sigArgsList.add(NO_CONTAINER_PKGS[i]);
    }
  }

  /**
   * ** Abstract Method Implementations **** Returns a list of strings where
   * each string represents a package name. Each package name will have it's
   * signature tested by the signature test framework.
   *
   * @param vehicleName
   *          The name of the SecurityAPI container where the signature tests
   *          should be conducted.
   *
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */

  protected String[] getPackages() {
    return new String[] { "jakarta.security.enterprise",
    "jakarta.security.enterprise.authentication.mechanism.http",
    "jakarta.security.enterprise.authentication.mechanism.http.openid",
    "jakarta.security.enterprise.credential",
    "jakarta.security.enterprise.identitystore",
    "jakarta.security.enterprise.identitystore.openid"};
  }

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @param vehicleName
   *          The name of the SecurityAPI container where the signature tests
   *          should be conducted.
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */
  protected String[] getPackages(String vehicleName) {
    List<String> packages = new LinkedList<String>();

    if (vehicleName.equals(NO_VEHICLE)) {
      addNoContainerPkgs(packages);
    } else {
      addDefaultPkgs(packages); // add default vehicle packages
      if (vehicleName.equals(EJB_VEHICLE) || vehicleName.equals(SERVLET_VEHICLE)
          || vehicleName.equals(JSP_VEHICLE)) {
        addEjbServletJspPkgs(packages);
      }
      if (vehicleName.equals(SERVLET_VEHICLE)
          || vehicleName.equals(JSP_VEHICLE)) {
        addServletJspPkgs(packages);
      }
    }
    return packages.toArray(new String[packages.size()]);
  }

  public File writeStreamToTempFile(InputStream inputStream, String tempFilePrefix, String tempFileSuffix) throws IOException {
    FileOutputStream outputStream = null;

    try {
        File file = File.createTempFile(tempFilePrefix, tempFileSuffix);
        outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            outputStream.write(buffer, 0, bytesRead);
        }
        return file;
    }

    finally {
        if (outputStream != null) {
            outputStream.close();
        }
    }
  }

  public File writeStreamToSigFile(InputStream inputStream, String packageVersion) throws IOException {
    FileOutputStream outputStream = null;
    String tmpdir = System.getProperty("java.io.tmpdir");
    try {
        File sigfile = new File(tmpdir+File.separator+"jakarta.security.enterprise.sig_"+packageVersion);
        if(sigfile.exists()){
          sigfile.delete();
          TestUtil.logMsg("Existing signature file deleted to create new one");
        }
        if(!sigfile.createNewFile()){
          TestUtil.logErr("signature file is not created");
        }
        outputStream = new FileOutputStream(sigfile);
        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            outputStream.write(buffer, 0, bytesRead);
        }
        return sigfile;
    }

    finally {
        if (outputStream != null) {
            outputStream.close();
        }
    }
  }

  @Test
  public void signatureTest() throws Fault {
    TestUtil.logMsg("$$$ SigTestEE.signatureTest() called");
    SigTestResult results = null;
    String mapFile = null;
    String packageFile = null;
    String repositoryDir = null;
    Properties mapFileAsProps = null;
    try {

    InputStream inStreamMapfile = SecurityAPISigTestIT.class.getClassLoader().getResourceAsStream("ee/jakarta/tck/security/signaturetest/sig-test.map");
    File mFile = writeStreamToTempFile(inStreamMapfile, "sig-test", ".map");
    mapFile = mFile.getCanonicalPath();
    TestUtil.logMsg("mapFile location is :"+mapFile);

    InputStream inStreamPackageFile = SecurityAPISigTestIT.class.getClassLoader().getResourceAsStream("ee/jakarta/tck/security/signaturetest/sig-test-pkg-list.txt");
    File pFile = writeStreamToTempFile(inStreamPackageFile, "sig-test-pkg-list", ".txt");
    packageFile = pFile.getCanonicalPath();
    TestUtil.logMsg("packageFile location is :"+packageFile);
  
    mapFileAsProps = getSigTestDriver().loadMapFile(mapFile);
    String packageVersion = mapFileAsProps.getProperty("jakarta.security.enterprise");
    TestUtil.logMsg("Package version from mapfile :"+packageVersion);

    InputStream inStreamSigFile = SecurityAPISigTestIT.class.getClassLoader().getResourceAsStream("ee/jakarta/tck/security/signaturetest/jakarta.security.enterprise.sig_"+packageVersion);
    File sigFile = writeStreamToSigFile(inStreamSigFile, packageVersion);
    TestUtil.logMsg("signature File location is :"+sigFile.getCanonicalPath());
    repositoryDir = System.getProperty("java.io.tmpdir");


    } catch(IOException ex){
        TestUtil.logMsg("Exception while creating temp files :"+ex);
    }

    String[] packages = getPackages();
    String[] classes = getClasses();
    String testClasspath = System.getProperty("signature.sigTestClasspath");
    String optionalPkgToIgnore = testInfo.getOptionalTechPackagesToIgnore();

    // unlisted optional packages are technology packages for those optional
    // technologies (e.g. jsr-88) that might not have been specified by the
    // user.
    // We want to ensure there are no full or partial implementations of an
    // optional technology which were not declared
    ArrayList<String> unlistedTechnologyPkgs = getUnlistedOptionalPackages();

    // If testing with Java 9+, extract the JDK's modules so they can be used
    // on the testcase's classpath.
    Properties sysProps = System.getProperties();
    String version = (String) sysProps.get("java.version");
    if (!version.startsWith("1.")) {
      String jimageDir = testInfo.getJImageDir();
      File f = new File(jimageDir);
      f.mkdirs();

      String javaHome = (String) sysProps.get("java.home");
      TestUtil.logMsg("Executing JImage");

      try {
        ProcessBuilder pb = new ProcessBuilder(javaHome + "/bin/jimage", "extract", "--dir=" + jimageDir, javaHome + "/lib/modules");
        TestUtil.logMsg(javaHome + "/bin/jimage extract --dir=" + jimageDir + " " + javaHome + "/lib/modules");
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        while ((line = out.readLine()) != null) {
          TestUtil.logMsg(line);
        }

        int rc = proc.waitFor();
        TestUtil.logMsg("JImage RC = " + rc);
        out.close();
      } catch (Exception e) {
        TestUtil.logMsg("Exception while executing JImage!  Some tests may fail.");
        e.printStackTrace();
      }
    }

    try {
      results = getSigTestDriver().executeSigTest(packageFile, mapFile,
          repositoryDir, packages, classes, testClasspath,
          unlistedTechnologyPkgs, optionalPkgToIgnore);
      
      TestUtil.logMsg(results.toString());
      if (!results.passed()) {
        TestUtil.logErr("results.passed() returned false");
        throw new Exception();
      }

      TestUtil.logMsg("$$$ SigTestEE.signatureTest() returning");
    } catch (Exception e) {
      if (results != null && !results.passed()) {
        throw new Fault("SigTestEE.signatureTest() failed!, diffs found");
      } else {
        TestUtil.logErr("Unexpected exception " + e.getMessage());
        throw new Fault("signatureTest failed with an unexpected exception", e);
      }
    }
  }
  

  /*
   * @class.setup_props: sigTestClasspath, Location of SecurityAPI jar files;
   * ts_home, The base path of this TCK;
   */

  /*
   * @testName: signatureTest
   * 
   * @assertion: A SecurityAPI platform must implement the required classes and
   * and APIs specified in the SecurityAPI Platform Specification.
   * 
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   *
   */

  /*
   * Call the parent class's cleanup method.
   */

} // end class CTSSigTest
