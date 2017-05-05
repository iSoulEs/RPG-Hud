package net.spellcraftgaming.rpghud.gui.hud.element.defaulthud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.spellcraftgaming.lib.GameData;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;

public class HudElementLevelDefault extends HudElement {

	public HudElementLevelDefault() {
		super(HudElementType.LEVEL, 0, 0, 0, 0, true);
		this.parent = HudElementType.WIDGET;
	}

	@Override
	public boolean checkConditions() {
		return GameData.shouldDrawHUD();
	}

	@Override
	public void drawElement(Gui gui, float zLevel, float partialTicks, double scale) {
		GlStateManager.disableBlend();
		String level = String.valueOf(GameData.getPlayerXPLevel());
		drawStringWithShadow(level, (this.settings.getBoolValue(Settings.render_player_face) ? 38 : 12) - this.mc.fontRendererObj.getStringWidth(level) / 2, (this.settings.getBoolValue(Settings.render_player_face) ? 38 : 14), 0x80FF20, scale);
		GlStateManager.enableBlend();
	}

}
