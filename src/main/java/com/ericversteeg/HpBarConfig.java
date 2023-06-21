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
            keyName = "largeSecondary",
            name = "Larger Secondary",
            description = "Configures whether or not the secondary bar is larger."
    )
    default boolean isLargeSecondary() { return false; }

    @ConfigItem(
            position = 4,
            keyName = "secondaryText",
            name = "Secondary Text",
            description = "Configures whether or not to show secondary text."
    )
    default boolean hasSecondaryText() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "alwaysVisible",
            name = "Always On",
            description = "Configures whether or not bars are always on."
    )
    default boolean isAlwaysVisible() { return true; }

    @ConfigItem(
            position = 6,
            keyName = "showRun",
            name = "Show Run Bar Separately",
            description = "Configures whether or not to show run bar separately when running."
    )
    default boolean showRunBar() { return true; }

    @ConfigItem(
            position = 7,
            keyName = "anchorType",
            name = "Anchor",
            description = "Configures anchor."
    )
    default RSAnchorType anchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 8,
            keyName = "anchorX",
            name = "Offset X",
            description = "Configure x offset."
    )
    default int anchorX() {
        return 0;
    }

    @ConfigItem(
            position = 9,
            keyName = "anchorY",
            name = "Offset Y",
            description = "Configures y offset."
    )
    default int anchorY() {
        return 0;
    }

    @ConfigItem(
            position = 10,
            keyName = "width",
            name = "Width",
            description = "Configures the width."
    )
    default int width() {
        return 200;
    }

    @ConfigItem(
            position = 11,
            keyName = "height",
            name = "Height",
            description = "Configures the height."
    )
    default int height() {
        return 30;
    }
}