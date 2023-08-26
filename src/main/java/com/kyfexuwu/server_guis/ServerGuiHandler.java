package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerGuiHandler extends ScreenHandler {

    public final InvGUI<?> gui;
    public final Inventory inventory;
    public final ServerGUIs.ScreenType type;
    final ServerPlayerEntity player;
    final Object argument;

    public ServerGuiHandler(int syncId, PlayerInventory playerInventory, ServerGUIs.ScreenType type, InvGUI<?> gui, Object argument) {
        super(type.type, syncId);
        this.type=type;

        this.consumers = new ClickConsumer[type.slotCount];

        this.argument = argument;

        this.inventory = new SimpleInventory(type.slotCount +36);
        this.inventory.onOpen(playerInventory.player);

        this.player = (ServerPlayerEntity) playerInventory.player;
        for(int i = 0; i < type.slotCount; i++){
            this.addSlot(new Slot(this.inventory, i, 0, 0));
            this.putInvGUIItem(i, gui.items[i]);
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


    @Override
    public ItemStack quickMove(PlayerEntity player1, int slot) {
        return this.gui.onShiftClick(slot);
    }

    @Override
    public boolean canUse(PlayerEntity player1) { return true; }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if(slotIndex>=0&&slotIndex<this.inventory.size()-36){
            if(this.consumers[slotIndex]!=null){
                this.consumers[slotIndex].consume(slotIndex, button, actionType, (ServerPlayerEntity) player, this.gui, appeaseCompiler(this.argument));
            }
            return;
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    private boolean closeQuietly=false;
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if(!this.closeQuietly) {
            this.gui.onClose();
        }
    }
    private final ClickConsumer<?>[] consumers;
    public void putInvGUIItem(int slot, InvGUIItem item){
        this.setStackInSlot(slot, 0, item.getItem(this.player, this.argument));
        this.consumers[slot]=item.onClick();
    }

    public void refresh(){
        for(int i = 0; i < this.inventory.size()-36; i++){
            var item1 = this.gui.items[i].getItem(this.player, this.argument);
            var item2 = this.inventory.getStack(i);

            if(!ItemStack.areItemsEqual(item1,item2) || (
                    item1.hasNbt()?
                            !item1.getNbt().equals(item2.getNbt()):
                            !item2.hasNbt())) {
                this.setStackInSlot(i, 0, item1);
            }
        }
    }

    public void closeQuietly(){
        this.closeQuietly=true;
        this.player.closeHandledScreen();
    }

    static <T> T appeaseCompiler(Object toConvert){
        try {
            return (T) toConvert;
        }catch(ClassCastException e){
            return null;
        }
    }
}
