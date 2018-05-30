package com.Da_Technomancer.crossroads.tileentities.technomancy;

import com.Da_Technomancer.crossroads.API.Properties;
import com.Da_Technomancer.crossroads.blocks.ModBlocks;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class SquareRootAxisTileEntity extends AbstractMathAxisTE{

	private EnumFacing facing;

	@Override
	protected double getOutSpeed(double speed1, double speed2){
		return speed1 < 0 ? 0 : Math.sqrt(speed1);
	}

	@Override
	protected EnumFacing getInOne(){
		if(facing == null){
			if(world.getBlockState(pos).getBlock() != ModBlocks.squareRootAxis){
				return EnumFacing.DOWN;
			}
			facing = world.getBlockState(pos).getValue(Properties.FACING);
		}
		return facing.getOpposite();
	}

	@Nullable
	@Override
	protected EnumFacing getInTwo(){
		return null;
	}

	@Override
	protected EnumFacing getOut(){
		if(facing == null){
			if(world.getBlockState(pos).getBlock() != ModBlocks.squareRootAxis){
				return EnumFacing.DOWN;
			}
			facing = world.getBlockState(pos).getValue(Properties.FACING);
		}
		return facing;
	}

	@Override
	protected EnumFacing getBattery(){
		return EnumFacing.DOWN;
	}

	@Override
	protected void cleanDirCache(){
		facing = null;
	}
}
