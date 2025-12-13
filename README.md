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
  - Date Input
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
        <groupId>org.strassburger</groupId>
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
    implementation 'org.strassburger:TUI4J:0.0.0'
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

import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.FlexJustify;
import org.strassburger.tui4j.formatting.layout.FlexText;
import org.strassburger.tui4j.input.*;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.input.validationrules.TextValidationRules;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.printer.ConsolePrinter;
import org.strassburger.tui4j.printer.Printer;

public class Main {
    public static void main(String[] args) {
        Printer printer = new ConsolePrinter();
    
        printer.println(" ");
        printer.println(new FlexText(StyledText.text("Welcome to the TUI4J example application!").fg(AnsiColor.BLUE).bold()));
        printer.println(" ");
    
        String name = new TextInput()
                .setLabel("What is your name? ")
                .setInline(true)
                .read();
    
        String gender = new SelectInput<String>()
                .setLabel("Select your gender: ")
                .addOption("Male", "Male")
                .addOption("Female", "Female")
                .addOption("Other", "Other")
                .setOptionsStyle(StyledText.text(" (%num%) ").fg(AnsiColor.CYAN).append("%label%"))
                .read();
    
        String email = new TextInput()
                .setLabel("What is your email address?")
                .addValidationRule(
                        new ValidationRule<String>() {
                            @Override
                            public boolean validate(String s) {
                                return s.contains("@") && s.contains(".");
                            }
      
                            @Override
                            public StyledText getErrorMessage() {
                                return StyledText.text("Email address must contain '@' and '.'").fg(AnsiColor.RED);
                            }
                        }
                )
                .read();
    
        String about = new MultilineTextInput()
                .setLabel("Tell me about yourself: ")
                .addValidationRule(TextValidationRules.minLength(30))
                .read();
    
        int age = new IntegerInput()
                .setLabel("How old are you? ")
                .addValidationRule(NumberValidationRules.greaterThan(0))
                .addValidationRule(NumberValidationRules.lessThan(150))
                .read();
    
        double height = new DoubleInput()
                .setLabel("How tall are you? ")
                .addValidationRule(NumberValidationRules.greaterThan(0.0))
                .addValidationRule(NumberValidationRules.lessThan(3.0))
                .read();
    
        printer.println(" ");
        printer.println(StyledText.text("Your Inputs:").fg(AnsiColor.BRIGHT_WHITE).bold());
        printer.println(new FlexText()
                .addPart(StyledText.text("Name ").fg(AnsiColor.BRIGHT_WHITE))
                .addPart(StyledText.text(" " + name).fg(AnsiColor.BRIGHT_WHITE))
                .setJustify(FlexJustify.SPACE_BETWEEN)
                .setSeparatorChar(StyledText.text(".").fg(AnsiColor.BRIGHT_BLACK))
        );
        printer.println(FlexText.keyValue("Email ", " " + email));
        printer.println(FlexText.keyValue("Age ", " " + age));
        printer.println(FlexText.keyValue("Height (m) ", " " + height));
        printer.println(FlexText.keyValue("Gender ", " " + gender));
        printer.println(" ");
    
        boolean shouldContinue = new BooleanInput()
                .setLabel("Do you want to continue? (y/n)")
                .read();
    
        if (shouldContinue) {
            printer.println("Continuing...");
        } else {
            printer.println("Exiting...");
        }
    }
}
```

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](https://github.com/KartoffelChipss/TUI4J/blob/main/LICENSE) file for details.
