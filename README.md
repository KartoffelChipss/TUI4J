# TUI4J

![maven build and test](https://github.com/KartoffelChipss/TUI4J/actions/workflows/maven-build-and-test.yml/badge.svg)
[![Maven](https://maven.strassburger.org/api/badge/org.strassburger/tui4j)](https://maven.strassburger.org/browse/org/strassburger/tui4j)

TUI4J is a Java-based library for creating text-based user interfaces (TUIs).

## Demo

![Demo](https://f.j4n.net/tui_demo.gif)

*You can find the code for this demo [here](#formatting-and-inputs).*

## Features

- Flexible Inputs
  - Text and Multiline Text Input
  - Number Input (Integer, Double)
  - Boolean Input
  - Selection Input
  - Date Input
  - Enter-To-Continue Input
  - Built-in validation rules for text, numbers and dates, or your own
- Easy text formatting
  - Foreground and background colors, bold and underline
  - Automatic plain-text fallback for terminals without ANSI support
  - Flex Layouts (Centering, Justifying, Spacing)
  - Tables with alignment, column growth and several border styles
  - Spinners and Progress Bars with built-in styles
- Fast console output
  - One shared console for the whole program
  - Batched output for printing large amounts of text quickly
  - Safe to print from multiple threads
- Command Line Parsing
  - Subcommands
  - Typed Options and Arguments (string, integer, double, float, boolean)
  - Clear errors for unknown options, missing arguments and invalid values
- Customizable
  - Custom Input Prompts
  - Custom Input Validators
  - Custom Renderers and Table Styles

## Installation

Make sure to replace `0.0.0` with the version you want to use. (You can find the latest version [here](https://maven.strassburger.org/browse/org/strassburger/tui4j))

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

- [Formatting and Inputs](#formatting-and-inputs)
- [Commands](#commands)
- [Table](#table)
- [Printing Large Output](#printing-large-output)

All examples use `TerminalConsole.system()`, the console for stdin and stdout. Call it once and pass the console around instead of creating new consoles: several consoles on the same terminal can take each other's input and print out of order.

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
  public static void main(String[] args) throws InterruptedException {
    Console console = TerminalConsole.system();

    console.println(" ");
    console.println(new FlexText(StyledText.text("Welcome to the TUI4J example application!").fg(AnsiColor.BLUE).bold()));
    console.println(" ");

    String name = console.read(TextInput.withPrompt("What is your name? ")
            .inline(true));

    String gender = console.read(SelectInput.<String>withPrompt("Select your gender: ")
            .option("Male", "Male")
            .option("Female", "Female")
            .option("Other", "Other")
            .optionsStyle(StyledText.text(" (%num%) ").fg(AnsiColor.CYAN).append("%label%")));

    String email = console.read(TextInput.withPrompt("What is your email address?")
            .validationRule(
                    new ValidationRule<String>() {
                      @Override
                      public boolean validate(String s) {
                        return s.contains("@") && s.contains(".");
                      }

                      @Override
                      public StyledText errorMessage() {
                        return StyledText.text("Email address must contain '@' and '.'").fg(AnsiColor.RED);
                      }
                    }
            ));

    String about = console.read(MultilineTextInput.withPrompt("Tell me about yourself: ")
            .validationRule(TextValidationRules.minLength(30)));

    int age = console.read(IntegerInput.withPrompt("How old are you? ")
            .validationRule(NumberValidationRules.greaterThan(0))
            .validationRule(NumberValidationRules.lessThan(150)));

    double height = console.read(DoubleInput.withPrompt("How tall are you? ")
            .validationRule(NumberValidationRules.greaterThan(0.0))
            .validationRule(NumberValidationRules.lessThan(3.0)));

    console.println(" ");
    console.println(StyledText.text("Your Inputs:").fg(AnsiColor.BRIGHT_WHITE).bold());
    console.println(new FlexText()
            .part(StyledText.text("Name ").fg(AnsiColor.BRIGHT_WHITE))
            .part(StyledText.text(" " + name).fg(AnsiColor.BRIGHT_WHITE))
            .justify(FlexJustify.SPACE_BETWEEN)
            .separatorChar(StyledText.text(".").fg(AnsiColor.BRIGHT_BLACK))
    );
    console.println(FlexText.keyValue("Email ", " " + email));
    console.println(FlexText.keyValue("Age ", " " + age));
    console.println(FlexText.keyValue("Height (m) ", " " + height));
    console.println(FlexText.keyValue("Gender ", " " + gender));
    console.println(" ");

    boolean shouldContinue = console.read(BooleanInput.withPrompt("Do you want to continue? (y/n)"));

    if (shouldContinue) {
      console.println("Continuing...");

      Spinner spinner = new Spinner()
              .message(StyledText.text("Loading some stuff..."))
              .spinnerColor(AnsiColor.CYAN)
              .spinnerFrames(SpinnerFrames.SYNTHWAVE)
              .speedMs(100);
      console.print(spinner);

      for (int i = 0; i < 50; i++) {
        Thread.sleep(100);
        spinner.message(StyledText.text("Loading some stuff... " + (i + 1) * 2 + "%"));
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
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.console.TerminalConsole;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

public class Main {
    public static void main(String[] args) {
        Console console = TerminalConsole.system();

        String[] testArgs = "farewell franky".split(" ");
        console.println("Executing with args: " + String.join(" ", testArgs));
    
        Option<String> nameOption = Option.str("name", "n", "World");
        Argument<String> nameArgument = Argument.str("name", true);
    
        Command command = Command.root()
                .subCommand(
                        Command.named("greet")
                                .option(nameOption)
                                .handler(context -> {
                                  String name = context.optionValue(nameOption);
                                  console.println("Hello, " + name + "!");
                                }),
                        Command.named("farewell")
                                .argument(nameArgument)
                                .handler(context -> {
                                  String name = context.argumentValue(nameArgument);
                                  console.println("Goodbye, " + name + "!");
                                })
                );
    
        try {
            command.execute(testArgs);
        } catch (CommandException e) {
            console.println(StyledText.text("Error: " + e.getMessage()).fg(AnsiColor.RED));
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
    Printer printer = TerminalConsole.system();

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
            .style(TableStyles.UNICODE_BOX.withBorderColor(AnsiColor.BRIGHT_BLACK))
            .hasHeader(true)
            .columnGrow(0, 1)
            .columnAlignment(0, ColumnAlign.START)
            .columnAlignment(1, ColumnAlign.CENTER)
            .columnAlignment(2, ColumnAlign.END)
            .row(headers)
            .row(row1)
            .row(row2)
            .row(row3);

    printer.println(table);
  }
}
```

### Printing Large Output

By default, the console flushes after every print call so output appears immediately. When printing a lot at once, wrap it in a batch: the output is buffered and flushed once at the end, which is much faster. Tables and other layouts are batched automatically.

```java
import org.strassburger.tui4j.console.Batch;
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.console.TerminalConsole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) throws IOException {
    Console console = TerminalConsole.system();

    // Lambda form
    console.batch(() -> {
      for (int i = 0; i < 100_000; i++) {
        console.println("Line " + i);
      }
    });

    // try-with-resources form, for code that throws checked exceptions or returns early
    try (Batch ignored = console.buffered()) {
      for (String line : Files.readAllLines(Path.of("data.csv"))) {
        console.println(line);
      }
    }
  }
}
```

Batches can be nested, and the console is flushed even if the code inside throws. A prompt printed inside a batch is still shown, because reading input always flushes first.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](https://github.com/KartoffelChipss/TUI4J/blob/main/LICENSE) file for details.
