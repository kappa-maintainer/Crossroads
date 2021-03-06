package com.Da_Technomancer.crossroads.blocks.beams;

import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.gui.container.ColorChartContainer;
import com.Da_Technomancer.essentials.blocks.ESProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ColorChart extends Block{

	private static final VoxelShape[] SHAPES = new VoxelShape[6];

	static{
		SHAPES[0] = makeCuboidShape(0, 15, 0, 16, 16, 16);;
		SHAPES[1] = makeCuboidShape(0, 0, 0, 16, 1, 16);;
		SHAPES[2] = makeCuboidShape(0, 0, 15, 16, 16, 16);
		SHAPES[3] = makeCuboidShape(0, 0, 0, 16, 16, 1);
		SHAPES[4] = makeCuboidShape(15, 0, 0, 16, 16, 16);
		SHAPES[5] = makeCuboidShape(0, 0, 0, 1, 16, 16);
	}

	public ColorChart(){
		super(Properties.create(Material.WOOD).hardnessAndResistance(3));
		String name = "color_chart";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			NetworkHooks.openGui((ServerPlayerEntity) playerIn, new INamedContainerProvider(){
				@Override
				public ITextComponent getDisplayName(){
					return new TranslationTextComponent("container.color_chart");
				}

				@Override
				public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity){
					return new ColorChartContainer(i, playerInventory, null);
				}
			});
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return getDefaultState().with(ESProperties.FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return SHAPES[state.get(ESProperties.FACING).getIndex()];
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(ESProperties.FACING);
	}
}
