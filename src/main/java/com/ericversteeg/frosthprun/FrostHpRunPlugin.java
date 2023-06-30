package com.ericversteeg.frosthprun;

import com.ericversteeg.frosthprun.config.BarInfo;
import com.ericversteeg.frosthprun.config.BarType;
import com.google.gson.Gson;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.*;

@PluginDescriptor(
	name = "Frost HP & Run",
	description = "Hitpoints and run bar overlays."
)

public class FrostHpRunPlugin extends Plugin
{
	@Inject private FrostHpRunOverlay overlay;
	@Inject private OverlayManager overlayManager;
	@Inject private Client client;
	@Inject private FrostHpRunConfig config;
	@Inject private ConfigManager configManager;
	@Inject private Gson gson;

	private static final int VENOM_THRESHOLD = 1000000;

	private long lastHpChange = 0L;
	private int lastHp = -1;

	private long lastPrayerChange = 0L;
	private int lastPrayer = -1;
	private boolean fromActivePrayer = false;

	private long lastRunChange = 0L;
	private int lastRun = -1;
	private boolean fromRunIncrease = false;

	private long lastCombatChange = 0L;

	boolean isStaminaActive = false;
	private boolean lastStaminaActive = false;

	private boolean isPoisoned = false;
	private boolean isEnvenomed = false;
	private boolean isDiseased = false;

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
	FrostHpRunConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FrostHpRunConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getGroup().equals(FrostHpRunConfig.GROUP))
		{
			overlay.clearHpViewInfo();
			overlay.clearRunViewInfo();
		}
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
			if (lastPrayer >= 0 && lastPrayer != prayer)
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
		Player localPlayer = client.getLocalPlayer();

		if (localPlayer != null)
		{
			// from Status Bars plugin
			Actor interacting = localPlayer.getInteracting();

			if ((interacting instanceof NPC && ArrayUtils.contains(((NPC) interacting).getComposition().getActions(), "Attack"))
					|| (interacting instanceof Player && client.getVarbitValue(Varbits.PVP_SPEC_ORB) == 1))
			{
				lastCombatChange = Instant.now().toEpochMilli();
			}
		}

		if (isPrayerActive())
		{
			lastPrayerChange = Instant.now().toEpochMilli();
			fromActivePrayer = true;
		}

		LocalPoint currentLocation = client.getLocalPlayer().getLocalLocation();
		LocalPoint destinationLocation = client.getLocalDestinationLocation();

		if (currentLocation != null && destinationLocation != null)
		{
			WorldPoint worldLocation = WorldPoint.fromLocal(client, currentLocation);
			WorldPoint worldDestination = WorldPoint.fromLocal(client, destinationLocation);

			int distance = worldLocation.distanceTo(worldDestination);
			if (distance > 2)
			{
				lastRunChange = Instant.now().toEpochMilli();
				fromRunIncrease = false;
			}
		}

		int run = client.getEnergy() / 100;
		int runDiff = run - lastRun;
		if (lastRun != -1 && runDiff != 0 && runDiff != 1)
		{
			if (runDiff > 0)
			{
				fromRunIncrease = true;
				lastRunChange = Instant.now().toEpochMilli();
			}
		}
		lastRun = run;

		// from Poison plugin
		final int poison = client.getVarpValue(VarPlayer.POISON);

		if (poison >= VENOM_THRESHOLD)
		{
			isPoisoned = false;
			isEnvenomed = true;
		}
		else if (poison > 0)
		{
			isPoisoned = true;
			isEnvenomed = false;
		}
		else
		{
			isPoisoned = false;
			isEnvenomed = false;
		}

		isDiseased = client.getVarpValue(VarPlayer.DISEASE_VALUE) > 0;
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// from Timers plugin
		if (event.getVarbitId() == Varbits.RUN_SLOWED_DEPLETION_ACTIVE
				|| event.getVarbitId() == Varbits.STAMINA_EFFECT
				|| event.getVarbitId() == Varbits.RING_OF_ENDURANCE_EFFECT)
		{
			int staminaEffectActive = client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE);
			int staminaPotionEffectVarb = client.getVarbitValue(Varbits.STAMINA_EFFECT);
			int enduranceRingEffectVarb = client.getVarbitValue(Varbits.RING_OF_ENDURANCE_EFFECT);

			final int totalStaminaEffect = staminaPotionEffectVarb + enduranceRingEffectVarb;
			if (staminaEffectActive == 1)
			{
				isStaminaActive = totalStaminaEffect != 0;
				if (!lastStaminaActive && isStaminaActive)
				{
					lastRunChange = Instant.now().toEpochMilli();
					fromRunIncrease = true;
				}
				lastStaminaActive = isStaminaActive;
			}
		}
	}

	public boolean isActive()
	{
		long lastActive = getLastActive();

		long delay = 1800L;
		if (lastActive == lastHpChange || (lastActive == lastPrayerChange && !fromActivePrayer)
				|| (!config.showRunBar() && lastActive == lastRunChange && fromRunIncrease))
		{
			delay = 3600L;
		}
		else if (lastActive == lastCombatChange)
		{
			delay = (config.combatHideDelay() + 2) * 600L;
		}
		else if (lastActive == lastPrayerChange && fromActivePrayer)
		{
			delay = 1200L;
		}
		return Instant.now().toEpochMilli() - lastActive <= delay;
	}

	public long getLastActive()
	{
		long lastActive = lastCombatChange;

		if (overlay.hasBarType(BarType.HITPOINTS) && lastHpChange > lastActive)
		{
			lastActive = lastHpChange;
		}
		if (overlay.hasBarType(BarType.PRAYER) && lastPrayerChange > lastActive)
		{
			lastActive = lastPrayerChange;
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
		long delay = 1800L;
		if (fromRunIncrease)
		{
			delay = 3600L;
		}
		return Instant.now().toEpochMilli() - lastRunChange <= delay;
	}

	private boolean isPrayerActive()
	{
		Prayer [] prayers = Prayer.values();
		for (Prayer prayer: prayers) {
			if (client.isPrayerActive(prayer))
			{
				return true;
			}
		}
		return false;
	}

	public Map<BarType, BarInfo> barInfo()
	{
		int hpHue = 0;
		if (isEnvenomed)
		{
			hpHue = 145;
		}
		else if (isPoisoned)
		{
			hpHue = 95;
		}
		else if (isDiseased)
		{
			hpHue = 75;
		}

		BarInfo hitpoints = new BarInfo(
				client.getBoostedSkillLevel(Skill.HITPOINTS),
				client.getRealSkillLevel(Skill.HITPOINTS),
				hpHue
		);

		int prayerHue = 175;
		if (isPrayerActive())
		{
			prayerHue = 155;
		}

		BarInfo prayer = new BarInfo(
				client.getBoostedSkillLevel(Skill.PRAYER),
				client.getRealSkillLevel(Skill.PRAYER),
				prayerHue
		);

		BarInfo attack = new BarInfo(
				client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
				100,
				121
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
