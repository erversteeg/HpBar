package com.ericversteeg;

import com.ericversteeg.config.BarType;
import com.ericversteeg.view.RSAnchorType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(HpBarConfig.GROUP)
public interface HpBarConfig extends Config
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
            position = 4,
            keyName = "enableHp",
            name = "Enable",
            description = "Configures whether to enable.",
            section = hpBarSection
    )
    default boolean showHpBar() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "alwaysVisible",
            name = "Always On",
            description = "Configures whether or not bars are always on.",
            section = hpBarSection
    )
    default boolean isAlwaysVisible() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "barOpacity",
            name = "Opacity",
            description = "Configures the opacity of the bar (20-100).",
            section = hpBarSection
    )
    default int barOpacity() { return 100; }

    @ConfigItem(
            position = 6,
            keyName = "largeSecondary",
            name = "Large Secondary",
            description = "Configures whether or not the secondary bar is large.",
            section = hpBarSection
    )
    default boolean isLargeSecondary() { return false; }

    @ConfigItem(
            position = 7,
            keyName = "primaryText",
            name = "Text",
            description = "Configures whether or not to show text.",
            section = hpBarSection
    )
    default boolean hasPrimaryText() { return true; }

    @ConfigItem(
            position = 8,
            keyName = "smallText",
            name = "Small Text",
            description = "Configures whether or not to show small text.",
            section = hpBarSection
    )
    default boolean hasSecondaryText() { return true; }

    @ConfigItem(
            position = 9,
            keyName = "anchorType",
            name = "Anchor",
            description = "Configures view anchor.",
            section = hpBarSection
    )
    default RSAnchorType anchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 10,
            keyName = "anchorX",
            name = "Offset X",
            description = "Configures x offset.",
            section = hpBarSection
    )
    default int anchorX() {
        return 0;
    }

    @ConfigItem(
            position = 11,
            keyName = "anchorY",
            name = "Offset Y",
            description = "Configures y offset.",
            section = hpBarSection
    )
    default int anchorY() {
        return 80;
    }

    @ConfigItem(
            position = 12,
            keyName = "width",
            name = "Width",
            description = "Configures the width.",
            section = hpBarSection
    )
    default int width() {
        return 200;
    }

    @ConfigItem(
            position = 13,
            keyName = "height",
            name = "Height",
            description = "Configures the height.",
            section = hpBarSection
    )
    default int height() {
        return 30;
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
            keyName = "runAnchorType",
            name = "Anchor",
            description = "Configures view anchor.",
            section = runBarSection
    )
    default RSAnchorType runAnchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 17,
            keyName = "runAnchorX",
            name = "Offset X",
            description = "Configure x offset.",
            section = runBarSection
    )
    default int runAnchorX() {
        return 0;
    }

    @ConfigItem(
            position = 18,
            keyName = "runAnchorY",
            name = "Offset Y",
            description = "Configures y offset.",
            section = runBarSection
    )
    default int runAnchorY() {
        return 160;
    }

    @ConfigItem(
            position = 19,
            keyName = "runWidth",
            name = "Width",
            description = "Configures width.",
            section = runBarSection
    )
    default int runWidth() {
        return 160;
    }

    @ConfigItem(
            position = 20,
            keyName = "runHeight",
            name = "Height",
            description = "Configures height.",
            section = runBarSection
    )
    default int runHeight() {
        return 25;
    }
}