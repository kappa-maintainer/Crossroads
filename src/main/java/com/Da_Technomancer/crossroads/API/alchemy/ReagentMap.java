package com.Da_Technomancer.crossroads.API.alchemy;

import com.Da_Technomancer.crossroads.API.heat.HeatUtil;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores Alchemy reagents, along with temperature. Avoid using HashMap methods that aren't overwritten to minimize unintended behaviour
 */
public class ReagentMap extends HashMap<String, Integer>{

	private double heat;
	private int totalQty;

	public ReagentMap(){
		super(AlchemyCore.REAGENT_COUNT);
	}

	public void transferReagent(String id, int amount, ReagentMap srcMap){
		amount = Math.min(amount, srcMap.getQty(id));

		addReagent(id, amount, srcMap.getTempC());
		srcMap.removeReagent(id, amount);
	}

	public void transferReagent(IReagent reag, int amount, ReagentMap srcMap){
		transferReagent(reag.getId(), amount, srcMap);
	}

	public int addReagent(String id, int amount, double srcTemp){
		if(id == null){
			return 0;
		}

		int current = getQty(id);
		current += amount;
		if(current < 0){
			current = 0;
		}
		heat += HeatUtil.toKelvin(srcTemp) * amount;
		put(id, current);
		return current;
	}

	public int addReagent(IReagent reag, int amount, double srcTemp){
		return addReagent(reag.getId(), amount, srcTemp);
	}

	public int removeReagent(String id, int amount){
		if(id == null){
			return 0;
		}

		int current = getQty(id);
		if(amount > current){
			amount = current;
		}
		heat -= getTempK() * amount;
		current -= amount;
		put(id, current);
		if(heat < 0){
			heat = 0;
		}
		if(totalQty <= 0){
			heat = 0;
			totalQty = 0;
		}
		return current;
	}

	public int removeReagent(IReagent reag, int amount){
		return removeReagent(reag.getId(), amount);
	}

	public ReagentStack getStack(String id){
		return new ReagentStack(id, getQty(id));
	}

	public ReagentStack getStack(IReagent reag){
		return new ReagentStack(reag, getQty(reag));
	}

	public void setTemp(double tempC){
		heat = totalQty * HeatUtil.toKelvin(tempC);
	}

	@Override
	public Integer get(Object key){
		if(key instanceof IReagent){
			key = ((IReagent) key).getId();
		}
		return super.get(key);
	}

	@Override
	public Integer put(String key, Integer value){
		if(key == null || value == null || value < 0){
			return 0;
		}
		totalQty -= getQty(key);
		totalQty += value;
		return super.put(key, value);
	}

	public Integer put(IReagent key, Integer value){
		return put(key.getId(), value);
	}

	@Override
	public Integer remove(Object key){
		if(key instanceof IReagent){
			key = ((IReagent) key).getId();
		}
		Integer qty = super.remove(key);
		if(qty != null){
			heat -= getTempK() * qty;
			totalQty -= qty;
		}
		return qty;
	}

	@Override
	public void clear(){
		totalQty = 0;
		heat = 0;
		super.clear();
	}

	@Override
	public boolean isEmpty(){
		return totalQty == 0;
	}

	public int getQty(String id){
		Integer raw = get(id);
		return raw == null ? 0 : raw;
	}

	public int getQty(IReagent reag){
		return getQty(reag.getId());
	}

	public int getTotalQty(){
		return totalQty;
	}

	public double getHeat(){
		return heat;
	}

	public double getTempC(){
		return HeatUtil.toCelcius(getTempK());
	}

	public double getTempK(){
		return totalQty == 0 ? 0 : heat / totalQty;
	}

	public void refresh(){
		totalQty = 0;
		for(Integer qty : values()){
			totalQty += qty;
		}
	}

	public CompoundNBT write(CompoundNBT nbt){
		nbt.putDouble("he", heat);
		for(String key : keySet()){
			int qty = get(key);
			if(qty > 0){
				nbt.putInt("qty_" + key, qty);
			}
		}

		return nbt;
	}

	public static ReagentMap readFromNBT(CompoundNBT nbt){
		ReagentMap map = new ReagentMap();
		map.heat = nbt.getDouble("he");
		for(String key : nbt.keySet()){
			if(!key.startsWith("qty_")){
				continue;
			}
			map.put(key.substring(4), nbt.getInt(key));
		}

		return map;
	}

	@Override
	public boolean containsKey(Object key){
		if(key instanceof IReagent){
			key = ((IReagent) key).getId();
		}
		return super.containsKey(key);
	}

	@Deprecated
	public Set<IReagent> keySetReag(){
		return keySet().stream().map(AlchemyCore::getReagent).filter(Objects::nonNull).collect(Collectors.toSet());
	}
}
