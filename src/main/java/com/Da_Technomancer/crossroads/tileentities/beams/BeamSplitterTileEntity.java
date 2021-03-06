package com.Da_Technomancer.crossroads.tileentities.beams;

import com.Da_Technomancer.crossroads.API.CRProperties;
import com.Da_Technomancer.crossroads.API.CircuitUtil;
import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.beams.BeamUnit;
import com.Da_Technomancer.crossroads.API.templates.BeamRenderTE;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import com.Da_Technomancer.essentials.blocks.redstone.IRedstoneHandler;
import com.Da_Technomancer.essentials.blocks.redstone.RedstoneUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Crossroads.MODID)
public class BeamSplitterTileEntity extends BeamRenderTE{

	@ObjectHolder("beam_splitter")
	public static TileEntityType<BeamSplitterTileEntity> type = null;

	private Direction dir = null;

	public BeamSplitterTileEntity(){
		super(type);
	}

	private Direction getDir(){
		if(dir == null){
			BlockState state = getBlockState();
			if(state.getBlock() != CRBlocks.beamSplitter){
				return Direction.NORTH;
			}
			dir = state.get(ESProperties.FACING);
		}
		return dir;
	}

	@Override
	public void updateContainingBlockInfo(){
		dir = null;
		super.updateContainingBlockInfo();
	}

	public float getPowerMultiplier(){
		return Math.max(0, Math.min(1F, CircuitUtil.combineRedsSources(redsHandler) / 15F));
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt){
		super.write(nbt);
		redsHandler.write(nbt);
		return nbt;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt){
		super.read(state, nbt);
		redsHandler.read(state, nbt);
	}

	@Override
	protected void doEmit(BeamUnit out){
		int toFill = Math.round(out.getPower() * getPowerMultiplier());
		Direction facing = getDir();
		BeamUnit toDraw;
		BeamUnit remain;

		if(out.isEmpty() || toFill == 0){
			toDraw = BeamUnit.EMPTY;
			remain = out;
		}else{
			toDraw = new BeamUnit(MiscUtil.withdrawExact(out.getValues(), toFill));
			remain = new BeamUnit(out.getEnergy() - toDraw.getEnergy(), out.getPotential() - toDraw.getPotential(), out.getStability() - toDraw.getStability(), out.getVoid() - toDraw.getVoid());
		}

		if(beamer[facing.getIndex()].emit(toDraw, world)){
			refreshBeam(facing.getIndex());
		}
		if(beamer[facing.getOpposite().getIndex()].emit(remain, world)){
			refreshBeam(facing.getOpposite().getIndex());
		}
	}

	@Override
	protected boolean[] inputSides(){
		boolean[] input = new boolean[] {true, true, true, true, true, true};
		Direction facing = getDir();
		input[facing.getIndex()] = false;
		input[facing.getOpposite().getIndex()] = false;
		return input;
	}

	@Override
	protected boolean[] outputSides(){
		boolean[] output = new boolean[6];
		Direction facing = getDir();
		output[facing.getIndex()] = true;
		output[facing.getOpposite().getIndex()] = true;
		return output;
	}

	private void updateSignalState(){
		markDirty();
		BlockState state = getBlockState();
		float powerLevel = getPowerMultiplier();
		int prevPowerLevel = state.get(CRProperties.POWER_LEVEL);
		if(powerLevel <= 0 && prevPowerLevel != 0){
			world.setBlockState(pos, state.with(CRProperties.POWER_LEVEL, 0));
		}else if(powerLevel >= 1F && prevPowerLevel != 2){
			world.setBlockState(pos, state.with(CRProperties.POWER_LEVEL, 2));
		}else if(powerLevel > 0 && powerLevel < 1F && prevPowerLevel != 1){
			world.setBlockState(pos, state.with(CRProperties.POWER_LEVEL, 1));
		}
	}

	@Override
	public void remove(){
		super.remove();
		redsOpt.invalidate();
	}

	public CircuitUtil.InputCircHandler redsHandler = new CircuitUtil.InputCircHandler();
	private LazyOptional<IRedstoneHandler> redsOpt = CircuitUtil.makeBaseCircuitOptional(this, redsHandler, 0, this::updateSignalState);

	@Override
	@SuppressWarnings("unchecked")
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction dir){
		if(cap == RedstoneUtil.REDSTONE_CAPABILITY){
			return (LazyOptional<T>) redsOpt;
		}
		return super.getCapability(cap, dir);
	}
} 
