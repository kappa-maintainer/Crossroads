package com.Da_Technomancer.crossroads.blocks.heat;

import java.util.List;

import javax.annotation.Nullable;

import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.CrossroadsBlocks;
import com.Da_Technomancer.crossroads.gui.GuiHandler;
import com.Da_Technomancer.crossroads.items.CrossroadsItems;
import com.Da_Technomancer.crossroads.tileentities.heat.SmelterTileEntity;

import net.minecraft.block.*;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Smelter extends ContainerBlock{

	public Smelter(){
		super(Material.IRON);
		String name = "heating_chamber";
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CrossroadsItems.TAB_CROSSROADS);
		setHardness(3);
		setSoundType(SoundType.METAL);
		CrossroadsBlocks.toRegister.add(this);
		CrossroadsBlocks.blockAddQue(this);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			playerIn.openGui(Crossroads.instance, GuiHandler.SMELTER_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state){
		return BlockRenderType.MODEL;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		TileEntity te = world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return new SmelterTileEntity();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		for(int i = 0; i < SmelterTileEntity.TEMP_TIERS.length; i++){
			tooltip.add((i + 1) + "x speed: -" + SmelterTileEntity.USAGE * (i + 1) + "°C/t when above " + SmelterTileEntity.TEMP_TIERS[i] + "°C");
		}
	}
}
