# TUI4J

![maven build and test](https://github.com/KartoffelChipss/TUI4J/actions/workflows/maven-build-and-test.yml/badge.svg)
[![Maven](https://maven.strassburger.org/api/badge/org.strassburger/tui4j)](https://maven.strassburger.org/browse/org/strassburger/tui4j)

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
  - Flex Layouts (Centering, Justifying, Spacing)
  - Tables
  - Spinners and Loaders
- Command Line Parsing
  - Subcommands
  - Options
  - Arguments
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
        <artifactId>tui4j</artifactId>
        <version>0.0.0</version>
    </dependency>
</dependencies>

<repositories>
  <repository>
    <id>strassburger-repo</id>
    <url>https://maven.strassburger.org/releases</url>
  </repository>
</repositories>
```

### Gradle

```groovy
dependencies {
    implementation 'org.strassburger:tui4j:0.0.0'
}

repositories {
    maven { url 'https://maven.strassburger.org/releases' }
}
```

## API Documentation

The API documentation can be found [here](https://tui4j.strassburger.org).

## Examples

### Formatting and Inputs

```java
package org.example;

import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.FlexJustify;
import org.strassburger.tui4j.formatting.layout.FlexText;
import org.strassburger.tui4j.formatting.layout.Spinner;
import org.strassburger.tui4j.formatting.layout.SpinnerFrames;
import org.strassburger.tui4j.input.*;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.input.validationrules.TextValidationRules;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.console.TerminalConsole;

public class Main {
  public static void main(String[] args) {
    Console console = new TerminalConsole();

    console.println(" ");
    console.println(new FlexText(StyledText.text("Welcome to the TUI4J example application!").fg(AnsiColor.BLUE).bold()));
    console.println(" ");

    String name = console.read(new TextInput()
            .setPrompt("What is your name? ")
            .setInline(true));

    String gender = console.read(new SelectInput<String>()
            .setPrompt("Select your gender: ")
            .addOption("Male", "Male")
            .addOption("Female", "Female")
            .addOption("Other", "Other")
            .setOptionsStyle(StyledText.text(" (%num%) ").fg(AnsiColor.CYAN).append("%label%")));

    String email = console.read(new TextInput()
            .setPrompt("What is your email address?")
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
            ));

    String about = console.read(new MultilineTextInput()
            .setPrompt("Tell me about yourself: ")
            .addValidationRule(TextValidationRules.minLength(30)));

    int age = console.read(new IntegerInput()
            .setPrompt("How old are you? ")
            .addValidationRule(NumberValidationRules.greaterThan(0))
            .addValidationRule(NumberValidationRules.lessThan(150)));

    double height = console.read(new DoubleInput()
            .setPrompt("How tall are you? ")
            .addValidationRule(NumberValidationRules.greaterThan(0.0))
            .addValidationRule(NumberValidationRules.lessThan(3.0)));

    console.println(" ");
    console.println(StyledText.text("Your Inputs:").fg(AnsiColor.BRIGHT_WHITE).bold());
    console.println(new FlexText()
            .addPart(StyledText.text("Name ").fg(AnsiColor.BRIGHT_WHITE))
            .addPart(StyledText.text(" " + name).fg(AnsiColor.BRIGHT_WHITE))
            .setJustify(FlexJustify.SPACE_BETWEEN)
            .setSeparatorChar(StyledText.text(".").fg(AnsiColor.BRIGHT_BLACK))
    );
    console.println(FlexText.keyValue("Email ", " " + email));
    console.println(FlexText.keyValue("Age ", " " + age));
    console.println(FlexText.keyValue("Height (m) ", " " + height));
    console.println(FlexText.keyValue("Gender ", " " + gender));
    console.println(" ");

    boolean shouldContinue = console.read(new BooleanInput()
            .setPrompt("Do you want to continue? (y/n)"));

    if (shouldContinue) {
      console.println("Continuing...");

      Spinner spinner = new Spinner()
              .setMessage(StyledText.text("Loading some stuff..."))
              .setSpinnerColor(AnsiColor.CYAN)
              .setSpinnerFrames(SpinnerFrames.SYNTHWAVE)
              .setSpeedMs(100);
      console.print(spinner);

      for (int i = 0; i < 50; i++) {
        Thread.sleep(100);
        spinner.setMessage(StyledText.text("Loading some stuff... " + (i + 1) * 2 + "%"));
      }

      spinner.stop(StyledText.text("✔ Loading complete!").fg(AnsiColor.GREEN).bold());

    } else {
      console.println("Exiting...");
    }
  }
}
```

### Commands

```java
import org.strassburger.tui4j.command.Argument;
import org.strassburger.tui4j.command.Command;
import org.strassburger.tui4j.command.Option;
import org.strassburger.tui4j.command.exceptions.CommandException;
import org.strassburger.tui4j.formatting.layout.*;
import org.strassburger.tui4j.input.*;

public class Main {
    public static void main(String[] args) {
        String[] testArgs = "farewell franky".split(" ");
        System.out.println("Executing with args: " + String.join(" ", testArgs));
    
        Option<String> nameOption = Option.str("name", "n", "World");
        Argument<String> nameArgument = Argument.str("name", true);
    
        Command command = Command.root()
                .addSubCommand(
                        Command.named("greet")
                                .addOption(nameOption)
                                .setHandler(context -> {
                                  String name = context.getOptionValue(nameOption);
                                  System.out.println("Hello, " + name + "!");
                                }),
                        Command.named("farewell")
                                .addArgument(nameArgument)
                                .setHandler(context -> {
                                  String name = context.getArgumentValue(nameArgument);
                                  System.out.println("Goodbye, " + name + "!");
                                })
                );
    
        try {
            command.execute(testArgs);
        } catch (CommandException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

### Table

```java

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.layout.table.Table;
import org.strassburger.tui4j.formatting.layout.table.styles.ColumnAlign;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyles;
import org.strassburger.tui4j.console.Printer;
import org.strassburger.tui4j.console.TerminalConsole;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    Printer printer = new TerminalConsole();

    List<StyledText> headers = List.of(
            StyledText.text("Name").fg(AnsiColor.BRIGHT_WHITE).bold(),
            StyledText.text("Age").fg(AnsiColor.BRIGHT_WHITE).bold(),
            StyledText.text("City").fg(AnsiColor.BRIGHT_WHITE).bold()
    );

    List<StyledText> row1 = List.of(
            StyledText.text("Alice").fg(AnsiColor.CYAN),
            StyledText.text("30").fg(AnsiColor.GREEN),
            StyledText.text("New York").fg(AnsiColor.MAGENTA)
    );

    List<StyledText> row2 = List.of(
            StyledText.text("Bob").fg(AnsiColor.CYAN),
            StyledText.text("25").fg(AnsiColor.GREEN),
            StyledText.text("Los Angeles").fg(AnsiColor.MAGENTA)
    );

    List<StyledText> row3 = List.of(
            StyledText.text("Charlie").fg(AnsiColor.CYAN),
            StyledText.text("35").fg(AnsiColor.GREEN),
            StyledText.text("Chicago").fg(AnsiColor.MAGENTA)
    );

    Table table = new Table()
            .setStyle(TableStyles.UNICODE_BOX.withBorderColor(AnsiColor.BRIGHT_BLACK))
            .setHasHeader(true)
            .setColumnGrow(0, 1)
            .setColumnAlignment(0, ColumnAlign.START)
            .setColumnAlignment(1, ColumnAlign.CENTER)
            .setColumnAlignment(2, ColumnAlign.END)
            .addRow(headers)
            .addRow(row1)
            .addRow(row2)
            .addRow(row3);

    printer.println(table);
  }
}
```

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](https://github.com/KartoffelChipss/TUI4J/blob/main/LICENSE) file for details.
