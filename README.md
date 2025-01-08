# TUI4J

![maven build and test](https://github.com/KartoffelChipss/TUI4J/actions/workflows/maven-build-and-test.yml/badge.svg)

TUI4J is a Java-based library for creating text-based user interfaces (TUIs).

## Features

- Flexible Inputs
  - Text Input
  - Multiline Text Input
  - Number Input
  - Boolean Input
  - Selection Input
  - Enter-To-Continue Input
- Easy text formatting
  - Colors
  - Centering
  - Spacing
- Customizable
  - Custom Input Prompts
  - Custom Input Validators

## Installation

Make sure to replace `1.0.0` with the version you want to use.

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.KartoffelChipss</groupId>
        <artifactId>TUI4J</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Gradle

```groovy
dependencies {
    implementation 'com.github.KartoffelChipss:TUI4J:1.0.0'
}

repositories {
    maven { url 'https://jitpack.io' }
}
```