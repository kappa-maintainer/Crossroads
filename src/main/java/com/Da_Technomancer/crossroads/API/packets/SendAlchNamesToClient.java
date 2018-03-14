package com.Da_Technomancer.crossroads.API.packets;

import com.Da_Technomancer.crossroads.API.alchemy.AlchemyCore;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("serial")
public class SendAlchNamesToClient extends Message<SendAlchNamesToClient>{

	public SendAlchNamesToClient(){
		
	}

	public String[] names;
	public boolean fullReset;
	public long seed;
	
	public SendAlchNamesToClient(String[] names, boolean fullReset, long seed){
		this.names = names;
		this.fullReset = fullReset;
		this.seed = seed;
	}

	@Override
	public IMessage handleMessage(MessageContext context){
		if(context.side != Side.CLIENT){
			System.err.println("MessageToClient received on wrong side:" + context.side);
			return null;
		}
		if(names == null){
			System.err.println("AlchNamesSync received null array");
			return null;
		}

		Minecraft minecraft = Minecraft.getMinecraft();
		minecraft.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				processMessage(names, fullReset, seed);
			}
		});

		return null;
	}

	public void processMessage(String[] names, boolean fullReset, long seed){
		for(int i = 0; i < names.length; i++){
			AlchemyCore.CUST_REAG_NAMES[i] = names[i];
		}
		
		if(fullReset){
			AlchemyCore.setup(null, seed);
		}
	}
}
