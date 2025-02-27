package net.spellcraftgaming.rpghud.gui.hud.element.modern;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementDetailsVanilla;
import net.spellcraftgaming.rpghud.main.ModRPGHud;
import net.spellcraftgaming.rpghud.settings.Settings;

public class HudElementDetailsModern extends HudElementDetailsVanilla {

	public HudElementDetailsModern() {
		super();
		this.posX = 0;
		this.posY = 0;
		this.elementWidth = 0;
		this.elementHeight = 0;
		this.moveable = true;
	}

	@Override
	public boolean checkConditions() {
		return !this.mc.gameSettings.showDebugInfo && !this.isChatOpen();
	}

	@Override
	public void drawElement(AbstractGui gui, MatrixStack ms, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
		this.offset = (this.settings.getBoolValue(Settings.render_player_face) ? 0 : 16) + ((this.settings.getBoolValue(Settings.show_numbers_health) && this.settings.getBoolValue(Settings.show_numbers_food)) ? 0 : 8);
		int width = calculateWidth();
		if (gui instanceof ForgeIngameGui) {
			if (this.settings.getBoolValue(Settings.show_armor)) {
				GL11.glTranslated(this.settings.getPositionValue(Settings.armor_det_position)[0], this.settings.getPositionValue(Settings.armor_det_position)[1], 0);
				drawArmorDetails(gui, ms, width);
				GL11.glTranslated(-this.settings.getPositionValue(Settings.armor_det_position)[0], -this.settings.getPositionValue(Settings.armor_det_position)[1], 0);
			}
			GL11.glTranslated(this.settings.getPositionValue(Settings.item_det_position)[0], this.settings.getPositionValue(Settings.item_det_position)[1], 0);
			drawItemDetails(gui, ms, Hand.MAIN_HAND, width);
			drawItemDetails(gui, ms, Hand.OFF_HAND, width);
			GL11.glTranslated(-this.settings.getPositionValue(Settings.item_det_position)[0], -this.settings.getPositionValue(Settings.item_det_position)[1], 0);
			if (this.settings.getBoolValue(Settings.show_arrow_count)) {
				GL11.glTranslated(this.settings.getPositionValue(Settings.arrow_det_position)[0], this.settings.getPositionValue(Settings.arrow_det_position)[1], 0);
				drawArrowCount(gui, ms, width);
				GL11.glTranslated(-this.settings.getPositionValue(Settings.arrow_det_position)[0], -this.settings.getPositionValue(Settings.arrow_det_position)[1], 0);
			}
		}
	}

