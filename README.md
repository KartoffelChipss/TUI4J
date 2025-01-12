# TUI4J

![maven build and test](https://github.com/KartoffelChipss/TUI4J/actions/workflows/maven-build-and-test.yml/badge.svg)
[![Release](https://jitpack.io/v/KartoffelChipss/TUI4J.svg)](https://jitpack.io/#KartoffelChipss/TUI4J)
![Monthly download statistics](https://jitpack.io/v/KartoffelChipss/TUI4J/month.svg)

TUI4J is a Java-based library for creating text-based user interfaces (TUIs).

## Demo

![Demo](https://f.j4n.net/tui_demo.gif)

*You can find the code for this demo [here](#example-usage).*

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

## Example Usage

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

## Color Codes

### Text Colors

| Code  | Color  | Preview  |
|-------|--------|----------|
| &0    | Black  | <span style="color:black;">&0 Black</span> |
| &1    | Blue   | <span style="color:blue;">&1 Blue</span> |
| &2    | Green  | <span style="color:green;">&2 Green</span> |
| &3    | Cyan   | <span style="color:cyan;">&3 Cyan</span> |
| &4    | Red    | <span style="color:red;">&4 Red</span> |
| &5    | Magenta| <span style="color:magenta;">&5 Magenta</span> |
| &6    | Yellow | <span style="color:yellow;">&6 Yellow</span> |
| &7    | White  | <span style="color:white;">&7 White</span> |
| &8    | Dark Gray | <span style="color:#808080;">&8 Dark Gray</span> |
| &9    | Blue   | <span style="color:#0000FF;">&9 Bright Blue</span> |
| &a    | Green  | <span style="color:#00FF00;">&a Bright Green</span> |
| &b    | Cyan   | <span style="color:#00FFFF;">&b Bright Cyan</span> |
| &c    | Red    | <span style="color:#FF0000;">&c Bright Red</span> |
| &d    | Magenta| <span style="color:#FF00FF;">&d Bright Magenta</span> |
| &e    | Yellow | <span style="color:#FFFF00;">&e Bright Yellow</span> |
| &f    | White  | <span style="color:#FFFFFF;">&f Bright White</span> |
| &l    | Bold   | <strong>&l Bold</strong> |
| &n    | Underline | <span style="text-decoration:underline;">&n Underline</span> |
| &r    | Reset  | &r Reset |

### Background Colors

| Code  | Color  | Preview  |
|-------|--------|----------|
| &x0   | Black  | <span style="background-color:black; color:white;">&x0 Black</span> |
| &x1   | Blue   | <span style="background-color:blue; color:white;">&x1 Blue</span> |
| &x2   | Green  | <span style="background-color:green; color:white;">&x2 Green</span> |
| &x3   | Cyan   | <span style="background-color:cyan; color:black;">&x3 Cyan</span> |
| &x4   | Red    | <span style="background-color:red; color:white;">&x4 Red</span> |
| &x5   | Magenta| <span style="background-color:magenta; color:white;">&x5 Magenta</span> |
| &x6   | Yellow | <span style="background-color:yellow; color:black;">&x6 Yellow</span> |
| &x7   | White  | <span style="background-color:white; color:black;">&x7 White</span> |
| &x8   | Dark Gray | <span style="background-color:#808080; color:white;">&x8 Dark Gray</span> |
| &x9   | Blue   | <span style="background-color:#0000FF; color:white;">&x9 Bright Blue</span> |
| &xa   | Green  | <span style="background-color:#00FF00; color:black;">&xa Bright Green</span> |
| &xb   | Cyan   | <span style="background-color:#00FFFF; color:black;">&xb Bright Cyan</span> |
| &xc   | Red    | <span style="background-color:#FF0000; color:white;">&xc Bright Red</span> |
| &xd   | Magenta| <span style="background-color:#FF00FF; color:white;">&xd Bright Magenta</span> |
| &xe   | Yellow | <span style="background-color:#FFFF00; color:black;">&xe Bright Yellow</span> |
| &xf   | White  | <span style="background-color:#FFFFFF; color:black;">&xf Bright White</span> |

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](https://github.com/KartoffelChipss/TUI4J/blob/main/LICENSE) file for details.
