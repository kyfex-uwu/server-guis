package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

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

	private static void checkGUIType(InvGUI<?> gui, String errorMessage, ScreenType... types){
		for(var type : types){
			if(gui.type==type) return;
		}
		throw new IllegalArgumentException(errorMessage);
	}

	public static void setBeaconLevel(InvGUI<?> gui, int level){
		checkGUIType(gui, "Tried to set beacon level of gui "+gui+", but the gui is not a beacon gui!",
				ScreenType.BEACON);

		gui.propertyDelegate.set(0,level);
	}
	public static void setBeaconEffects(InvGUI<?> gui, Optional<StatusEffect> effect1, Optional<StatusEffect> effect2){
		checkGUIType(gui, "Tried to set status effects of gui "+gui+", but the gui is not a beacon gui!",
				ScreenType.BEACON);

		effect1.ifPresent(statusEffect ->
				gui.propertyDelegate.set(1, StatusEffect.getRawId(statusEffect)));
		effect2.ifPresent(statusEffect ->
				gui.propertyDelegate.set(2, StatusEffect.getRawId(statusEffect)));
	}

	public static void setFurnaceCookProgress(InvGUI<?> gui, double cookProgress){
		checkGUIType(gui, "Tried to set cook data of gui "+gui+", but the gui is not a furnace type gui!",
				ScreenType.FURNACE, ScreenType.BLAST_FURNACE, ScreenType.SMOKER);

		if(cookProgress<0) cookProgress=0;
		else if(cookProgress>1) cookProgress=1;
		gui.propertyDelegate.set(2, (int) (1000*cookProgress));
	}
	public static void setFurnaceFuelProgress(InvGUI<?> gui, double fuelProgress){
		checkGUIType(gui, "Tried to set fuel data of gui "+gui+", but the gui is not a furnace type gui!",
				ScreenType.FURNACE, ScreenType.BLAST_FURNACE, ScreenType.SMOKER);

		if(fuelProgress<0) fuelProgress=0;
		else if(fuelProgress>1) fuelProgress=1;
		gui.propertyDelegate.set(0, (int) (1000*fuelProgress));
	}

	public static void setBrewingFuel(InvGUI<?> gui, int fuelAmt){
		checkGUIType(gui, "Tried to set fuel of gui "+gui+", but the gui is not a brewing gui!",
				ScreenType.BREWING_STAND);

		gui.propertyDelegate.set(1, fuelAmt);
	}
	public static void setBrewingTime(InvGUI<?> gui, int time){
		checkGUIType(gui, "Tried to set time of gui "+gui+", but the gui is not a brewing gui!",
				ScreenType.BREWING_STAND);

		gui.propertyDelegate.set(0, time);
	}

	public static void setBannerPattern(InvGUI<?> gui, int pattern){
		checkGUIType(gui, "Tried to set pattern of gui "+gui+", but the gui is not a loom gui!",
				ScreenType.LOOM);

		gui.propertyDelegate.set(0, pattern);
	}

	public static boolean handleBookPageTurning(InvGUI<?> gui, int buttonIndex){
		checkGUIType(gui, "Tried to handle book pages of gui "+gui+", but the gui is not a lectern gui!",
				ScreenType.LECTERN);

		if(buttonIndex==1||buttonIndex==2){
			gui.propertyDelegate.set(0, gui.propertyDelegate.get(0)-3+buttonIndex*2);
			return true;
		}
		return false;
	}
	public static void setBookPage(InvGUI<?> gui, int pageIndex){
		checkGUIType(gui, "Tried to set page of gui "+gui+", but the gui is not a lectern gui!",
				ScreenType.LECTERN);

		gui.propertyDelegate.set(0, pageIndex);
	}

	public static class EnchantmentData{
		public int id;
		public int level;
		public int xpAmt;

		private static final Random random = Random.create();
		private static int calcRandomExperience(int slot){
			return Math.min(30, Math.max(1,
					(random.nextInt(8) + random.nextInt(16) + 8)*(slot+1)/3)); //clamps between 1 and 30
		}

		public EnchantmentData(int id, int level, int xpAmt){
			this.id=id;
			this.level=level;
			this.xpAmt=xpAmt;
		}

		public static class EnchantmentDataBuilder{
			private int id=-1;
			private int level=1;
			private int xpAmt=-1;
			private EnchantmentDataBuilder(){}

			public EnchantmentDataBuilder setEnchantmentId(int id){
				if(id<0) return this;

				this.id=id;
				return this;
			}
			public EnchantmentDataBuilder setEnchantment(Enchantment enchantment){
				this.id=Registries.ENCHANTMENT.getRawId(enchantment);
				return this;
			}

			public EnchantmentDataBuilder setLevel(int level){
				if(level<1) return this;

				this.level=level;
				return this;
			}

			public EnchantmentDataBuilder setXPAmt(int amt){
				if(amt<0) return this;

				this.xpAmt=amt;
				return this;
			}
			public EnchantmentDataBuilder setRandomXPAmt(int slot){
				this.xpAmt=calcRandomExperience(slot);
				return this;
			}

			public EnchantmentData build(){
				if(this.xpAmt==-1) this.xpAmt = calcRandomExperience(0);
				return new EnchantmentData(this.id, this.level, this.xpAmt);
			}
		}
		public static EnchantmentDataBuilder builder(){
			return new EnchantmentDataBuilder();
		}
	}
	public static void setEnchatingTableEnchantments(InvGUI<?> gui,
		Optional<EnchantmentData> ench1, Optional<EnchantmentData> ench2, Optional<EnchantmentData> ench3){
		checkGUIType(gui, "Tried to set enchanting data of gui "+gui+", but the gui is not an enchanting table gui!",
				ScreenType.ENCHANTMENT);

		ench1.ifPresent(data->{
			gui.propertyDelegate.set(0, data.xpAmt);
			gui.propertyDelegate.set(4, data.id);
			gui.propertyDelegate.set(7, data.level);
		});
		ench2.ifPresent(data->{
			gui.propertyDelegate.set(1, data.xpAmt);
			gui.propertyDelegate.set(5, data.id);
			gui.propertyDelegate.set(8, data.level);
		});
		ench3.ifPresent(data->{
			gui.propertyDelegate.set(2, data.xpAmt);
			gui.propertyDelegate.set(6, data.id);
			gui.propertyDelegate.set(9, data.level);
		});
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
