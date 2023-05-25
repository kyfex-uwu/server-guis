package com.kyfexuwu.server_guis;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGUIs implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Server GUIs");

	@Override
	public void onInitialize() {
		load();

		LOGGER.info("Server GUIs loaded!");
	}

	public void load(){
		InvGUI.register("screen1",new InvGUI.Template(ScreenHandlerType.GENERIC_9X3, Text.of("Screen 1"),
				InvGUIItem.decode(
						"#########"+
								"######o##"+
								"#########",
						new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE),
						new InvGUIItem.InvGuiEntry('o', new InvGUIItem(Blocks.REDSTONE_BLOCK, Text.of("Click me!"),
								(slotIndex, button, actionType, pPlayer, thisInv)->{
									InvGUI.get("screen2").build(pPlayer).open(pPlayer);
								})))));
		InvGUI.register("screen2",new InvGUI.Template(ScreenHandlerType.GENERIC_9X3, Text.of("Screen 2"),
				InvGUIItem.decode(
						"#########"+
								"##o######"+
								"#########",
						new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE),
						new InvGUIItem.InvGuiEntry('o', new InvGUIItem(Blocks.DIAMOND_BLOCK, Text.of("Now click me!"),
								(slotIndex, button, actionType, pPlayer, thisInv)->{
									InvGUI.get("screen3").build(pPlayer).open(pPlayer);
								})))));
		InvGUI.register("screen3",new InvGUI.Template(ScreenHandlerType.BEACON, Text.of("Screen 3"),
				InvGUIItem.decode(
						"##o",
						new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE),
						new InvGUIItem.InvGuiEntry('o', new RenderedInvGUIItem(
								(player)-> new ItemStack(Items.DIAMOND).setCustomName(player.getDisplayName()),
								(slotIndex, button, actionType, pPlayer, thisInv)->{
									InvGUI.get("screen1").build(pPlayer).open(pPlayer);
								}))
				)));
	}
}
