# TCK

This sub-repo contains working applications that demonstrate and test various aspects of Jakarta Security

* **app-mem** - Uses the embedded in-memory identity store. The application sets the data to be used by means of an annotation
  * Test URL: http://localhost:8080/app-mem/servlet?name=reza&password=secret1
* **app-db**  - Uses the database identity store. The applications defines an embedded datasource and binds this to the identity store definition via an annotation. The data to be used is inserted in the datasoource by the application during startup.
  * Test URL: http://localhost:8080/app-db/servlet?name=reza&password=secret1
* **app-ldap** - Uses the LDAP identity store. The application instantiates an embedded LDAP server and binds its URL to the identity store definition via an annotation. The data to be used is inserted in the LDAP server by the application during startup.
  * Test URL: http://localhost:8080/app-ldap/servlet?name=reza&password=secret1
* **app-custom** - Uses an identity store that's fully provided by the application. Just for the example, this store does the caller name/credential check internally.
  * Test URL: http://localhost:8080/app-custom/servlet?name=reza&password=secret1
* **app-custom-session** - As app-custom, but uses a Jakarta Security provided interceptor to automatically establish an authentication session when authenticated. This means that the identity store is only consulted once per session.
  * Check initially not authenticated: http://localhost:8080/app-custom-session/servlet
  * authenticate: http://localhost:8080/app-custom-session/servlet?name=reza&password=secret1
  * Check authentication remembered: http://localhost:8080/app-custom-session/servlet
  * logout: http://localhost:8080/app-custom-session/servlet?logout
* **app-custom-rememberme** - As app-custom-session, but uses a Jakarta Security provided interceptor to conditionally remember the caller by writing a cookie and storing the details in an application provided special purpose identity store
  * Check initially not authenticated: http://localhost:8080/app-custom-rememberme/servlet
  * authenticate: http://localhost:8080/app-custom-rememberme/servlet?name=reza&password=secret1
  * Check authentication NOT remembered: http://localhost:8080/app-custom-rememberme/servlet
  * authenticate with remember me: http://localhost:8080/app-custom-rememberme/servlet?name=reza&password=secret1&rememberme=true
  * Check authentication remembered: http://localhost:8080/app-custom-rememberme/servlet
  * logout: http://localhost:8080/app-custom-session/servlet?logout
* **app-openid** - As app-mem but uses the Jakarta Security provided OpenID Connect authentication mechanism.
  * Deploys two applications to the server being tested: the test app (openid-client), and a mock OpenID Provider (openid-server)  
  * Check initially not authenticated
  * Requests /Secured servlet
  * Redirect to the mock OpenID Provider takes place, and a redirect back is issued to /Callback
  * Authentication Mechanism contacts OpenID Provider directly to verify token
  * Uses groups/roles coming from the OpenID Provider
* **app-openid2** - As app-openid but uses an actual certified OpenID provider deployed to a separately started Tomcat
  * Starts Tomcat on port 8081 and deploys the Mitre OpenID Provider
  * Uses groups/roles coming from a local extra identity store
* **app-openid3** - As app-openid2 but sets the "redirectToOriginalResource" attribute
  * At the end of the flow, the end-user is redirected to the original /protectedServlet and the original request is restored.
* **app-mem-basic** - As app-mem but uses the Jakarta Security provided BASIC authentication mechanism
  * Test URL: http://localhost:8080/app-mem-basic/servlet (then provide "reza" and "secret1" in the dialog presented by the browser)
  * Note that /servlet is a protected resource and the dialog presented comes from the browser itself and not from the application
* **app-mem-form** - As app-mem but uses the Jakarta Security provided FORM authentication mechanism.
  * Test URL: http://localhost:8080/app-mem-form/servlet (then provide "reza" and "secret1" in the form presented)
  * Note that /servlet is a protected resource. The authentication mechanism forwards to /login-servlet, which posts back to j_security_check. The authentication mechanism listens to this URL and if authentication succeeds a redirect back to /servlet is send.
* **app-mem-customform** - As app-mem but uses the Jakarta Security provided CUSTOM FORM authentication mechanism.
  * Test URL: http://localhost:8080/app-mem-customform/servlet (then provide "reza" and "secret1" in the form presented)
  * Note that /servlet is a protected resource. The authentication mechanism forwards to /login.xhtml, which posts back to itself. A backing bean then programmatically resumes the authentication dialog and if authentication succeeds a redirect back to /servlet is send.
* **app-multiple-store** - As app-custom but uses two identity stores; 1 that does the authentication (checks username and password match) while the other provides the groups once authentication has succeeded.
  * Test URL: http://localhost:8080/app-multiple-store/servlet?name=reza&password=secret1
* **app-multiple-store-backup** - As app-custom but uses two identity stores that are tried in order. First authentication is attempted against the first one, and when that fails it's attempted against the second one. In this example, user "reza" is present in both stores with different passwords, while user "alex" is only present in the second store.
  * Test URL: http://localhost:8080/app-multiple-store/servlet?name=reza&password=secret1 (first store)
  * Test URL: http://localhost:8080/app-multiple-store/servlet?name=reza&password=secret2 (second store)
  * Test URL: http://localhost:8080/app-multiple-store-backup/servlet?name=alex&password=verysecret (second store)
