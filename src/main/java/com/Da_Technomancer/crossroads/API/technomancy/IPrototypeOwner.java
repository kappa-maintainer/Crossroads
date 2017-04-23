package com.Da_Technomancer.crossroads.API.technomancy;

import javax.annotation.Nonnull;

import com.Da_Technomancer.crossroads.API.enums.PrototypePortTypes;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPrototypeOwner{
	
	public boolean hasCap(Capability<?> cap, @Nonnull EnumFacing side);

	/**
	 * This may crash if there is no tile entity adjacent. 
	 * This is fine, as all callers should first check hasCap(Capability<?> cap, EnumFacing side), which doesn't crash.
	 */
	public <T> T getCap(Capability<T> cap, @Nonnull EnumFacing side);
	
	/**
	 * Used to send block updates through the owner, for redstone.
	 * @param fromSide Side of the port calling this.
	 * @param blockIn The port block.
	 */
	public void neighborChanged(@Nonnull EnumFacing fromSide, Block blockIn);
	
	/**
	 * Called once every 20 ticks for all IPrototypeOwners that would be loaded. 
	 * If this IPrototypeOwner needs to prematurely stop itself from loading, it can remove itself from the main map.
	 * @return True if this is no longer valid.
	 */
	public boolean loadTick();
	
	/**
	 * Returns the prototype port types for each side. This cannot be used to fetch capabilities, it mainly exists for rendering purposes.
	 */
	@SideOnly(Side.CLIENT)
	public default PrototypePortTypes[] getTypes(){
		return new PrototypePortTypes[6];
	}
}
