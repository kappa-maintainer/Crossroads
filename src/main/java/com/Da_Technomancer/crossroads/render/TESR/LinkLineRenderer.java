package com.Da_Technomancer.crossroads.render.TESR;

import com.Da_Technomancer.crossroads.API.templates.ILinkTE;
import com.Da_Technomancer.crossroads.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class LinkLineRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/model/link_line.png");

	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		if(te == null || !te.getWorld().isBlockLoaded(te.getPos(), false) || !ILinkTE.isLinkTool(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND)) && !ILinkTE.isLinkTool(Minecraft.getMinecraft().player.getHeldItem(EnumHand.OFF_HAND))){
			return;
		}

		if(te instanceof ILinkTE){
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.enableBlend();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1, 1, 1, 0.5F);
			float brightX = OpenGlHelper.lastBrightnessX;
			float brightY = OpenGlHelper.lastBrightnessY;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

			Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
			GlStateManager.translate(x + .5D, y + 0.5D, z + .5D);


			BufferBuilder vb = Tessellator.getInstance().getBuffer();
			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			for(BlockPos link : ((ILinkTE) te).getLinks()){
				double length = link.getDistance(0, 0, 0);
				Vec3d widthVec = Minecraft.getMinecraft().player.getLook(partialTicks).crossProduct(new Vec3d(link.getX(), link.getY(), link.getZ()));
				widthVec = widthVec.scale(0.15D / widthVec.length());
				vb.pos(link.getX() - widthVec.x, link.getY() - widthVec.y, link.getZ() - widthVec.z).tex(0, length).endVertex();
				vb.pos(link.getX() + widthVec.x, link.getY() + widthVec.y, link.getZ() + widthVec.z).tex(1, length).endVertex();
				vb.pos(widthVec.x, widthVec.y, widthVec.z).tex(1, 0).endVertex();
				vb.pos(-widthVec.x, -widthVec.y, -widthVec.z).tex(0, 0).endVertex();
			}
			Tessellator.getInstance().draw();

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.enableCull();
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}else{
			Main.logger.error("Non-linking TileEntity attempting to render links! Report to mod author. TE: " + te.getClass().getName());
		}
	}
}