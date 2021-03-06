package com.Da_Technomancer.crossroads.API.effects.alchemy;

import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

public class LumenEffect extends AetherEffect{

	@Override
	protected Block soilBlock(){
		return Blocks.SAND;
	}

	@Override
	protected Block rockBlock(){
		return Blocks.SANDSTONE;
	}

	@Override
	protected Block crystalBlock(){
		return CRBlocks.blockSalt;
	}

	@Override
	protected Block fluidBlock(){
		return Blocks.AIR;
	}

	@Override
	protected Block woodBlock(){
		return Blocks.BONE_BLOCK;
	}

	@Override
	protected Block foliageBlock(){
		return Blocks.AIR;
	}

	@Override
	protected RegistryKey<Biome> biome(){
		return Biomes.DESERT;
	}

	@Override
	public ITextComponent getName(){
		return new TranslationTextComponent("effect.terraform_desert");
	}
}
