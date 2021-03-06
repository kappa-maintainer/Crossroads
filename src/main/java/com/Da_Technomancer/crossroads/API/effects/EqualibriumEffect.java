package com.Da_Technomancer.crossroads.API.effects;

import com.Da_Technomancer.crossroads.API.beams.EnumBeamAlignments;
import com.Da_Technomancer.crossroads.entity.EntityGhostMarker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EqualibriumEffect extends BeamEffect{

	@Override
	public void doBeamEffect(EnumBeamAlignments align, boolean voi, int power, World worldIn, BlockPos pos, @Nullable Direction dir){
		if(!performTransmute(align, voi, power, worldIn, pos)){
			if(voi){
				EntityGhostMarker marker = new EntityGhostMarker(worldIn, EntityGhostMarker.EnumMarkerType.VOID_EQUILIBRIUM);
				marker.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
				CompoundNBT rangeData = new CompoundNBT();
				rangeData.putInt("range", power);
				marker.data = rangeData;
				worldIn.addEntity(marker);

				//Effect in crystal master axis.
			}else{
				EntityGhostMarker marker = new EntityGhostMarker(worldIn, EntityGhostMarker.EnumMarkerType.EQUILIBRIUM);
				marker.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
				CompoundNBT rangeData = new CompoundNBT();
				rangeData.putInt("range", power);
				marker.data = rangeData;
				worldIn.addEntity(marker);

				//Effect in crystal master axis
			}
		}
	}
}
