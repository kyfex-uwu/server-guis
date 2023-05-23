package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;

public class ServerGuiHandler extends GenericContainerScreenHandler {
    public final InvGUI gui;
    public ServerGuiHandler(int syncId, PlayerInventory playerInventory, InvGUI gui) {
        super(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, new SimpleInventory(9*3), 3);
        this.gui=gui;
    }

    private final ClickConsumer[] consumers = new ClickConsumer[27];
    public void putInvGUIItem(int slot, InvGUIItem item){
        this.setStackInSlot(slot, 0, item.display);
        this.consumers[slot]=item.onClick;
    }

    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if(slotIndex>=0&&slotIndex<this.getInventory().size()){
            if(consumers[slotIndex]!=null){
                consumers[slotIndex].consume(slotIndex, button, actionType, player, this.gui);
            }
            return;
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }
}
