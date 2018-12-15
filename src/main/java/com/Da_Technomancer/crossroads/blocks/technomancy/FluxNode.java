package com.Da_Technomancer.crossroads.blocks.technomancy;

import com.Da_Technomancer.crossroads.blocks.ModBlocks;
import com.Da_Technomancer.crossroads.items.ModItems;
import com.Da_Technomancer.crossroads.tileentities.technomancy.FluxNodeTileEntity;
import com.Da_Technomancer.essentials.blocks.EssentialsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FluxNode extends BlockContainer{

	public FluxNode(){
		super(Material.IRON);
		String name = "flux_node";
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(ModItems.TAB_CROSSROADS);
		setHardness(3);
		setSoundType(SoundType.METAL);
		ModBlocks.toRegister.add(this);
		ModBlocks.blockAddQue(this);
		setDefaultState(getDefaultState().withProperty(EssentialsProperties.REDSTONE_BOOL, false));
	}

	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, EssentialsProperties.REDSTONE_BOOL);
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(EssentialsProperties.REDSTONE_BOOL, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(EssentialsProperties.REDSTONE_BOOL) ? 1 : 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new FluxNodeTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		if(worldIn.isBlockPowered(pos)){
			if(!state.getValue(EssentialsProperties.REDSTONE_BOOL)){
				worldIn.setBlockState(pos, state.withProperty(EssentialsProperties.REDSTONE_BOOL, true));
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof FluxNodeTileEntity){
					((FluxNodeTileEntity) te).updateRedstone(true);
				}
			}
		}else{
			if(state.getValue(EssentialsProperties.REDSTONE_BOOL)){
				worldIn.setBlockState(pos, state.withProperty(EssentialsProperties.REDSTONE_BOOL, false));
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof FluxNodeTileEntity){
					((FluxNodeTileEntity) te).updateRedstone(false);
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
		return face == EnumFacing.UP ? BlockFaceShape.CENTER_SMALL : BlockFaceShape.UNDEFINED;
	}
}
