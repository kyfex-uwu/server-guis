package com.kyfexuwu.server_guis;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGUIs implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Server GUIs");

	@Override
	public void onInitialize() {
		//Testing.test();
		LOGGER.info("Server GUIs loaded!");
	}

	public static <T> ClickConsumer<T> nothingClick(){ return (_1,_2,_3,_4,_5,_6)->{}; }
	public static <T> CloseConsumer<T> nothingClose(){ return (_1,_2,_3)->{}; }

	public static InvGUIItem IMMOVABLE = new InvGUIItem() {
		private static ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack().setCustomName(Text.of("§f"));
		private static ClickConsumer<?> onClick = ServerGUIs.nothingClick();

		@Override
		public ItemStack getItem(ServerPlayerEntity player, Object argument) { return item; }
		@Override
		public ClickConsumer<?> onClick() { return onClick; }
	};

	public static ItemStack getPlayerHead(int i1, int i2, int i3, int i4, String textureString){
		var val = new NbtCompound();
		val.putString("Value", textureString);
		var textures = new NbtList();
		textures.add(0, val);
		var props = new NbtCompound();
		props.put("textures", textures);

		var nbt = new NbtCompound();
		nbt.put("Id",new NbtIntArray(new int[]{i1, i2, i3, i4}));
		nbt.put("Properties",props);

		var finalCompound = new NbtCompound();
		finalCompound.put("SkullOwner",nbt);

		var head = new ItemStack(Blocks.PLAYER_HEAD);
		head.setNbt(finalCompound);
		return head;
	}
	public static ItemStack getPlayerHead(String username){
		var finalCompound = new NbtCompound();
		finalCompound.put("SkullOwner", NbtString.of(username));

		var head = new ItemStack(Blocks.PLAYER_HEAD);
		head.setNbt(finalCompound);
		return head;
	}
}
