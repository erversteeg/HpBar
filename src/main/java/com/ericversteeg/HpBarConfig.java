package com.ericversteeg;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(HpBarConfig.GROUP)
public interface HpBarConfig extends Config
{
    String GROUP = "hpbar";
}