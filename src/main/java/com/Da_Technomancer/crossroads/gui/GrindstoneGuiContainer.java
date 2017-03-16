package com.Da_Technomancer.crossroads.gui;

import com.Da_Technomancer.crossroads.Main;
import com.Da_Technomancer.crossroads.gui.container.GrindstoneContainer;
import com.Da_Technomancer.crossroads.tileentities.rotary.GrindstoneTileEntity;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GrindstoneGuiContainer extends GuiContainer{

	private IInventory playerInv;
	private GrindstoneTileEntity te;

	public GrindstoneGuiContainer(IInventory playerInv, GrindstoneTileEntity te){
		super(new GrindstoneContainer(playerInv, te));
		this.playerInv = playerInv;
		this.te = te;

		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID + ":textures/gui/container/grindstone_gui.png"));
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		drawTexturedModalRect(guiLeft + 66, guiTop + 35, 176, 0, 44, getScaledProgress());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		String s = this.te.getDisplayName().getUnformattedText();
		this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);
	}

	private int getScaledProgress(){
		return (int) Math.ceil(te.getField(0) * 17 / GrindstoneTileEntity.REQUIRED);
	}

}
