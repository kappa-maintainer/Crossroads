package com.Da_Technomancer.crossroads.API.effects.alchemy;

import com.Da_Technomancer.crossroads.API.alchemy.EnumMatterPhase;
import com.Da_Technomancer.crossroads.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class VoltusEffect implements IAlchEffect{

	private static final Color[] BOLT_COLORS = new Color[] {new Color(255, 255, 0, 220), new Color(255, 228, 34, 220), new Color(255, 194, 62, 220)};

	@Override
	public void doEffect(World world, BlockPos pos, int amount, double temp, EnumMatterPhase phase) {
		if(phase != EnumMatterPhase.FLAME || Math.random() > 0.92D){
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX() - 5, pos.getY() - 5, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5), EntitySelectors.IS_ALIVE);
			for(EntityLivingBase ent : ents){
				ent.onStruckByLightning(null);
				RenderUtil.addArc(world.provider.getDimension(), (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, (float) ent.posX, (float) ent.posY, (float) ent.posZ, 1, 0F, BOLT_COLORS[(int) (world.getTotalWorldTime() % 3)].getRGB());
			}
		}
	}
}
