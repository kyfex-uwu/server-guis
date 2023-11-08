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
		BEACON(ScreenHandlerType.BEACON,1, 3),
		BLAST_FURNACE(ScreenHandlerType.BLAST_FURNACE,3, 4),
		BREWING_STAND(ScreenHandlerType.BREWING_STAND,5, 2),
		CARTOGRAPHY_TABLE(ScreenHandlerType.CARTOGRAPHY_TABLE, 3),
		CRAFTING(ScreenHandlerType.CRAFTING, 10),
		ENCHANTMENT(ScreenHandlerType.ENCHANTMENT, 2, 10),
		FURNACE(ScreenHandlerType.FURNACE,3, 4),
		GRINDSTONE(ScreenHandlerType.GRINDSTONE,3),
		HOPPER(ScreenHandlerType.HOPPER,5),
		LECTERN(ScreenHandlerType.LECTERN, 1, 1), //for books
		//LEGACY_SMITHING(ScreenHandlerType.LEGACY_SMITHING), //pre 1.19? maybe?
		LOOM(ScreenHandlerType.LOOM, 4, 1),//make sure banner slot is banner, and dye slot is dye
		//MERCHANT(ScreenHandlerType.MERCHANT, 3),
		SMITHING(ScreenHandlerType.SMITHING,4),
		SMOKER(ScreenHandlerType.SMOKER,3, 4),
		STONECUTTER(ScreenHandlerType.STONECUTTER, 2, 1);

		//book (edit?)
		//command block
		//creative inventory
		//sign edit
		//horse
		//inventory?
		//jigsaw
		//structure block

		public final ScreenHandlerType<?> type;
		public final int slotCount;
		public final int propCount;
		ScreenType(ScreenHandlerType<?> type, int slotCount){
			this(type, slotCount, 0);
		}
		ScreenType(ScreenHandlerType<?> type, int slotCount, int propCount){
			this.type=type;
			this.slotCount=slotCount;
			this.propCount=propCount;
		}
	}

	@Override
	public void onInitializeServer() {
		//if(MinecraftServer.class.getSimpleName().equals("MinecraftServer")) Testing.test();
		LOGGER.info("Server GUIs loaded!");
	}

	public static ClickConsumer<?> nothingClick(){ return (_1, _2, _3, _4, _5, _6)->{}; }
	private static final ClickConsumer<?> nothingClickInst = nothingClick();
	public static final InvGUIItem IMMOVABLE = new InvGUIItem() {
		private static final ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack().setCustomName(Text.of("§f"));

		@Override
		public ItemStack getItem(ServerPlayerEntity player, InvGUI<?> gui, Object argument) { return item; }
		@Override
		public ClickConsumer<?> onClick() { return nothingClickInst; }
	};
	public static final InvGUIItem EMPTY = new InvGUIItem() {
		@Override
		public ItemStack getItem(ServerPlayerEntity player, InvGUI<?> gui, Object argument) {
			return ItemStack.EMPTY;
		}

		@Override
		public ClickConsumer<?> onClick() { return nothingClickInst; }
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
