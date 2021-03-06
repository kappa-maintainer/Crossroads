package com.Da_Technomancer.crossroads.blocks.alchemy;

import com.Da_Technomancer.crossroads.API.CRProperties;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.alchemy.CoolingCoilTileEntity;
import com.Da_Technomancer.essentials.ESConfig;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CoolingCoil extends ContainerBlock{

	private static final VoxelShape SHAPE_X = makeCuboidShape(0, 4, 4, 16, 12, 12);
	private static final VoxelShape SHAPE_Z = makeCuboidShape(4, 4, 0, 12, 12, 16);

	private final boolean crystal;

	public CoolingCoil(boolean crystal){
		super(CRBlocks.getGlassProperty().notSolid());
		this.crystal = crystal;
		String name = (crystal ? "crystal_" : "") + "cooling_coil";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return new CoolingCoilTileEntity(!crystal);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state){
		return BlockRenderType.MODEL;
	}

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer(){
//		return BlockRenderLayer.TRANSLUCENT;
//	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(CRProperties.HORIZ_FACING);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		if(ESConfig.isWrench(playerIn.getHeldItem(hand))){
			if(!worldIn.isRemote){
				worldIn.setBlockState(pos, state.func_235896_a_(CRProperties.HORIZ_FACING));
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof CoolingCoilTileEntity){
					((CoolingCoilTileEntity) te).rotate();
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return getDefaultState().with(CRProperties.HORIZ_FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return state.get(CRProperties.HORIZ_FACING).getAxis() == Axis.X ? SHAPE_X : SHAPE_Z;
	}
}
