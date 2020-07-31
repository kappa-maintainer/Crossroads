package com.Da_Technomancer.crossroads.items;

import com.Da_Technomancer.crossroads.Main;

import net.minecraft.item.ItemFood;

public class MashedPotato extends ItemFood{
	
	public MashedPotato(){
		super(5, .3F, true);
		String name = "mashed_potato";
		setUnlocalizedName(Main.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(ModItems.TAB_CROSSROADS);
		ModItems.toRegister.add(this);
		ModItems.itemAddQue(this);
	}
}
