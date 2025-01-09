# TUI4J

![maven build and test](https://github.com/KartoffelChipss/TUI4J/actions/workflows/maven-build-and-test.yml/badge.svg)
[![Release](https://jitpack.io/v/KartoffelChipss/TUI4J.svg)](https://jitpack.io/#KartoffelChipss/TUI4J)
![Monthly download statistics](https://jitpack.io/v/KartoffelChipss/TUI4J/month.svg)

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

Make sure to replace `0.0.0` with the version you want to use. (You can find the latest version [here](https://jitpack.io/#KartoffelChipss/TUI4J))

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.KartoffelChipss</groupId>
        <artifactId>TUI4J</artifactId>
        <version>0.0.0</version>
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
    implementation 'com.github.KartoffelChipss:TUI4J:0.0.0'
}

repositories {
    maven { url 'https://jitpack.io' }
}
```

## API Documentation

The API documentation can be found [here](https://TUI4J.j4n.net).

## Usage

```java
package org.example;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.*;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.input.validationrules.TextValidationRules;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

public class Main {
    public static void main(String[] args) {
        Printer.println(" ");
        Printer.printCentered("&9&lWelcome to the TUI4J example application!");
        Printer.println(" ");

        String name = new TextInput()
                .setLabel("What is your name? ")
                .setInline(true)
                .read();

        String email = new TextInput()
                .setLabel("What is your email address?")
                .addValidationRules(
                        new ValidationRule<String>() {
                            @Override
                            public boolean validate(String s) {
                                return s.contains("@") && s.contains(".");
                            }

                            @Override
                            public String getErrorMessage() {
                                return "Email address must contain '@' and '.'";
                            }
                        }
                )
                .read();

        String about = new MultilineTextInput()
                .setLabel("Tell me about yourself: ")
                .addValidationRules(
                        TextValidationRules.minLength(30)
                )
                .read();

        int age = new IntegerInput()
                .setLabel("How old are you? ")
                .addValidationRules(
                        NumberValidationRules.greaterThan(0),
                        NumberValidationRules.lessThan(150)
                )
                .read();

        double height = new DoubleInput()
                .setLabel("How tall are you? ")
                .addValidationRules(
                        NumberValidationRules.greaterThan(0.0),
                        NumberValidationRules.lessThan(3.0)
                )
                .read();

        Printer.println(" ");
        Printer.println("&f&lYour Inputs:");
        Printer.printSpaceBetween("&fName", "&f" + name, "&7.");
        Printer.printSpaceBetween("&fEmail", "&f" + email, "&7.");
        Printer.printSpaceBetween("&fAge", "&f" + age, "&7.");
        Printer.printSpaceBetween("&fHeight", "&f" + height, "&7.");
        Printer.println(" ");

        boolean shouldContinue = new BooleanInput()
                .setLabel("Do you want to continue? ")
                .read();
    }
}
```

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](https://github.com/KartoffelChipss/TUI4J/blob/main/LICENSE) file for details.