# CHANGELOG

# Version 2.5.0-BETA.5
- Fixed `ClassCastException` that could happen when using `HttpURLConnection`
  ([#1053](https://github.com/microsoft/ApplicationInsights-Java/issues/1053))

# Version 2.5.0-BETA.4
- Fixed registration of custom JMX performance counters
  ([#1042](https://github.com/microsoft/ApplicationInsights-Java/issues/1042))
- Fixed `IllegalStateException` that could happen when using `HttpURLConnection`
  ([#1037](https://github.com/microsoft/ApplicationInsights-Java/issues/1037))
- Fixed `NullPointerException` that could happen when using Java 11
  ([#1032](https://github.com/microsoft/ApplicationInsights-Java/issues/1032))

# Version 2.5.0-BETA.3
- Fixed `HttpURLConnection` instrumentation was not capturing outgoing url
  ([#1025](https://github.com/microsoft/ApplicationInsights-Java/issues/1025))
- Added agent logging capture threshold, configurable via `<Logging threshold="warn" />` 
  in the `AI-Agent.xml`, with default threshold `warn`
  ([#1026](https://github.com/microsoft/ApplicationInsights-Java/issues/1026))
- Fixed request telemetry displaying `200` response code for some failed requests
  ([#810](https://github.com/microsoft/ApplicationInsights-Java/issues/810))
- Fixed GC performance counters not working 
  ([#929](https://github.com/microsoft/ApplicationInsights-Java/issues/929))

# Version 2.5.0-BETA.2
- Added back support for `<Class>` custom instrumentation in `AI-Agent.xml`
- Fixed opting out of agent capturing logging via `<Logging enabled="false" />`
- Misc fixes [#969](https://github.com/microsoft/ApplicationInsights-Java/issues/969),
             [#978](https://github.com/microsoft/ApplicationInsights-Java/issues/978)

# Version 2.5.0-BETA
- Added support for Java 9-12
- Added new `applicationinsights-web-auto.jar` artifact that automatically registers the web filter
  by just being present in your dependencies (works for both servlet containers and spring boot standalone)
  - No need to modify `web.xml`, or add `@WebFilter`
- Agent now captures application logging via `Log4j 1.2`, `Log4j2` and `Logback`
  - No need to add `applicationinsights-logging-*.jar` dependency and modify the application's logging configuration
   (e.g. `log4j.properties`, `log4j2.xml`, `logback.xml`)
  - To opt out of this (e.g. if you prefer to capture logging via the Application Insights appenders),
    add `<Logging enabled="false" />` to the `AI-Agent.xml` file inside of the `Builtin` tag
- Agent now automatically captures dependencies for async requests by tracking the request across multiple threads
- Agent now captures JDBC queries for all JDBC drivers
- Added additional HTTP client support
  - `java.net.HttpURLConnection`
  - Apache HttpClient 3.x (4.x was already previously supported)
  - OkHttp3
  - OkHttp2 (previously did not propagate distributed trace context)
- Agent now sets `Operation Name` (used for aggregating similar requests) automatically
  based on Spring `@RequestMapping` and JAX-RS `@Path`
  - No need for registering `RequestNameHandlerInterceptorAdapter` or writing own interceptor
  - (also supports Struts, based on action class / method name)
- Agent now supports multiple applications deployed in the same application server
  - (support was removed temporarily in the 2.4.0 release)
- Simplified JBoss and Wildfly deployment when using the agent
  - No need for setting `jboss.modules.system.pkgs`, `java.util.logging.manager` and `-Xbootclasspath`
- Added `<RoleName>` tag in `ApplicationInsights.xml` to simplify role name configuration
  - No need to write a custom `ContextInitializer`
- Removed support for `<Class>` custom instrumentation in `AI-Agent.xml`
- Removed support for `<RuntimeException>` custom instrumentation in `AI-Agent.xml`

# Version 2.4.1
- Fix [#910](https://github.com/microsoft/ApplicationInsights-Java/issues/910) Fixed correlation id serialization.
- Upgraded spring boot dependencies in spring-boot-starter from 1.5.9 to 1.5.21.

# Version 2.4.0
- Updated Spring Boot Starter version number to track with the SDK version.
- Upgrade gradle to 5.3.1
- Fix [#907](https://github.com/microsoft/ApplicationInsights-Java/issues/907) - ensure string compare is case insensitive when running a SQL explain on a select statement.
- Fixed ThreadLocal leak [#887](https://github.com/microsoft/ApplicationInsights-Java/pull/887)
- Fixed QuickPulse schema version [#904](https://github.com/microsoft/ApplicationInsights-Java/pull/904)
- Added retries to CDSProfileFetcher [#901](https://github.com/microsoft/ApplicationInsights-Java/pull/901)
- Fix [#919](https://github.com/microsoft/ApplicationInsights-Java/issues/919) - Fixed issue when adding duplicate Windows performance counter.
- Added caching of sdk version id, reducing number of file IO operations [#896](https://github.com/microsoft/ApplicationInsights-Java/pull/896)
- Fixed bug with live metrics (QuickPulse) where request/dependency durations were being truncated to the millisecond.
- Misc stability improvements
[#932](https://github.com/microsoft/ApplicationInsights-Java/pull/932)
[#941](https://github.com/microsoft/ApplicationInsights-Java/pull/941)
[#945](https://github.com/microsoft/ApplicationInsights-Java/pull/945)
[#946](https://github.com/microsoft/ApplicationInsights-Java/pull/946)
[#947](https://github.com/microsoft/ApplicationInsights-Java/pull/947)
[#948](https://github.com/microsoft/ApplicationInsights-Java/pull/948)

# Version 2.4.0-BETA
- Removed support for multiple apps instrumented with single JVM Agent. Instrumentation will only work for single apps
  in application server.
- Fixed [#749](https://github.com/Microsoft/ApplicationInsights-Java/issues/749) introduce support for PostgreSQL jdbc4 prepared statements.
- Introduced support for Manual Async and Explicit Multithreading correlation.
- <strike>Introduced `setRequestTelemetryContext` API in `WebTelemetryModule` Interface.</strike>
- Introduced experimental API's `AIHttpServletListner`, `HttpServerHandler`, `ApplicationInsightsServletExtractor`
  and `HttpExtractor`.
- Deprecated `ApplicationInsightsHttpResponseWrapper`
- Fixed [#826](https://github.com/Microsoft/ApplicationInsights-Java/issues/826) Remove duplicate provider.
- Fix incorrect sdk version being sent in Quick Pulse payload.
- Fix [#882](https://github.com/Microsoft/ApplicationInsights-Java/issues/882) Dependency metrics does not show up on
Livemetrics UX.

# Version 2.3.1
- Fixed [#799](https://github.com/Microsoft/ApplicationInsights-Java/issues/799) Removed dependency on Guava vulnerable to [CVE-2018-10237](https://nvd.nist.gov/vuln/detail/CVE-2018-10237).

# Version 2.3.0
- Introducing Application Insights SpringBoot Starter 1.1.1 (GA VERSION).
- Fix [#784](https://github.com/Microsoft/ApplicationInsights-Java/issues/784) Shade guava dependency.
- [#716](https://github.com/Microsoft/ApplicationInsights-Java/issues/716) Introduced W3C Distributed tracing protocol.

# Version 2.2.1
- Fixed [#767](https://github.com/Microsoft/ApplicationInsights-Java/issues/767). Updated gRPC dependencies which inlcudes latest netty version.
- Fixed [#751](https://github.com/Microsoft/ApplicationInsights-Java/issues/751). Added support for absolute paths for log file output.
- Abstracted Internal Logger in Core into separate reusable module.
- Deprecated InternalAgentLogger in favor of InternalLogger for better consistency.
- Fixed [#752](https://github.com/Microsoft/ApplicationInsights-Java/issues/752). Agent now supports diagnostic writing logs to file.
- Added ability to configure FileLogger for SpringBootStarter.
- Fixed the `WebRequestTrackingFilter` order in FilterChain for SpringBoot Starter.


# Version 2.2.0
- Introduces SpringBoot Starter 1.1.0-BETA
- Starter now respects autoconfiguration for Micrometer metrics.
- Starter adds autoconfiguration for Local Forwarder Telemetry Channel. (Please look at readme for details on configuration.)
- Fix [#712](https://github.com/Microsoft/ApplicationInsights-Java/issues/712) the thread shutdown issue in SpringBoot Starter by registering `ApplicationInsightsServletContextListener`.
- SpringBoot Starter now supports reading iKey using all the variable names as core sdk.
- Starter would no longer support relaxed binding of ikey property due to complex conditional need and backport problems with RelaxedBinder from Boot 2 to 1.5.x.
- `InterceptorRegistry` class no longer has `@EnableWebMvc` annotation as it breaks springboot autoconfig.
- Deprecated `getRoleName`/`setRoleName` and `getRoleInstance`/`setRoleInstance` in `DeviceContext`. Introduced `CloudContext` to hold replacements, `getRole`/`setRole` and `getRoleInstance`/`setRoleInstance`, respectively.
- Introduced `CloudInfoContextInitializer` to set roleInstance in `CloudContext`. This new initializer is included by default and therefore will not affect the current tags.
- Adds `WebAppNameContextInitializer` for use with the `WebRequestTrackingFilter`.
- Adds `LocalForwarderChannel` for use with the [LocalForwarder](https://github.com/Microsoft/ApplicationInsights-LocalForwarder).
- Removes Servlet 3.0 annotations from `WebRequestTrackingFilter` and `ApplicationInsightsServletContextListener` which were causing issues in certain cases. This will allow easier customization of the filter. To use the listener moving forward, it will need to be defined in web.xml.
- Fix QuickPulse post interval bug from 5 seconds to 1 second.

# Version 2.1.2
- Fix [#676](https://github.com/Microsoft/ApplicationInsights-Java/issues/676). This fixes the HTTP dependency collection when using NetFlix Zuul Library.
- Remove the method `httpMethodFinishedWithPath` from the interface `ImplementationCoordinator.java` as the associated instrumentation
  explicitly depended on `HttpUriRequest` class of ApacheHttpClient which is not always true.
- Fix [#662](https://github.com/Microsoft/ApplicationInsights-Java/issues/662). Updated thread pool to properly shutdown all threads.
- Fix [#661](https://github.com/Microsoft/ApplicationInsights-Java/issues/661). We now properly shadow the com.google.thirdparty package.
- Fix [#686](https://github.com/Microsoft/ApplicationInsights-Java/issues/686). Added required attributes to @WebFilter annotation.

# Version 2.1.1
- Introducing support for SpringBoot via Application-Insights-SpringBoot-Starter [#646](https://github.com/Microsoft/ApplicationInsights-Java/pull/646). This is currently in beta.
- In order to add support for SpringBoot starter some fields in core SDK are made public.
- Introduced public constructor in `InProcessTelemetryChannel.java` class.
- Introduced a public method `getActiveWithoutInitializingConfig()` in `TelemetryConfiguration.java` class.

# Version 2.1.0
- Introduced Heartbeat feature which sends periodic heartbeats with basic information about application and runtime to Application Insights.
- Enable support for system properties in the instrumentation key resolving component.
- Performance improvements during initialization.
- Fix [#647](https://github.com/Microsoft/ApplicationInsights-Java/issues/647). If a performance counter cannot be computed, it will not report a placeholder value (`-1`). Instead, it does not report any value.

## Version 2.0.2
- Fix incorrect success flag set when capturing HTTP Dependency.
- Fix [#577](https://github.com/Microsoft/ApplicationInsights-Java/issues/577), removed HttpFactory class as it was not being used.
- Fixed issue with sessionId not being set in request telemetry due to date parsing issues.
- Fix [#616](https://github.com/Microsoft/ApplicationInsights-Java/issues/616), added a way to have real time SDK Logs when logging on File.
- Fix [#609](https://github.com/Microsoft/ApplicationInsights-Java/issues/609), fixes the inaccurate timestamp recorded with JMX Metrics.
- Added a way to configure MaxInstantRetries from XML.
- Added the ability to have cold SDK initialization (no logs except critical logAlways messages)
- Fix issue when dependency start time was not being recorded correctly.
- Fixed [#533](https://github.com/Microsoft/ApplicationInsights-Java/issues/533) HTTP Dependency Telemetry now matches with .NET SDK.
- Introduced public method `httpMethodFinishedWithPath(String identifier, String method, String path, String correlationId, String uri, String target, int result, long delta)`
  to support instrumentation of Path of URI in HTTP requests.
- `httpMethodFinished(String identifier, String method, String correlationId, String uri, String target, int result, int delta)` is now marked as deprecated
- Logger Messages now being pushed as custom dimension when reporting exceptions via Loggers. (#400)
- Enhanced Log4j2 appender to support basic parameters including Filters, Layouts and includeException. (#348)
- Fixed PageView telemetry data not being reported.
- Fixed Issue [#526](https://github.com/Microsoft/ApplicationInsights-Java/issues/526) (NPE in MapUtil.copy())
- Fixed Issue [#513](https://github.com/Microsoft/ApplicationInsights-Java/issues/513) (Memory leak in SDKShutdownActivity). This fix upgrades our Servlet version from 2.5 to 3.0. The SDK must now be run on an application server supporting Servlet 3.0.
- Fixed Issue [#504](https://github.com/Microsoft/ApplicationInsights-Java/issues/504) (SDK initialization happens twice) to improve startup performance.

## Version 2.0.1
- Fix Inconsistency in artifact names in POM files

## Version 2.0.0
- Upgraded logback dependency version to 1.2.3
- Improved FixedRateSampling so that it also supports Sampling Percentage set by users programmatically. Fixes [issue #535](https://github.com/Microsoft/ApplicationInsights-Java/issues/535)
- Fault Tolerance improvements: introducing retries and exponential backoff capabilities with disk persistence.
- [Issue #499](https://github.com/Microsoft/ApplicationInsights-Java/pull/499): Fix handling of NaN and +/-Infinity in JSON serializer
- [Issue #506](https://github.com/Microsoft/ApplicationInsights-Java/pull/506): Null Reference Check causing Null Pointer Exception in `TelemetryCorrelationUtils.java`

## Version 2.0.0-BETA
- Updating various dependencies to latest version
- Introducing public class CustomClassWriter in Agent to enable finding common super classes used for Agent instrumentation without loading it
- Introducing Parametrized constructor in WebRequestTrackingFilter to ensure name binding
- Fixed Issue #428 (Agent cannot capture oracle dependencies)
- Fixed Issue #418 (Java.lang.verify error caused while capturing sql dependencies in JDK 1.8)
- Fixed Issue #286, #277 (Issues in capturing preparedStatement calls)
- Fixed Issue #365 (Relocate all web jars)
- Fixed Issue #276 (Instrumentation issues with HTTP Client)
- Introducing public method 'getIncludedTypes' and 'getExcludedTypes' in TelemetryProcessorXmlElement.
- Introducing class 'com.microsoft.applicationinsights.internal.config.ParamIncludedTypeXmlElement'.
- Introducing class 'com.microsoft.applicationinsights.internal.config.ParamExcludedTypeXmlElement'
- Introducing class 'com.microsoft.applicationinsights.internal.channel.samplingV2.SamplingScoreGeneratorV2'
- Introducing Telemetry Processor 'com.microsoft.applicationinsights.internal.channel.samplingV2.FixedRateSamplingTelemetryProcessor'
- Introducing FixedRate Sampling v2 Using Telemetry Processors
- Fixed issue #436 (TraceTelemetry with Severity is not shown in UI). This fixes a regression issue with `TelemetryClient.trackTrace` and `TelemetryClient.trackException`.
- Introducing support for [cross-component correlation](https://docs.microsoft.com/en-us/azure/application-insights/application-insights-correlation). Addresses issue [#457](https://github.com/Microsoft/ApplicationInsights-Java/issues/457).
- Changed signature of com.microsoft.applicationinsights.internal.agent.CoreAgentNotificationHandler.httpMethodFinished. It now includes correlation information.
- Compilation now targets Java 1.7. Java 1.6 is no longer supported.
- Adding system property `applicationinsights.configurationDirectory` to allow to explicitly set directory containing the config file.

## Version 1.0.10
- `track()` method of 'com.microsoft.applicationinsights.TelemetryClient' is now modified. No longer performing pre-sanitization
- All Sanitization will now occur in `com.microsoft.applicationinsights.telemetry.JsonTelemetryDataSerializer` class.
- Method `sanitize` of interface `com.microsoft.applicationinsights.telemetry.Telemetry` is now obsolete.
- The auto generated schema classes now have writer method with limits according to endpoint.
- Fixed issue #403 (Exceeding property length invalidates custom event)
- Fixed issue #401 (Custom key and property sanitized)
- Fixed Request Telemetry Sending bug with new schema.
- Fixed reliability issue with Jedis client dependency collector
- Fixed Request Telemetry Sending bug with new schema
- Schema updated to the latest version. Changes in internal namespace `core/src/main/java/com/microsoft/applicationinsights/internal/schemav2`.
- Class `SendableData` in internal namespace deleted.
- Class `com.microsoft.applicationinsights.telemetry.BaseSampleSourceTelemetry` takes generic class qualifier `Domain` instead of `SendableData`.
- Class `com.microsoft.applicationinsights.telemetry.BaseTelemetry` takes generic class qualifier `Domain` instead of `SendableData`.
- Methods `getExceptionHandledAt` and `setExceptionHandledAt` of `ExceptionTelemetry` marked obsolete and do not do anything.
- Obsolete methods of `RemoteDependencyTelemetry`:  `getCount`, `setCount`, `getMin`, `setMin`, `getMax`, `setMax`, `getStdDev`, `setStdDev`, `getDependencyKind`, `setDependencyKind`, `getAsync`, `setAsync`, `getDependencySource`, `setDependencySource`.
- Obsolete methods of `RequestTelemetry`: `getHttpMethod`, `setHttpMethod`.
- Add option to configure instrumentation key via `APPINSIGHTS_INSTRUMENTATIONKEY` environment variable for consistency with other SDKs.
- Fix the issue where `track(...)` of `TelemetryClient` class was overwriting the provided telemetry timestamp.
- Changed the policy on failed sent requests to delay retrying for 5 minutes instead of immediately retrying.

## Version 1.0.9
- Fix the issue of infinite retry and connection drain on certificate error by updating the version of http client packaged with the SDK.

## Version 1.0.8
- #367 Updated the agent project not to be transitive when resolved for core
- #374 Updated the core JAR to remove the transitive dependencies for guava, removed the transitive dependency from core and agent, & updated to the latest version of the httpclient
- #376 Delaying retry on send when no connection is found
- #377 Bug fixes for Live Metrics Stream integration
- #378 Added Live Metrics Stream URL filter and fixed duration
- #379 Updated Gradle to 3.5
- #380 Delaying retry on send when ConnectionException is thrown
- #381 Delaying retry on send when HostUnknownException is thrown

## Version 1.0.7
- Use [Shadow plugin](http://imperceptiblethoughts.com/shadow/) to create self contained JAR files in the SDK
- Allow marking a metric as custome performance counter. See [here](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Mark-Metric-as-Custom-Performace-Counter)
- Better support for Visual Studio Code for edting the Application Insights Java SDK Code
- Added sampling support. See [here](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Sampling)
- Allow changing the performance counter collection frequency and added hooks before and after sending the PC see [here](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Perfomance-Counters-Collection:-Setting-collection-frequency) and [here](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Perfomance-Counters-Collection:-Plugin)
- Agent built-in types are now off by default. Support wildcard to monitor multiple classes in the agent.
- Add dependency type in the agent configuration see [here](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Configure-Dependecy-Type-in-the-Agent-Configuration)


## Version 1.0.6
- [Allow reporting runtime exceptions](https://github.com/Microsoft/ApplicationInsights-Java/pull/302)
- [Collect JVM heap memory usage and automatic deadlock detection](https://github.com/Microsoft/ApplicationInsights-Java/pull/313)
- [Make frequency of performance counter collection configurable](https://github.com/Microsoft/ApplicationInsights-Java/pull/316)
- Plugin to allow intervening before and after performance counters collection [change](https://github.com/Microsoft/ApplicationInsights-Java/pull/328) [docs](https://github.com/Microsoft/ApplicationInsights-Java/wiki/Perfomance-Counters-Collection:-Plugin)
- [Comply to server side throttling](https://github.com/Microsoft/ApplicationInsights-Java/pull/321)
- [Upgrade to Gradle 3.0 build](https://github.com/Microsoft/ApplicationInsights-Java/pull/325)
- [Allow running Java agent outside of webapp context](https://github.com/Microsoft/ApplicationInsights-Java/pull/326)
- [Built in instrumentation for MuleESB client](https://github.com/Microsoft/ApplicationInsights-Java/pull/330)
- [Built in instrumentation for current Apache Derby DB](https://github.com/Microsoft/ApplicationInsights-Java/pull/333)

## Version 1.0.5
- [Added proxy support](https://github.com/Microsoft/ApplicationInsights-Java/pull/290).
- [Treat HTTP response codes below 400 (and 401) as success](https://github.com/Microsoft/ApplicationInsights-Java/pull/295).
- [Fix agent load problem on Linux](https://github.com/Microsoft/ApplicationInsights-Java/pull/287).
- [Improve cleanup and exception handling in WebRequestTrackingFilter](https://github.com/Microsoft/ApplicationInsights-Java/pull/294).
- [Propagate log context properties in custom properties](https://github.com/Microsoft/ApplicationInsights-Java/pull/288).
- [Ability to filter out certain telemetry from being sent](https://github.com/Microsoft/ApplicationInsights-Java/pull/296).
- Some other miscellaneous fixes and improvements.

## Version 1.0.4
- Interim version replaced by 1.0.5 on August 2016

## Version 1.0.3
- Align to a new BOND schema used by the Application Insights data collection endpoints.

## Version 1.0.2
- Prevent from overriding the instrumentation key using the one specified in the config when it's provided explicitly in code.
- Handle all successfull HTTP status codes and report the relevant HTTP Requests as successful.
- Handle all exceptions thrown by the ConfigurationFileLocator .

## Version 1.0.1
- The [Java agent](app-insights-java-agent.md) collects dependency information about the following:
    - HTTP calls made via HttpClient, OkHttp and RestTemplate (Spring).
    - Calls to Redis made via the Jedis client. When a configurable threshold is passed, the SDK will also fetch the call arguments.
    - JDBC calls made with Oracle DB and Apache Derby DB clients.
    - Support the 'executeBatch' query type for prepared statements – The SDK will show the statement with the number of batches.
    - Provide the query plan for JDBC clients that has support for that (MySql, PostgreSql) – The query plan is fetched only when a configurable threshold is crossed

## Version 1.0.0
- Adding support for the Application Insights writer plugin for CollectD.
- Adding support for the Application Insights Java agent.
- Fix for a compatibility issue with supporting HttpClient versions 4.2 and later.

## Version 0.9.6
- Make the Java SDK compatible with servlet v2.5 and HttpClient pre-v4.3.
- Adding support for Java EE interceptors.
- Removing redundant dependencies from the Logback appender.

## Version 0.9.5
- Fix for an issue where custom events are not correlated with Users/Sessions due to cookie parsing errors.
- Improved logic for resolving the location of the ApplicationInsights.xml configuration file.
- Anonymous User and Session cookies will not be generated on the server side. To implement user and session tracking for web apps, instrumentation with the JavaScript SDK is now required – cookies from the JavaScript SDK are still respected. Note that this change may cause a significant restatement of user and session counts as only user-originated sessions are being counted now.

## Version 0.9.4
- Support collecting performance counters from 32-bit Windows machines.
- Support manual tracking of dependencies using a new ```trackDependency``` method API.
- Ability to tag a telemetry item as synthetic, by adding a ```SyntheticSource``` property to the reported item.

