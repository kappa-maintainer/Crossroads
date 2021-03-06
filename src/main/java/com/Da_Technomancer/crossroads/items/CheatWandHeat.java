package com.Da_Technomancer.crossroads.items;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.heat.IHeatHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;

public class CheatWandHeat extends Item{

	private static final int RATE = 100;

	protected CheatWandHeat(){
		super(new Properties().group(CRItems.TAB_CROSSROADS).maxStackSize(1));
		String name = "cheat_wand_heat";
		setRegistryName(name);
		CRItems.toRegister.add(this);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context){
		TileEntity te = context.getWorld().getTileEntity(context.getPos());
		LazyOptional<IHeatHandler> heatOpt;
		if(te != null && (heatOpt = te.getCapability(Capabilities.HEAT_CAPABILITY, null)).isPresent()){
			IHeatHandler cable = heatOpt.orElseThrow(NullPointerException::new);
			if(context.getPlayer() != null && context.getPlayer().isSneaking()){
				cable.addHeat(-RATE);
			}else{
				cable.addHeat(RATE);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add(new TranslationTextComponent("tt.crossroads.boilerplate.creative"));
		tooltip.add(new TranslationTextComponent("tt.crossroads.cheat_heat.desc", RATE));
		tooltip.add(new TranslationTextComponent("tt.crossroads.cheat_heat.cold", RATE));
	}
}
