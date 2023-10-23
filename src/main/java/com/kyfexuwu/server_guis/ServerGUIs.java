package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGUIs implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Server GUIs");

	public enum ScreenType{
		GENERIC_9X1(ScreenHandlerType.GENERIC_9X1,9),
		GENERIC_9X2(ScreenHandlerType.GENERIC_9X2,18),
		GENERIC_9X3(ScreenHandlerType.GENERIC_9X3,27),
		GENERIC_9X4(ScreenHandlerType.GENERIC_9X4,36),
		GENERIC_9X5(ScreenHandlerType.GENERIC_9X5,45),
		GENERIC_9X6(ScreenHandlerType.GENERIC_9X6,54),
		GENERIC_3X3(ScreenHandlerType.GENERIC_3X3,9),
		ANVIL(ScreenHandlerType.ANVIL,3),
		BEACON(ScreenHandlerType.BEACON,1),
		BLAST_FURNACE(ScreenHandlerType.BLAST_FURNACE,3),
		BREWING_STAND(ScreenHandlerType.BREWING_STAND,5),
		FURNACE(ScreenHandlerType.FURNACE,3),
		GRINDSTONE(ScreenHandlerType.GRINDSTONE,3),
		HOPPER(ScreenHandlerType.HOPPER,5),
		//LEGACY_SMITHING(ScreenHandlerType.LEGACY_SMITHING),
		SMITHING(ScreenHandlerType.SMITHING,4),
		SMOKER(ScreenHandlerType.SMOKER,3);
		//try to add crafting (1)
		//try to add enchanting (2)
		//try to add loom and stonecutter (3)
		//cartography table look into (4)

		public final ScreenHandlerType<?> type;
		public final int slotCount;
		ScreenType(ScreenHandlerType<?> type, int slotCount){
			this.type=type;
			this.slotCount=slotCount;
		}
	}

	@Override
	public void onInitializeServer() {
		if(MinecraftServer.class.getSimpleName().equals("MinecraftServer")) Testing.test();
		LOGGER.info("Server GUIs loaded!");
	}

	public static <T> ClickConsumer<T> nothingClick(){ return (_1, _2, _3, _4, _5, _6)->{}; }

	public static final InvGUIItem IMMOVABLE = new InvGUIItem() {
		private static final ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack().setCustomName(Text.of("§f"));
		private static final ClickConsumer<?> onClick = ServerGUIs.nothingClick();

		@Override
		public ItemStack getItem(ServerPlayerEntity player, Object argument) { return item; }
		@Override
		public ClickConsumer<?> onClick() { return onClick; }
	};
	public static final InvGUIItem EMPTY = new InvGUIItem() {
		@Override
		public ItemStack getItem(ServerPlayerEntity player, Object argument) {
			return ItemStack.EMPTY;
		}

		@Override
		public ClickConsumer<?> onClick() {
			return ServerGUIs.nothingClick();
		}
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
