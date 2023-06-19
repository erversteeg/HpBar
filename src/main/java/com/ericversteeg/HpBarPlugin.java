package com.ericversteeg;

import com.ericversteeg.config.BarInfo;
import com.ericversteeg.config.BarType;
import com.google.gson.Gson;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Instant;
import java.util.*;

@PluginDescriptor(
	name = "Hp Bar",
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

	private long lastSkillChange = 0L;
	private int lastHp = -1;

	private long lastRunChange = 0L;
	private int lastRun = -1;

	private HashSet<Skill> initSkills = new HashSet<>();

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
	public void onStatChanged(StatChanged statChanged)
	{
		Skill skill = statChanged.getSkill();

		if (!initSkills.contains(skill))
		{
			initSkills.add(skill);
			return;
		}

		if (skill.ordinal() == Skill.ATTACK.ordinal()
				|| skill.ordinal() == Skill.STRENGTH.ordinal()
				|| skill.ordinal() == Skill.DEFENCE.ordinal()
				|| skill.ordinal() == Skill.RANGED.ordinal()
				|| skill.ordinal() == Skill.PRAYER.ordinal()
				|| skill.ordinal() == Skill.MAGIC.ordinal())
		{
			lastSkillChange = Instant.now().toEpochMilli();
		}

		if (skill.ordinal() == Skill.HITPOINTS.ordinal())
		{
			int hp = statChanged.getBoostedLevel();
			if (hp - lastHp != 0 && hp - lastHp != 1)
			{
				lastSkillChange = Instant.now().toEpochMilli();
			}

			lastHp = hp;
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		int run = client.getEnergy() / 100;
		if (run < lastRun)
		{
			lastRunChange = Instant.now().toEpochMilli();
		}
		lastRun = run;
	}

	public boolean isCombat()
	{
		return Instant.now().toEpochMilli() - lastSkillChange < 7000L;
	}

	public boolean isRun()
	{
		return Instant.now().toEpochMilli() - lastRunChange < 3000L
				&& Instant.now().toEpochMilli() - lastSkillChange > 30000L;
	}

	Map<BarType, BarInfo> barInfo()
	{
		BarInfo hitpoints = new BarInfo(
				client.getBoostedSkillLevel(Skill.HITPOINTS),
				client.getRealSkillLevel(Skill.HITPOINTS),
				0
		);

		BarInfo prayer = new BarInfo(
				client.getBoostedSkillLevel(Skill.PRAYER),
				client.getRealSkillLevel(Skill.PRAYER),
				155
		);

		BarInfo attack = new BarInfo(
				client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
				100,
				180
		);

		BarInfo run = new BarInfo(
				client.getEnergy() / 100,
				100,
				50
		);

		Map<BarType, BarInfo> barInfo = new HashMap<>();

		barInfo.put(BarType.HITPOINTS, hitpoints);
		barInfo.put(BarType.PRAYER, prayer);
		barInfo.put(BarType.SPECIAL_ATTACK, attack);
		barInfo.put(BarType.RUN_ENERGY, run);

		return barInfo;
	}
}
