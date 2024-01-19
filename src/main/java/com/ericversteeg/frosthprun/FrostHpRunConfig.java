package com.ericversteeg.frosthprun;

import com.ericversteeg.frosthprun.config.BarType;
import com.ericversteeg.frosthprun.view.RSAnchorType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup(FrostHpRunConfig.GROUP)
public interface FrostHpRunConfig extends Config
{
    String GROUP = "hpbar";

    @ConfigSection(
            position = 0,
            name = "Hp Bar",
            description = "Hp and associated bars."
    )
    String hpBarSection = "hpBar";

    @ConfigItem(
            position = 0,
            keyName = "primaryBar",
            name = "Primary",
            description = "Configures what the primary bar is.",
            section = hpBarSection
    )
    default BarType primaryBarType() { return BarType.HITPOINTS; }

    @ConfigItem(
            position = 1,
            keyName = "secondaryBar",
            name = "Secondary",
            description = "Configures what the secondary bar is.",
            section = hpBarSection
    )
    default BarType secondaryBarType() { return BarType.PRAYER; }

    @ConfigItem(
            position = 2,
            keyName = "tertiaryBar",
            name = "Tertiary",
            description = "Configures what the tertiary bar is.",
            section = hpBarSection
    )
    default BarType tertiaryBarType() { return BarType.SPECIAL_ATTACK; }

    @ConfigItem(
            position = 3,
            keyName = "quaternaryBar",
            name = "Quaternary",
            description = "Configures what the quaternary bar is.",
            section = hpBarSection
    )
    default BarType quaternaryBarType() { return BarType.RUN_ENERGY; }

    @ConfigItem(
            position = 13,
            keyName = "enableHp",
            name = "Enable",
            description = "Configures whether to enable.",
            section = hpBarSection
    )
    default boolean showHpBar() { return true; }

    @ConfigItem(
            position = 14,
            keyName = "alwaysVisible",
            name = "Always On",
            description = "Configures whether or not bars are always on.",
            section = hpBarSection
    )
    default boolean isAlwaysVisible() { return true; }

    @ConfigItem(
            position = 15,
            keyName = "combatDelay",
            name = "Combat Hide Delay",
            description = "Configures how many ticks after combat the bar hides.",
            section = hpBarSection
    )
    default int combatHideDelay() { return 5; }

    @ConfigItem(
            position = 16,
            keyName = "barOpacity",
            name = "Opacity",
            description = "Configures the opacity of the bar (20-100).",
            section = hpBarSection
    )
    default int barOpacity() { return 100; }

    @ConfigItem(
            position = 17,
            keyName = "extendBars",
            name = "Extend Bars",
            description = "Configures whether or not bars extend beyond full size.",
            section = additionalConfigSection
    )
    default boolean extendBars() {
        return false;
    }

    @ConfigItem(
            position = 18,
            keyName = "largeSecondary",
            name = "Large Secondary",
            description = "Configures whether or not the secondary bar is large.",
            section = hpBarSection
    )
    default boolean isLargeSecondary() { return false; }

    @ConfigItem(
            position = 19,
            keyName = "primaryText",
            name = "Text",
            description = "Configures whether or not to show text.",
            section = hpBarSection
    )
    default boolean hasPrimaryText() { return true; }

    @ConfigItem(
            position = 20,
            keyName = "smallText",
            name = "Small Text",
            description = "Configures whether or not to show small text.",
            section = hpBarSection
    )
    default boolean hasSecondaryText() { return true; }

