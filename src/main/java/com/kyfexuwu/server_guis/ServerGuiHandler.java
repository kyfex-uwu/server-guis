package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashMap;

public class ServerGuiHandler extends ScreenHandler {
    public static final HashMap<ScreenHandlerType<?>, Integer> invSlotAmt = new HashMap<>();
    static{
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X1,9);
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X2,18);
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X3,27);
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X4,36);
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X5,45);
        invSlotAmt.put(ScreenHandlerType.GENERIC_9X6,54);
        invSlotAmt.put(ScreenHandlerType.GENERIC_3X3,9);
        invSlotAmt.put(ScreenHandlerType.ANVIL,3);
        invSlotAmt.put(ScreenHandlerType.BEACON,1);
        invSlotAmt.put(ScreenHandlerType.BLAST_FURNACE,3);
        invSlotAmt.put(ScreenHandlerType.BREWING_STAND,5);
        invSlotAmt.put(ScreenHandlerType.CRAFTING,10);
        invSlotAmt.put(ScreenHandlerType.ENCHANTMENT,2);
        invSlotAmt.put(ScreenHandlerType.FURNACE,3);
        invSlotAmt.put(ScreenHandlerType.GRINDSTONE,3);
        invSlotAmt.put(ScreenHandlerType.HOPPER,5);
        invSlotAmt.put(ScreenHandlerType.LECTERN,1);//book
        invSlotAmt.put(ScreenHandlerType.LOOM,4);
        invSlotAmt.put(ScreenHandlerType.MERCHANT,3);//villager with trades
        invSlotAmt.put(ScreenHandlerType.SHULKER_BOX,27);
        invSlotAmt.put(ScreenHandlerType.LEGACY_SMITHING,3);//pre 1.20
        invSlotAmt.put(ScreenHandlerType.SMITHING,4);//post 1.20
        invSlotAmt.put(ScreenHandlerType.SMOKER,3);
        invSlotAmt.put(ScreenHandlerType.CARTOGRAPHY_TABLE,3);
        invSlotAmt.put(ScreenHandlerType.STONECUTTER,2);
    }

    public final InvGUI gui;
    public final Inventory inventory;
    private final PlayerEntity player;

    public ServerGuiHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerType<?> type, InvGUI gui) {
        super(type, syncId);

        int invSize = invSlotAmt.get(type);
        this.consumers = new ClickConsumer[invSize];

        this.inventory = new SimpleInventory(invSize +36);
        this.inventory.onOpen(playerInventory.player);

        this.player = playerInventory.player;
        for(int i = 0; i< invSize; i++){
            this.addSlot(new Slot(this.inventory, i, 0, 0));
            putInvGUIItem(i, gui.items[i]);
        }

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x+y*9+9, 0, 0));
            }
        }
        for(int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 0, 0));
        }

        this.gui=gui;
    }

    private final ClickConsumer[] consumers;
    public void putInvGUIItem(int slot, InvGUIItem item){
        this.setStackInSlot(slot, 0, item.display(this.player));
        this.consumers[slot]=item.onClick;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player1, int slot) { return ItemStack.EMPTY; }

    @Override
    public boolean canUse(PlayerEntity player1) { return true; }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if(slotIndex>=0&&slotIndex<this.inventory.size()-36){
            if(consumers[slotIndex]!=null){
                consumers[slotIndex].consume(slotIndex, button, actionType, player, this.gui);
            }
            return;
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }
}
