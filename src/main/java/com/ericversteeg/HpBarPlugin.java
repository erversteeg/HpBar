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
import net.runelite.client.util.RSTimeUnit;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@PluginDescriptor(
	name = "Frost HP & Run",
	description = "Health and run bar overlays."
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

	private long lastHpChange = 0L;
	private int lastHp = -1;

	private long lastPrayerChange = 0L;
	private int lastPrayer = -1;
	private boolean fromActivePrayer = false;

	private long lastRunChange = 0L;
	private int lastRun = -1;

	private long lastAttackChange = 0L;
	private int lastAttack = -1;

	boolean isStaminaActive = false;

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
	public void onConfigChanged(ConfigChanged configChanged)
	{
		overlay.clearHpViewInfo();
		overlay.clearRunViewInfo();
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		Skill skill = statChanged.getSkill();

		if (skill.ordinal() == Skill.HITPOINTS.ordinal())
		{
			int hp = statChanged.getBoostedLevel();
			if (lastHp >= 0 && (hp - lastHp < 0
					|| hp - lastHp > 1))
			{
				lastHpChange = Instant.now().toEpochMilli();
			}

			lastHp = hp;
		}
		else if (skill.ordinal() == Skill.PRAYER.ordinal())
		{
			int prayer = statChanged.getBoostedLevel();
			if (lastPrayer >= 0)
			{
				lastPrayerChange = Instant.now().toEpochMilli();
				fromActivePrayer = false;
			}

			lastPrayer = prayer;
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		Prayer [] prayers = Prayer.values();
		for (Prayer prayer: prayers) {
			if (client.isPrayerActive(prayer)) {
				lastPrayerChange = Instant.now().toEpochMilli();
				fromActivePrayer = true;
				break;
			}
		}

		int run = client.getEnergy() / 100;
		if (lastRun >= 0 && run - lastRun != 0 && run - lastRun != 1)
		{
			lastRunChange = Instant.now().toEpochMilli();
		}
		lastRun = run;

		int attack = client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10;
		if (lastAttack >= 0 && attack - lastAttack < 0)
		{
			lastAttackChange = Instant.now().toEpochMilli();
		}
		lastAttack = attack;

	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// from Timers plugin
		if (event.getVarbitId() == Varbits.RUN_SLOWED_DEPLETION_ACTIVE
				|| event.getVarbitId() == Varbits.STAMINA_EFFECT
				|| event.getVarbitId() == Varbits.RING_OF_ENDURANCE_EFFECT) {
			int staminaEffectActive = client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE);
			int staminaPotionEffectVarb = client.getVarbitValue(Varbits.STAMINA_EFFECT);
			int enduranceRingEffectVarb = client.getVarbitValue(Varbits.RING_OF_ENDURANCE_EFFECT);

			final int totalStaminaEffect = staminaPotionEffectVarb + enduranceRingEffectVarb;
			if (staminaEffectActive == 1)
			{
				isStaminaActive = totalStaminaEffect != 0;
			}
		}
	}

	public boolean isActive()
	{
		long lastActive = getLastActive();

		long hideDelay = 3600L;
		if (lastActive == lastPrayerChange && fromActivePrayer) {
			hideDelay = 1800L;
		}

		return Instant.now().toEpochMilli() - lastActive <= hideDelay;
	}

	public long getLastActive()
	{
		long lastActive = 0L;

		if (overlay.hasBarType(BarType.HITPOINTS) && lastHpChange > lastActive)
		{
			lastActive = lastHpChange;
		}
		if (overlay.hasBarType(BarType.PRAYER) && lastPrayerChange > lastActive)
		{
			lastActive = lastPrayerChange;
		}
		if (overlay.hasBarType(BarType.SPECIAL_ATTACK) && lastAttackChange > lastActive)
		{
			lastActive = lastAttackChange;
		}
		if (!config.showRunBar())
		{
			if (overlay.hasBarType(BarType.RUN_ENERGY) && lastRunChange > lastActive)
			{
				lastActive = lastRunChange;
			}
		}
		return lastActive;
	}

	public boolean isRun()
	{
		return Instant.now().toEpochMilli() - lastRunChange <= 2400L;
	}

	public Map<BarType, BarInfo> barInfo()
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

		int runHue = 50;
		if (isStaminaActive)
		{
			runHue = 25;
		}

		BarInfo run = new BarInfo(
				client.getEnergy() / 100,
				100,
				runHue
		);

		Map<BarType, BarInfo> barInfo = new HashMap<>();

		barInfo.put(BarType.HITPOINTS, hitpoints);
		barInfo.put(BarType.PRAYER, prayer);
		barInfo.put(BarType.SPECIAL_ATTACK, attack);
		barInfo.put(BarType.RUN_ENERGY, run);

		return barInfo;
	}
}
