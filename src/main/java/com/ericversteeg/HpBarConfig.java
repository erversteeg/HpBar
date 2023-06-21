package com.ericversteeg;

import com.ericversteeg.config.BarType;
import com.ericversteeg.view.RSAnchorType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(HpBarConfig.GROUP)
public interface HpBarConfig extends Config
{
    String GROUP = "hpbar";

    @ConfigItem(
            position = 0,
            keyName = "primaryBar",
            name = "Primary",
            description = "Configures what the primary bar is."
    )
    default BarType primaryBarType() { return BarType.HITPOINTS; }

    @ConfigItem(
            position = 1,
            keyName = "secondaryBar",
            name = "Secondary",
            description = "Configures what the secondary bar is."
    )
    default BarType secondaryBarType() { return BarType.PRAYER; }

    @ConfigItem(
            position = 2,
            keyName = "tertiaryBar",
            name = "Tertiary",
            description = "Configures what the tertiary bar is."
    )
    default BarType tertiaryBarType() { return BarType.SPECIAL_ATTACK; }

    @ConfigItem(
            position = 3,
            keyName = "quaternaryBar",
            name = "Quaternary",
            description = "Configures what the quaternary bar is."
    )
    default BarType quaternaryBarType() { return BarType.RUN_ENERGY; }

    @ConfigItem(
            position = 4,
            keyName = "enableHp",
            name = "Enable HP Bar",
            description = "Configures whether to enable the hp bar."
    )
    default boolean showHpBar() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "alwaysVisible",
            name = "Always On",
            description = "Configures whether or not hp bars are always on."
    )
    default boolean isAlwaysVisible() { return true; }

    @ConfigItem(
            position = 6,
            keyName = "largeSecondary",
            name = "Large Secondary",
            description = "Configures whether or not the secondary bar is large."
    )
    default boolean isLargeSecondary() { return false; }

    @ConfigItem(
            position = 7,
            keyName = "primaryText",
            name = "Primary Text",
            description = "Configures whether or not to show primary text."
    )
    default boolean hasPrimaryText() { return true; }

    @ConfigItem(
            position = 8,
            keyName = "smallText",
            name = "Small Text",
            description = "Configures whether or not to show small text."
    )
    default boolean hasSecondaryText() { return true; }

    @ConfigItem(
            position = 9,
            keyName = "anchorType",
            name = "Bar Anchor",
            description = "Configures view anchor."
    )
    default RSAnchorType anchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 10,
            keyName = "anchorX",
            name = "Bar Offset X",
            description = "Configures x offset."
    )
    default int anchorX() {
        return 0;
    }

    @ConfigItem(
            position = 11,
            keyName = "anchorY",
            name = "Bar Offset Y",
            description = "Configures y offset."
    )
    default int anchorY() {
        return 80;
    }

    @ConfigItem(
            position = 12,
            keyName = "width",
            name = "Bar Width",
            description = "Configures the width."
    )
    default int width() {
        return 200;
    }

    @ConfigItem(
            position = 13,
            keyName = "height",
            name = "Bar Height",
            description = "Configures the height."
    )
    default int height() {
        return 30;
    }

    @ConfigItem(
            position = 14,
            keyName = "enableRun",
            name = "Enable Run Bar",
            description = "Configures whether or not to enable the run bar."
    )
    default boolean showRunBar() { return true; }

    @ConfigItem(
            position = 15,
            keyName = "runAlwaysVisible",
            name = "Run Bar Always On",
            description = "Configures whether or not bars are always on."
    )
    default boolean isRunAlwaysVisible() { return true; }

    @ConfigItem(
            position = 16,
            keyName = "runAnchorType",
            name = "Run Bar Anchor",
            description = "Configures run bar anchor."
    )
    default RSAnchorType runAnchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 17,
            keyName = "runAnchorX",
            name = "Run Offset X",
            description = "Configure run bar x offset."
    )
    default int runAnchorX() {
        return 0;
    }

    @ConfigItem(
            position = 18,
            keyName = "runAnchorY",
            name = "Run Offset Y",
            description = "Configures run bar y offset."
    )
    default int runAnchorY() {
        return 160;
    }

    @ConfigItem(
            position = 19,
            keyName = "runWidth",
            name = "Run Width",
            description = "Configures run bar width."
    )
    default int runWidth() {
        return 160;
    }

    @ConfigItem(
            position = 20,
            keyName = "runHeight",
            name = "Run Height",
            description = "Configures run bar height."
    )
    default int runHeight() {
        return 25;
    }
}