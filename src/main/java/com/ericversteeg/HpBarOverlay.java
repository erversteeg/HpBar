package com.ericversteeg;

import com.ericversteeg.view.*;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import javax.inject.Inject;
import java.awt.*;

class HpBarOverlay extends RSViewOverlay {

	private final Client client;
	private final ItemManager itemManager;
	private final HpBarPlugin plugin;
	private final HpBarConfig config;

	private Font font;

	@Inject
	private HpBarOverlay(
			Client client,
			ItemManager itemManager,
			HpBarPlugin plugin,
			HpBarConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);

		this.client = client;
		this.itemManager = itemManager;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	void updateViews() {
		clearViewInfo();

		RSBox hpBarContainer = new RSBox(570,350, 125, 17);
		RSHpBar hpBar = new RSHpBar(0, 0, RSView.MATCH_PARENT, RSView.MATCH_PARENT);
		RSTextView hpText = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, FontManager.getRunescapeFont());

		hpText.setText(String.format("%d", client.getBoostedSkillLevel(Skill.HITPOINTS)));
		hpText.setLayoutGravity(RSViewGroup.Gravity.CENTER);

		hpBarContainer.addView(hpBar);
		hpBarContainer.addView(hpText);

		addViewInfo(new ViewInfo(client, hpBarContainer, RSAnchorType.TOP_LEFT, 570,350));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		updateViews();
		return super.render(graphics);
	}
}