    @ConfigItem(
            position = 21,
            keyName = "anchorType",
            name = "Anchor",
            description = "Configures view anchor.",
            section = hpBarSection
    )
    default RSAnchorType anchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 22,
            keyName = "anchorX",
            name = "Offset X",
            description = "Configures x offset.",
            section = hpBarSection
    )
    default int anchorX() {
        return 0;
    }

    @ConfigItem(
            position = 23,
            keyName = "anchorY",
            name = "Offset Y",
            description = "Configures y offset.",
            section = hpBarSection
    )
    default int anchorY() {
        return 100;
    }

    @ConfigItem(
            position = 24,
            keyName = "width",
            name = "Width",
            description = "Configures the width.",
            section = hpBarSection
    )
    default int width() {
        return 218;
    }

    @ConfigItem(
            position = 25,
            keyName = "height",
            name = "Height",
            description = "Configures the height.",
            section = hpBarSection
    )
    default int height() {
        return 25;
    }

    @ConfigSection(
            position = 1,
            name = "Run Bar",
            description = "Run bar."
    )
    String runBarSection = "runBar";

    @ConfigItem(
            position = 14,
            keyName = "enableRun",
            name = "Enable",
            description = "Configures whether or not to enable.",
            section = runBarSection
    )
    default boolean showRunBar() { return true; }

    @ConfigItem(
            position = 15,
            keyName = "runAlwaysVisible",
            name = "Always On",
            description = "Configures whether or not bar is always on.",
            section = runBarSection
    )
    default boolean isRunAlwaysVisible() { return true; }

    @ConfigItem(
            position = 15,
            keyName = "runBarOpacity",
            name = "Opacity",
            description = "Configures the opacity of the bar (20-100).",
            section = runBarSection
    )
    default int runBarOpacity() { return 100; }

    @ConfigItem(
            position = 15,
            keyName = "runPrimaryText",
            name = "Text",
            description = "Configures whether or not to show text.",
            section = runBarSection
    )
    default boolean hasRunPrimaryText() { return true; }

    @ConfigItem(
            position = 16,
            keyName = "runTextOffsetX",
            name = "Text Offset X",
            description = "Configures the run bar text x offset.",
            section = runBarSection
    )
    default int runBarTextOffsetX() { return 0; }

    @ConfigItem(
            position = 17,
            keyName = "negativeRunTextOffsetX",
            name = "Negative Text Offset X",
            description = "Configures the run bar text x offset is negative.",
            section = runBarSection
    )
    default boolean isNegativeRunTextOffsetX() { return false; }

    @ConfigItem(
            position = 18,
            keyName = "runTextOffsetY",
            name = "Text Offset Y",
            description = "Configures the run bar text y offset.",
            section = runBarSection
    )
    default int runBarTextOffsetY() { return 0; }

    @ConfigItem(
            position = 19,
            keyName = "negativeRunTextOffsetY",
            name = "Negative Text Offset Y",
            description = "Configures the run bar text y offset is negative.",
            section = runBarSection
    )
    default boolean isNegativeRunTextOffsetY() { return false; }

    @ConfigItem(
            position = 20,
            keyName = "runAnchorType",
            name = "Anchor",
            description = "Configures view anchor.",
            section = runBarSection
    )
    default RSAnchorType runAnchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 21,
            keyName = "runAnchorX",
            name = "Offset X",
            description = "Configure x offset.",
            section = runBarSection
    )
    default int runAnchorX() {
        return 0;
    }

    @ConfigItem(
            position = 22,
            keyName = "runAnchorY",
            name = "Offset Y",
            description = "Configures y offset.",
            section = runBarSection
    )
    default int runAnchorY() {
        return 180;
    }

    @ConfigItem(
            position = 23,
            keyName = "runWidth",
            name = "Width",
            description = "Configures width.",
            section = runBarSection
    )
    default int runWidth() {
        return 218;
    }

    @ConfigItem(
            position = 24,
            keyName = "runHeight",
            name = "Height",
            description = "Configures height.",
            section = runBarSection
    )
    default int runHeight() {
        return 25;
    }

    @ConfigSection(
            position = 2,
            name = "Hues",
            description = "Bar colors."
    )
    String huesSection = "huesSection";

    @ConfigItem(
            position = 4,
            keyName = "hitpointsHue",
            name = "Hitpoints Hue",
            description = "Configures what hue hitpoints is (0-360).",
            section = huesSection
    )
    default int hpHue() { return 0; }

    @ConfigItem(
            position = 5,
            keyName = "diseasedHue",
            name = "Diseased Hue",
            description = "Configures what hue diseased is (0-360).",
            section = huesSection
    )
    default int diseasedHue() { return 75; }

    @ConfigItem(
            position = 6,
            keyName = "poisonedHue",
            name = "Poisoned Hue",
            description = "Configures what hue poisoned is (0-360).",
            section = huesSection
    )
    default int poisonedHue() { return 95; }

    @ConfigItem(
            position = 7,
            keyName = "envenomedHue",
            name = "Envenomed Hue",
            description = "Configures what hue envenomed is (0-360).",
            section = huesSection
    )
    default int envenomedHue() { return 145; }

    @ConfigItem(
            position = 8,
            keyName = "activePrayerHue",
            name = "Active Prayer Hue",
            description = "Configures what hue prayer is when active (0-360).",
            section = huesSection
    )
    default int activePrayerHue() { return 270; }

    @ConfigItem(
            position = 9,
            keyName = "inactivePrayerHue",
            name = "Inactive Prayer Hue",
            description = "Configures what hue prayer is when inactive (0-360).",
            section = huesSection
    )
    default int inactivePrayerHue() { return 259; }

    @ConfigItem(
            position = 10,
            keyName = "specialAttackHue",
            name = "Special Attack Hue",
            description = "Configures what hue special attack is when inactive (0-360).",
            section = huesSection
    )
    default int specialAttackHue() { return 186; }

    @ConfigItem(
            position = 11,
            keyName = "runHue",
            name = "Run Hue",
            description = "Configures what hue run is (0-360).",
            section = huesSection
    )
    default int runHue() { return 50; }

    @ConfigItem(
            position = 12,
            keyName = "staminaHue",
            name = "Stamina Hue",
            description = "Configures what hue run is with stamina potion (0-360).",
            section = huesSection
    )
    default int staminaHue() { return 25; }

    @ConfigSection(
            position = 3,
            name = "Text Colors",
            description = "Text colors."
    )
    String textColorsSection = "textColorsSection";

    @ConfigItem(
            position = 0,
            keyName = "smallTextColor",
            name = "Small Text Color",
            description = "Configures thin bar text color.",
            section = textColorsSection
    )
    default TextColor smallTextColor() {
        return TextColor.WHITE;
    }

    @ConfigItem(
            position = 1,
            keyName = "secondaryTextColor",
            name = "Secondary Text Color",
            description = "Configures secondary bar text color.",
            section = textColorsSection
    )
    default TextColor secondaryTextColor() {
        return TextColor.WHITE;
    }

    @ConfigItem(
            position = 2,
            keyName = "primaryTextColor",
            name = "Primary Text Color",
            description = "Configures primary bar text color.",
            section = textColorsSection
    )
    default TextColor primaryTextColor() {
        return TextColor.WHITE;
    }

    @ConfigItem(
            position = 3,
            keyName = "runTextColor",
            name = "Run Text Color",
            description = "Configures run bar text color.",
            section = textColorsSection
    )
    default TextColor runTextColor() {
        return TextColor.WHITE;
    }

    @ConfigSection(
            position = 4,
            name = "Additional Configuration",
            description = ""
    )
    String additionalConfigSection = "additionalConfig";

    @ConfigItem(
            position = 21,
            keyName = "smallTextOffsetX",
            name = "Small Text Offset X",
            description = "Configures text offset x for thin bars.",
            section = additionalConfigSection
    )
    default int smallTextOffsetX() {
        return 0;
    }

    @ConfigItem(
            position = 22,
            keyName = "negativeSmallTextOffsetX",
            name = "Negative Small Text Offset X",
            description = "Configures whether or not the small text offset x number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativeSmallTextOffsetX() {
        return false;
    }

    @ConfigItem(
            position = 23,
            keyName = "smallTextOffsetY",
            name = "Small Text Offset Y",
            description = "Configures text offset y for thin bars.",
            section = additionalConfigSection
    )
    default int smallTextOffsetY() {
        return 0;
    }

    @ConfigItem(
            position = 24,
            keyName = "negativeSmallTextOffsetY",
            name = "Negative Small Text Offset Y",
            description = "Configures whether or not the small text offset y number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativeSmallTextOffsetY() {
        return false;
    }

    @ConfigItem(
            position = 25,
            keyName = "secondaryTextOffsetX",
            name = "Secondary Text Offset X",
            description = "Configures text offset x for secondary bar.",
            section = additionalConfigSection
    )
    default int secondaryTextOffsetX() {
        return 0;
    }

    @ConfigItem(
            position = 26,
            keyName = "negativeSecondaryTextOffsetX",
            name = "Negative Secondary Text Offset X",
            description = "Configures whether or not the secondary text offset x number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativeSecondaryTextOffsetX() {
        return false;
    }

    @ConfigItem(
            position = 27,
            keyName = "secondaryTextOffsetY",
            name = "Secondary Text Offset Y",
            description = "Configures text offset y for secondary bars.",
            section = additionalConfigSection
    )
    default int secondaryTextOffsetY() {
        return 0;
    }

    @ConfigItem(
            position = 28,
            keyName = "negativeSecondaryTextOffsetY",
            name = "Negative Secondary Text Offset Y",
            description = "Configures whether or not the secondary text offset y number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativeSecondaryTextOffsetY() {
        return false;
    }

    @ConfigItem(
            position = 29,
            keyName = "primaryTextOffsetX",
            name = "Primary Text Offset X",
            description = "Configures text offset x for primary bar.",
            section = additionalConfigSection
    )
    default int primaryTextOffsetX() {
        return 0;
    }

    @ConfigItem(
            position = 30,
            keyName = "negativePrimaryTextOffsetX",
            name = "Negative Primary Text Offset X",
            description = "Configures whether or not the primary text offset x number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativePrimaryTextOffsetX() {
        return false;
    }

    @ConfigItem(
            position = 31,
            keyName = "primaryTextOffsetY",
            name = "Primary Text Offset Y",
            description = "Configures text offset y for primary bars.",
            section = additionalConfigSection
    )
    default int primaryTextOffsetY() {
        return 0;
    }

    @ConfigItem(
            position = 32,
            keyName = "negativePrimaryTextOffsetY",
            name = "Negative Primary Text Offset Y",
            description = "Configures whether or not the primary text offset y number is negative.",
            section = additionalConfigSection
    )
    default boolean isNegativePrimaryTextOffsetY() {
        return false;
    }

    @ConfigItem(
            position = 33,
            keyName = "smallFontScaleFactor",
            name = "Small Font Scale Factor %",
            description = "Configures small font scale factor.",
            section = additionalConfigSection
    )
    default int smallFontScaleFactor() {
        return 100;
    }

    @ConfigItem(
            position = 34,
            keyName = "secondaryFontScaleFactor",
            name = "Secondary Font Scale Factor %",
            description = "Configures secondary font scale factor.",
            section = additionalConfigSection
    )
    default int secondaryFontScaleFactor() {
        return 100;
    }

    @ConfigItem(
            position = 35,
            keyName = "primaryFontScaleFactor",
            name = "Primary Font Scale Factor %",
            description = "Configures primary font scale factor.",
            section = additionalConfigSection
    )
    default int primaryFontScaleFactor() {
        return 100;
    }

    @ConfigItem(
            position = 36,
            keyName = "smallFontAlignment",
            name = "Small Font Alignment",
            description = "Configures small font alignment.",
            section = additionalConfigSection
    )
    default TextAlignment smallFontAlignment() {
        return TextAlignment.LEFT;
    }

    @ConfigItem(
            position = 37,
            keyName = "secondaryFontAlignment",
            name = "Secondary Font Alignment",
            description = "Configures secondary font alignment.",
            section = additionalConfigSection
    )
    default TextAlignment secondaryFontAlignment() {
        return TextAlignment.LEFT;
    }

    @ConfigItem(
            position = 38,
            keyName = "primaryFontAlignment",
            name = "Primary Font Alignment",
            description = "Configures primary font alignment.",
            section = additionalConfigSection
    )
    default TextAlignment primaryFontAlignment() {
        return TextAlignment.CENTER;
    }

    @ConfigItem(
            position = 39,
            keyName = "smallFontWeight",
            name = "Small Font Weight",
            description = "Configures small font weight.",
            section = additionalConfigSection
    )
    default FontWeight smallFontWeight() {
        return FontWeight.BOLD;
    }

    @ConfigItem(
            position = 40,
            keyName = "secondaryFontWeight",
            name = "Secondary Font Weight",
            description = "Configures secondary font weight.",
            section = additionalConfigSection
    )
    default FontWeight secondaryFontWeight() {
        return FontWeight.BOLD;
    }

    @ConfigItem(
            position = 41,
            keyName = "primaryFontWeight",
            name = "Primary Font Weight",
            description = "Configures primary font weight.",
            section = additionalConfigSection
    )
    default FontWeight primaryFontWeight() {
        return FontWeight.BLACK;
    }
}