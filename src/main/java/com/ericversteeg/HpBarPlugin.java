package com.ericversteeg;

import com.google.gson.Gson;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
	name = "HpBar",
	description = "Hp bar."
)

public class HpBarPlugin extends Plugin
{
	@Inject private HpBarOverlay overlay;
	@Inject private OverlayManager overlayManager;
	@Inject private Client client;
	@Inject
    HpBarConfig config;
	@Inject private ConfigManager configManager;
	@Inject private Gson gson;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Provides
    HpBarConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HpBarConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged config)
	{

	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{

	}
}
