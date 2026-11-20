package org.strassburger.tui4j.formatting.layout.spinner;

import org.strassburger.tui4j.formatting.StyledText;

import java.util.List;

/**
 * Predefined spinner frames for use with the {@link Spinner} class.
 */
public final class SpinnerFrames {
    private SpinnerFrames() {}

    public static final List<StyledText> DEFAULT = List.of(
            StyledText.text("|"),
            StyledText.text("/"),
            StyledText.text("-"),
            StyledText.text("\\")
    );
    public static final List<StyledText> DOTS = List.of(
            StyledText.text("⠋"),
            StyledText.text("⠙"),
            StyledText.text("⠹"),
            StyledText.text("⠸"),
            StyledText.text("⠼"),
            StyledText.text("⠴"),
            StyledText.text("⠦"),
            StyledText.text("⠧"),
            StyledText.text("⠇"),
            StyledText.text("⠏")
    );
    public static final List<StyledText> ARROWS = List.of(
            StyledText.text("←"),
            StyledText.text("↖"),
            StyledText.text("↑"),
            StyledText.text("↗"),
            StyledText.text("→"),
            StyledText.text("↘"),
            StyledText.text("↓"),
            StyledText.text("↙")
    );
    public static final List<StyledText> BOUNCING_BALL = List.of(
            StyledText.text("⠁"),
            StyledText.text("⠂"),
            StyledText.text("⠄"),
            StyledText.text("⠂")
    );
    public static final List<StyledText> GROWING_DOTS = List.of(
            StyledText.text(".    "),
            StyledText.text("..   "),
            StyledText.text("...  "),
            StyledText.text(".... "),
            StyledText.text("....."),
            StyledText.text(" ...."),
            StyledText.text("  ..."),
            StyledText.text("   .."),
            StyledText.text("    .")
    );
    public static final List<StyledText> CLOCK = List.of(
            StyledText.text("🕐"),
            StyledText.text("🕑"),
            StyledText.text("🕒"),
            StyledText.text("🕓"),
            StyledText.text("🕔"),
            StyledText.text("🕕"),
            StyledText.text("🕖"),
            StyledText.text("🕗"),
            StyledText.text("🕘"),
            StyledText.text("🕙"),
            StyledText.text("🕚"),
            StyledText.text("🕛")
    );
    public static final List<StyledText> MOON_PHASES = List.of(
            StyledText.text("🌑"),
            StyledText.text("🌒"),
            StyledText.text("🌓"),
            StyledText.text("🌔"),
            StyledText.text("🌕"),
            StyledText.text("🌖"),
            StyledText.text("🌗"),
            StyledText.text("🌘")
    );
    public static final List<StyledText> BLOCKS = List.of(
            StyledText.text("▁"),
            StyledText.text("▂"),
            StyledText.text("▃"),
            StyledText.text("▄"),
            StyledText.text("▅"),
            StyledText.text("▆"),
            StyledText.text("▇"),
            StyledText.text("█"),
            StyledText.text("▇"),
            StyledText.text("▆"),
            StyledText.text("▅"),
            StyledText.text("▄"),
            StyledText.text("▃"),
            StyledText.text("▁")
    );
    public static final List<StyledText> SYNTHWAVE = List.of(
            StyledText.text("▁▂▃"),
            StyledText.text("▂▃▄"),
            StyledText.text("▃▄▅"),
            StyledText.text("▄▅▆"),
            StyledText.text("▅▆▇"),
            StyledText.text("▆▇█"),
            StyledText.text("▇█▆"),
            StyledText.text("█▆▅"),
            StyledText.text("▆▅▄"),
            StyledText.text("▅▄▃"),
            StyledText.text("▄▃▂"),
            StyledText.text("▃▂▁")
    );
    public static final List<StyledText> SMOOTH_BLOCKS = List.of(
            StyledText.text("▉"),
            StyledText.text("▊"),
            StyledText.text("▋"),
            StyledText.text("▌"),
            StyledText.text("▍"),
            StyledText.text("▎"),
            StyledText.text("▏"),
            StyledText.text("▎"),
            StyledText.text("▍"),
            StyledText.text("▌"),
            StyledText.text("▋"),
            StyledText.text("▊"),
            StyledText.text("▉")
    );
    public static final List<StyledText> THROB = List.of(
            StyledText.text("●○○○○"),
            StyledText.text("○●○○○"),
            StyledText.text("○○●○○"),
            StyledText.text("○○○●○"),
            StyledText.text("○○○○●"),
            StyledText.text("○○○●○"),
            StyledText.text("○○●○○"),
            StyledText.text("○●○○○")
    );
    public static final List<StyledText> JUMPING_BLOCK = List.of(
            StyledText.text("▖"),
            StyledText.text("▘"),
            StyledText.text("▝"),
            StyledText.text("▗")
    );
    public static final List<StyledText> TRIANGLE = List.of(
            StyledText.text("◢"),
            StyledText.text("◣"),
            StyledText.text("◤"),
            StyledText.text("◥")
    );
    public static final List<StyledText> BALL = List.of(
            StyledText.text("◐"),
            StyledText.text("◓"),
            StyledText.text("◑"),
            StyledText.text("◒")
    );
    public static final List<StyledText> EYES = List.of(
            StyledText.text("◡◡"),
            StyledText.text("⊙⊙"),
            StyledText.text("◠◠")
    );
}
