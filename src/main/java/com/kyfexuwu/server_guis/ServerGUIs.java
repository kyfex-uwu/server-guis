package com.kyfexuwu.server_guis;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGUIs implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Server GUIs");

	public static class ScreenType{
		private final ScreenHandlerType<?> type;
		private ScreenType(ScreenHandlerType<?> type){
			this.type=type;
		}
		public ScreenHandlerType<?> get(){ return this.type; }
		public static final ScreenType GENERIC_9X1 = new ScreenType(ScreenHandlerType.GENERIC_9X1);
		public static final ScreenType GENERIC_9X2 = new ScreenType(ScreenHandlerType.GENERIC_9X2);
		public static final ScreenType GENERIC_9X3 = new ScreenType(ScreenHandlerType.GENERIC_9X3);
		public static final ScreenType GENERIC_9X4 = new ScreenType(ScreenHandlerType.GENERIC_9X4);
		public static final ScreenType GENERIC_9X5 = new ScreenType(ScreenHandlerType.GENERIC_9X5);
		public static final ScreenType GENERIC_9X6 = new ScreenType(ScreenHandlerType.GENERIC_9X6);
		public static final ScreenType GENERIC_3X3 = new ScreenType(ScreenHandlerType.GENERIC_3X3);
		public static final ScreenType ANVIL = new ScreenType(ScreenHandlerType.ANVIL);
		public static final ScreenType BEACON = new ScreenType(ScreenHandlerType.BEACON);
		public static final ScreenType BLAST_FURNACE = new ScreenType(ScreenHandlerType.BLAST_FURNACE);
		public static final ScreenType BREWING_STAND = new ScreenType(ScreenHandlerType.BREWING_STAND);
		public static final ScreenType FURNACE = new ScreenType(ScreenHandlerType.FURNACE);
		public static final ScreenType GRINDSTONE = new ScreenType(ScreenHandlerType.GRINDSTONE);
		public static final ScreenType HOPPER = new ScreenType(ScreenHandlerType.HOPPER);
		public static final ScreenType LEGACY_SMITHING = new ScreenType(ScreenHandlerType.LEGACY_SMITHING);
		public static final ScreenType SMITHING = new ScreenType(ScreenHandlerType.SMITHING);
		public static final ScreenType SMOKER = new ScreenType(ScreenHandlerType.SMOKER);
		//try to add crafting (1)
		//try to add enchanting (2)
		//try to add loom and stonecutter (3)
		//cartography table look into (4)
	}

	@Override
	public void onInitialize() {
		if(MinecraftClient.class.getSimpleName().equals("MinecraftClient")) Testing.test();
		LOGGER.info("Server GUIs loaded!");
	}

	public static <T> ClickConsumer<T> nothingClick(){ return (_1,_2,_3,_4,_5,_6)->{}; }
	public static <T> CloseConsumer<T> nothingClose(){ return (_1,_2,_3)->{}; }

	public static InvGUIItem IMMOVABLE = new InvGUIItem() {
		private static final ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack().setCustomName(Text.of("§f"));
		private static final ClickConsumer<?> onClick = ServerGUIs.nothingClick();

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
