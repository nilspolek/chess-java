# Chess-java
[![Build Status](https://drone.webnils.de/api/badges/nilspolek/SchachPrjekt2/status.svg)](https://drone.webnils.de/nilspolek/SchachPrjekt2)  [![](https://jitpack.io/v/nilspolek/chess-java.svg)](https://jitpack.io/#nilspolek/chess-java)  [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Install
## Maven
```xml

<repositories>
	<repository>
		<id>jitpack.io</id>
 		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.nilspolek</groupId>
	<artifactId>chess-java</artifactId>
	<version>LATEST</version>
</dependency>
```
## Gradle
```kotlin
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
dependencies {
	implementation 'com.github.nilspolek:chess-java:Tag'
}
```