* **app-jaxrs** - As app-custom, but uses a JAX-RS resource instead of a servlet and the mechanism doesn't delegate to an identity store. 
  * Test URL: http://localhost:8080/app-jaxrs/rest/resource/callerName?name=reza&password=secret1 (public resource, name)
  * Test URL: http://localhost:8080/app-jaxrs/rest/resource/hasRoleFoo?name=reza&password=secret1 (public resource, role)
  * Test URL: http://localhost:8080/app-jaxrs/rest/protectedResource/sayHi?name=reza&password=secret1 (protected resource)
* **app-securitycontext-auth** - This example has some aspects from app-mem-customform in that it uses the security context to trigger authentication, but here this happens from a Servlet and a special authentication mechanism is used that only processes a special credential provided with the securityContext.authenticate call.
  * Test URL: http://localhost:8080/app-securitycontext-auth/servlet?name=reza (authenticates as Reza)
  * Test URL: http://localhost:8080/app-securitycontext-auth/servlet?name=rezax (fails authentication via exception)
  * Test URL: http://localhost:8080/app-securitycontext-auth/servlet?name=rezax (fails authentication via status return code)

## Running tests in parallel (`mvn -T<N>`)

The default `mvn verify` already uses the GlassFish pool (provisioned by
`glassfish-pool-maven-plugin`, started/cloned per slot, leased by each test
JVM). Adding `-T<N>` runs reactor modules in parallel and is a large
wall-clock win, 10x faster on average.

The pool itself is parallel-safe (`PoolBootstrap.up` is JVM-wide synchronized
+ idempotent, slot leasing uses `FileChannel.tryLock`), but test modules
have to follow a few rules to be `-T`-safe. Existing modules already comply;
when adding a new one, check the points below.

### 1. No host-port collisions across modules

Modules that start an embedded server bound to `localhost:<port>` (UnboundID
LDAP, Tomcat for the Mitre OP, …) must each pick a distinct port. Under `-T`
two modules on the same port fight: only one binds, the other silently
fails, and tests get cryptic 500s or HTTP timeouts.

Conventions in use:

- LDAP modules: 33389 (`app-ldap`), 33390 (`app-ldap2`), 33391 (`app-ldap3`),
  12389-12413 for `app-ldap-*`. Pick the next free integer when adding one.
- Tomcat (Mitre OP) modules: 8443 + 8005 (`app-openid2`), 8444 + 8006
  (`app-openid3`). Pick another (8445/8007, …) for any new openid-with-Mitre
  module, and keep `server.xml` + the `ProtectedServlet` `providerURI`
  annotation + the antrun `<replace token="http://localhost:8080" value="…">`
  in sync.

### 2. No assumption that GF runs on a known port

Pool slots get ports from `adminBase + (slot-1) * portStride` (default
14848 + N*100), and a test JVM may lease any slot. Do NOT hardcode a slot's
HTTP/HTTPS port in app code. Use `@ArquillianResource URL base` for the
deployed-app URL; for outbound URLs that have to be configured at deployment
time (e.g. Soteria's `OpenIdAuthenticationMechanismDefinition.providerURI`),
use an EL expression backed by a `@RequestScoped`/`@Dependent` CDI bean that
reads `request.getServerName()/getServerPort()` at request time —
`app-openid`'s `OpenIdConfig.getProviderURI()` is the reference.

### 3. Pre-register every slot when an external service validates redirect URIs

When a third-party server (e.g. Mitre OP) validates redirect URIs against a
fixed allowlist, register one entry per *possible* slot. The openid-client
deployment may end up on slot 1, 2, … N, and Mitre rejects any redirect URI
not pre-registered. `app-openid2`/`app-openid3`'s antrun loops slot
1..`${session.request.degreeOfConcurrency}` into `clients.sql` using the
pool's `adminBase` + `portStride` — that property is Maven's `-TN` value
(defaults to 1) and is also the upper bound on how far the pool can grow,
since each Maven thread leases at most one slot at a time.

### 4. Wipe Tomcat `work/` before startup

If a module starts its own Tomcat in pre-integration-test, add
`<delete dir="${tomcat.dir}/work" quiet="true"/>` to the antrun *before*
`startup.sh`. Tomcat's `StandardManager` persists HTTP sessions to
`work/Catalina/localhost/<webapp>/SESSIONS.ser` on shutdown and rehydrates
them at startup; without the wipe, a re-run without `mvn clean` resurrects
the previous run's sessions and can skip flows the test depends on (e.g.
the OpenID consent page).

### 5. Don't race on shared paths in a `<plugins>` execution

Anything inheritable that writes to `${maven.multiModuleProjectDirectory}/…`
runs once per module under `-T` and races. The parent's source-staging step
uses a `mkdir`-based lock + marker file inside an `antrun` so first-acquirer
does the work and others fast-exit; copy that pattern for any new shared
preparation. Plain `maven-dependency-plugin:unpack` into a shared directory
is NOT thread-safe for the first-extraction window even with markers.

## Running the TCK in Docker

(needs updating to recent versions)

Examples for how to build and run the `app-mem-basic` sample in docker. The other TCK tests can be run in the same way.

### Wildfly
```
cd app-mem-basic
mvn clean install docker:build -Pwildfly,wildfly-docker
docker run -it -p 8080:8080 tck-samples/app-mem-basic:wildfly
```  

### Payara
```
cd app-mem-basic
mvn clean install docker:build -Ppayara,payara-docker
docker run -it -p 8080:8080 tck-samples/app-mem-basic:payara
```  