	/** Calculates the width for the element background */
	private int calculateWidth() {
		int width = 0;
		for (int i = this.mc.player.inventory.armorInventory.size() - 1; i >= 0; i--) {
			if (this.mc.player.inventory.armorItemInSlot(i) != ItemStack.EMPTY && this.mc.player.inventory.armorItemInSlot(i).getItem().isDamageable()) {
				ItemStack item = this.mc.player.inventory.armorItemInSlot(i);
				String s = (item.getMaxDamage() - item.getDamage()) + "/" + item.getMaxDamage();
				int widthNew = this.mc.fontRenderer.getStringWidth(s);
				if (widthNew > width)
					width = widthNew;
			}
		}
		ItemStack item = this.mc.player.getHeldItemMainhand();
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageable()) {
				String s = (item.getMaxDamage() - item.getDamage()) + "/" + item.getMaxDamage();
				int widthNew = this.mc.fontRenderer.getStringWidth(s);
                if (widthNew > width)
                    width = widthNew;
			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x = this.mc.player.inventory.getSizeInventory();
				int z = 0;
				if (ModRPGHud.renderDetailsAgain[0] || !ItemStack.areItemStacksEqual(this.itemMainHandLast, item)) {
					this.itemMainHandLast = item.copy();
					ModRPGHud.renderDetailsAgain[0] = false;

					for (int y = 0; y < x; y++) {
						item = this.mc.player.inventory.getStackInSlot(y);
						if (item != ItemStack.EMPTY && Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(this.mc.player.getHeldItemMainhand().getItem())) {
							z += item.getCount();
						}
					}
					this.count1 = z;
				} else {
					z = this.count1;
				}
				
				String s = "x " + z;
				int widthNew = this.mc.fontRenderer.getStringWidth(s);
				if (widthNew > width)
					width = widthNew;
			}
		}
		item = this.mc.player.getHeldItemOffhand();
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageable()) {
				String s = (item.getMaxDamage() - item.getDamage()) + "/" + item.getMaxDamage();
				int widthNew = this.mc.fontRenderer.getStringWidth(s);
				if (widthNew > width)
					width = widthNew;
			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x =  this.mc.player.inventory.getSizeInventory();
				int z = 0;
				if (ModRPGHud.renderDetailsAgain[1] || !ItemStack.areItemStacksEqual(this.itemOffhandLast, item) || !ItemStack.areItemStacksEqual(this.itemMainHandLast, item)) {
					this.itemOffhandLast = item.copy();
					ModRPGHud.renderDetailsAgain[1] = false;
					for (int y = 0; y < x; y++) {
						item = this.mc.player.inventory.getStackInSlot(y);
						if (item != ItemStack.EMPTY && Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(this.mc.player.getHeldItemOffhand().getItem())) {
							z += item.getCount();
						}
					}
					this.count2 = z;
				} else {
					z = this.count2;
				}
				String s = "x " + z;
				int widthNew = this.mc.fontRenderer.getStringWidth(s);
				if (widthNew > width)
					width = widthNew;
			}
		}
		item = this.mc.player.getHeldItemMainhand();
		if (this.settings.getBoolValue(Settings.show_arrow_count) && item != ItemStack.EMPTY && this.mc.player.getHeldItemMainhand().getItem() instanceof BowItem) {
			int x =  this.mc.player.inventory.getSizeInventory();
			int z = 0;

			if (ModRPGHud.renderDetailsAgain[2] || !ItemStack.areItemStacksEqual(this.itemMainHandLastArrow, item)) {
				ModRPGHud.renderDetailsAgain[2] = false;

				item = findAmmo(this.mc.player);
				if (item != ItemStack.EMPTY) {
					this.itemArrow = item.copy();
					for (int y = 0; y < x; y++) {
						ItemStack item3 = this.mc.player.inventory.getStackInSlot(y);
						if (ItemStack.areItemsEqual(item, item3)) {
							z += addArrowStackIfCorrect(item, item3);
						}
					}
					this.count3 = z;
				}
				this.count3 = 0;
			} else {
				z = this.count3;
			}
			String s = "x " + z;
			int widthNew = this.mc.fontRenderer.getStringWidth(s);
			if (widthNew > width)
				width = widthNew;
		}
		if (item == ItemStack.EMPTY || item == null) {
			this.itemMainHandLastArrow = ItemStack.EMPTY;
		} else {
			this.itemMainHandLastArrow = item.copy();
		}

		return width;
	}

	/**
	 * Draws the armor details
	 * 
	 * @param gui
	 *            the GUI to draw one
	 * @param width
	 *            the width of the background
	 */
	protected void drawArmorDetails(AbstractGui gui, MatrixStack ms, int width) {
		for (int i = this.mc.player.inventory.armorInventory.size() - 1; i >= 0; i--) {
			if (this.mc.player.inventory.armorItemInSlot(i) != ItemStack.EMPTY && this.mc.player.inventory.armorItemInSlot(i).getItem().isDamageable()) {
				drawRect(2, 30 + this.offset / 2, 10 + 6 + (width / 2), 10, 0xA0000000);
				RenderSystem.scaled(0.5D, 0.5D, 0.5D);
				ItemStack item = this.mc.player.inventory.armorItemInSlot(i);
				String s = (item.getMaxDamage() - item.getDamage()) + "/" + item.getMaxDamage();
				this.mc.getItemRenderer().renderItemIntoGUI(item, 6, 62 + this.offset);
				if(this.settings.getBoolValue(Settings.show_durability_bar)) this.mc.getItemRenderer().renderItemOverlays(this.mc.fontRenderer, item, 6, 62 + this.offset);
				RenderHelper.disableStandardItemLighting();
				AbstractGui.drawCenteredString(ms, this.mc.fontRenderer, s, 32 + width / 2, 66 + this.offset, -1);
				RenderSystem.scaled(2.0D, 2.0D, 2.0D);
				this.offset += 20;
			}
		}
	}

	/**
	 * Draws the held item details
	 * 
	 * @param gui
	 *            the GUI to draw on
	 * @param hand
	 *            the hand whose item should be detailed
	 * @param width
	 *            the width of the background
	 */
	protected void drawItemDetails(AbstractGui gui, MatrixStack ms, Hand hand, int width) {
		ItemStack item = this.mc.player.getHeldItem(hand);
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageable()) {
				drawRect(2, 30 + this.offset / 2, 10 + 6 + (width / 2), 10, 0xA0000000);
				String s = (item.getMaxDamage() - item.getDamage()) + "/" + item.getMaxDamage();
				RenderHelper.enableStandardItemLighting();
				RenderSystem.scaled(0.5, 0.5, 0.5);
				this.mc.getItemRenderer().renderItemIntoGUI(item, 6, 62 + this.offset);
				if(this.settings.getBoolValue(Settings.show_durability_bar)) this.mc.getItemRenderer().renderItemOverlays(this.mc.fontRenderer, item, 6, 62 + this.offset);
				RenderHelper.disableStandardItemLighting();
				AbstractGui.drawCenteredString(ms, this.mc.fontRenderer, s, 32 + width / 2, 66 + this.offset, -1);
				RenderSystem.scaled(2.0, 2.0, 2.0);
				RenderHelper.disableStandardItemLighting();
				this.offset += 20;

			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x =  this.mc.player.inventory.getSizeInventory();
				int z = 0;
				if ((hand == Hand.MAIN_HAND ? ModRPGHud.renderDetailsAgain[0] : ModRPGHud.renderDetailsAgain[1]) || !ItemStack.areItemStacksEqual((hand == Hand.MAIN_HAND ? this.itemMainHandLast : this.itemOffhandLast), item) || !ItemStack.areItemStacksEqual(this.itemMainHandLast, item)) {
					if (hand == Hand.MAIN_HAND) {
						this.itemMainHandLast = item.copy();
						ModRPGHud.renderDetailsAgain[0] = false;
					} else {
						this.itemOffhandLast = item.copy();
						ModRPGHud.renderDetailsAgain[1] = false;
					}
					for (int y = 0; y < x; y++) {
						item = this.mc.player.inventory.getStackInSlot(y);
						if (item != ItemStack.EMPTY && Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(this.mc.player.getHeldItem(hand).getItem())) {
							z += item.getCount();
						}
					}
					if (hand == Hand.MAIN_HAND)
						this.count1 = z;
					else
						this.count2 = z;
				} else {
					if (hand == Hand.MAIN_HAND)
						z = this.count1;
					else
						z = this.count2;
				}

				item = this.mc.player.getHeldItem(hand);
				drawRect(2, 30 + this.offset / 2, 10 + 6 + (width / 2), 10, 0xA0000000);
				String s = "x " + z;
				RenderSystem.scaled(0.5D, 0.5D, 0.5D);
				RenderHelper.enableStandardItemLighting();
				this.mc.getItemRenderer().renderItemIntoGUI(item, 6, 62 + this.offset);
				RenderHelper.disableStandardItemLighting();
				AbstractGui.drawCenteredString(ms, this.mc.fontRenderer, s, 32 + width / 2, 66 + this.offset, -1);
				RenderSystem.scaled(2.0D, 2.0D, 2.0D);
				this.offset += 20;
			}
		}
	}

	/**
	 * Draws the amount of arrows the player has in his inventory on the screen
	 * 
	 * @param gui
	 *            the GUI to draw on
	 * @param width
	 *            the width of the background
	 */
	protected void drawArrowCount(AbstractGui gui, MatrixStack ms, int width) {
		ItemStack item = this.mc.player.getHeldItemMainhand();
		if (this.settings.getBoolValue(Settings.show_arrow_count) && item != ItemStack.EMPTY && this.mc.player.getHeldItemMainhand().getItem() instanceof BowItem) {
			int x =  this.mc.player.inventory.getSizeInventory();
			int z = 0;

			if (ModRPGHud.renderDetailsAgain[2] || !ItemStack.areItemStacksEqual(this.itemMainHandLastArrow, item)) {
				ModRPGHud.renderDetailsAgain[2] = false;

				item = findAmmo(this.mc.player);
				if (item != ItemStack.EMPTY) {
					this.itemArrow = item.copy();
					for (int y = 0; y < x; y++) {
						ItemStack item3 = this.mc.player.inventory.getStackInSlot(y);
						if (ItemStack.areItemsEqual(item, item3)) {
							z += addArrowStackIfCorrect(item, item3);
						}
					}
					this.count3 = z;
				} else {
					this.count3 = 0;
				}
			} else {
				z = this.count3;
			}
			drawRect(2, 30 + this.offset / 2, 10 + 6 + (width / 2), 10, 0xA0000000);
			String s = "x " + z;
			RenderSystem.scaled(0.5D, 0.5D, 0.5D);
			RenderHelper.enableStandardItemLighting();
			if (this.itemArrow == ItemStack.EMPTY)
				this.itemArrow = new ItemStack(Items.ARROW);
			this.mc.getItemRenderer().renderItemIntoGUI(this.itemArrow, 6, 62 + this.offset);
			RenderHelper.disableStandardItemLighting();
			AbstractGui.drawCenteredString(ms, this.mc.fontRenderer, s, 32 + width / 2, 66 + this.offset, -1);
			RenderSystem.scaled(2.0D, 2.0D, 2.0D);
			this.offset += 20;

		}
		if (item == ItemStack.EMPTY || item == null) {
			this.itemMainHandLastArrow = ItemStack.EMPTY;
		} else {
			this.itemMainHandLastArrow = item.copy();
		}
	}

}
