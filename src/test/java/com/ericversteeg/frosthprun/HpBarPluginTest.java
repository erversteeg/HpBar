package com.ericversteeg.frosthprun;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HpBarPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FrostHpRunPlugin.class);
		RuneLite.main(args);
	}
}