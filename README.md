# ProGuard Service Mapper
[![GitHub license](https://img.shields.io/github/license/GiantTreeLP/proguard-services-mapper)](https://github.com/GiantTreeLP/proguard-services-mapper/blob/main/LICENSE)
[![Maven Package](https://github.com/GiantTreeLP/proguard-services-mapper/actions/workflows/publish.yml/badge.svg)](https://github.com/GiantTreeLP/proguard-services-mapper/actions/workflows/maven-publish.yml)
![GitHub branch checks state](https://img.shields.io/github/checks-status/GiantTreeLP/proguard-services-mapper/main)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.gianttreelp.proguardservicesmapper/proguard-services-mapper-common)](https://search.maven.org/artifact/com.github.gianttreelp.proguardservicesmapper/proguard-services-mapper-common)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.github.gianttreelp.proguardservicesmapper/proguard-services-mapper-common?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/com/github/gianttreelp/proguardservicesmapper/)


This is a service mapper for the [ProGuard](https://proguard.sourceforge.io/)
Java bytecode obfuscator.

It fixes the issue that files in the `META-INF/services` directory are not renamed or adapted to the new class names.

It replaces the original Jar file with a new one that contains the renamed services.

It consists of the following modules:

* [`proguard-service-mapper`](#proguard-service-mapper):
  The service mapper itself.
* [`proguard-service-mapper-cli`](#proguard-service-mapper-cli):
  The command line interface.
* [`proguard-service-mapper-maven`](#proguard-service-mapper-maven):
  The Maven plugin.

## ProGuard Service Mapper CLI

For convenience, the service mapper can be run from the command line. Withouth any additional arguments, it will print
help information.

### Usage

```shell
$ java -jar proguard-service-mapper-cli-<version>.jar -i <input-file> -m <mapping-file>
```

## ProGuard Service Mapper Maven

This plugin is intended to be integrated into your Maven build process and run after
the [ProGuard Maven plugin](https://wvengen.github.io/proguard-maven-plugin/).

### Usage in Maven

```xml
<plugin>
  <groupId>com.github.gianttreelp.proguardservicesmapper</groupId>
  <artifactId>proguard-service-mapper-maven-plugin</artifactId>
  <version>1.1-SNAPSHOT</version> <!-- Update with the version you want to use, preferably the latest -->
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>map-proguard</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <input><!-- The obfuscated input file --></input>
    <mapping><!-- The mapping file from ProGuard --></mapping>
  </configuration>
</plugin>
```
