package com.Da_Technomancer.crossroads.blocks.technomancy;

import com.Da_Technomancer.crossroads.API.templates.BeamBlock;
import com.Da_Technomancer.crossroads.tileentities.technomancy.ClockworkStabilizerTileEntity;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import com.Da_Technomancer.essentials.blocks.redstone.IReadable;
import com.Da_Technomancer.essentials.blocks.redstone.RedstoneUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClockworkStabilizer extends BeamBlock implements IReadable{

	private static final VoxelShape[] SHAPE = new VoxelShape[6];

	static{
		//Very crude shape to match the angled model- may be worth refining later
		SHAPE[0] = makeCuboidShape(4, 0, 4, 12, 16, 12);
		SHAPE[1] = makeCuboidShape(4, 0, 4, 12, 16, 12);
		SHAPE[2] = makeCuboidShape(4, 4, 0, 12, 12, 16);
		SHAPE[3] = makeCuboidShape(4, 4, 0, 12, 12, 16);
		SHAPE[4] = makeCuboidShape(0, 4, 0, 16, 12, 12);
		SHAPE[5] = makeCuboidShape(0, 4, 0, 16, 12, 12);
	}

	public ClockworkStabilizer(){
		super("clock_stabilizer");
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return SHAPE[state.get(ESProperties.FACING).getIndex()];
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return new ClockworkStabilizerTileEntity();
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state){
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos){
		return RedstoneUtil.clampToVanilla(read(worldIn, pos, blockState));
	}

	@Override
	public float read(World world, BlockPos pos, BlockState blockState){
		TileEntity te = world.getTileEntity(pos);
		return te instanceof ClockworkStabilizerTileEntity ? ((ClockworkStabilizerTileEntity) te).getRedstone() : 0;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag){
		tooltip.add(new TranslationTextComponent("tt.crossroads.clock_stab.desc", ClockworkStabilizerTileEntity.RATE * 100));
		tooltip.add(new TranslationTextComponent("tt.crossroads.clock_stab.circuit"));
	}
}