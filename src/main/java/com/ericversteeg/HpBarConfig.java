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
            keyName = "anchorType",
            name = "Anchor",
            description = "Configures anchor."
    )
    default RSAnchorType anchorType() {
        return RSAnchorType.TOP_CENTER;
    }

    @ConfigItem(
            position = 1,
            keyName = "anchorX",
            name = "X",
            description = "Configure x position."
    )
    default int anchorX() {
        return 0;
    }

    @ConfigItem(
            position = 2,
            keyName = "anchorY",
            name = "Y",
            description = "Configures y position."
    )
    default int anchorY() {
        return 0;
    }

    @ConfigItem(
            position = 3,
            keyName = "width",
            name = "Width",
            description = "Configures the width."
    )
    default int width() {
        return 200;
    }

    @ConfigItem(
            position = 3,
            keyName = "height",
            name = "Height",
            description = "Configures the height."
    )
    default int height() {
        return 30;
    }

    @ConfigItem(
            position = 4,
            keyName = "primaryBar",
            name = "Primary",
            description = "Configures what the primary bar is."
    )
    default BarType primaryBarType() { return BarType.HITPOINTS; }

    @ConfigItem(
            position = 5,
            keyName = "secondaryBar",
            name = "Secondary",
            description = "Configures what the secondary bar is."
    )
    default BarType secondaryBarType() { return BarType.PRAYER; }

    @ConfigItem(
            position = 6,
            keyName = "tertiaryBar",
            name = "Tertiary",
            description = "Configures what the tertiary bar is."
    )
    default BarType tertiaryBarType() { return BarType.SPECIAL_ATTACK; }

    @ConfigItem(
            position = 7,
            keyName = "quaternaryBar",
            name = "Quaternary",
            description = "Configures what the quaternary bar is."
    )
    default BarType quaternaryBarType() { return BarType.RUN_ENERGY; }

    @ConfigItem(
            position = 8,
            keyName = "secondaryText",
            name = "Small Text",
            description = "Configures whether or not to show small text."
    )
    default boolean hasSecondaryText() { return true; }

    @ConfigItem(
            position = 9,
            keyName = "alwaysVisible",
            name = "Always Visible",
            description = "Configures whether or not bars are always visible."
    )
    default boolean isAlwaysVisible() { return true; }

    @ConfigItem(
            position = 9,
            keyName = "showRun",
            name = "Show Run Bar",
            description = "Configures whether or not to show run bar when running."
    )
    default boolean isShowRunBar() { return true; }
}