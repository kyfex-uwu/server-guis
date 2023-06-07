package com.kyfexuwu.server_guis;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class RemovableInvGUIItem implements InvGUIItem{
    public static void giveBackRemovableItems(ServerPlayerEntity player, InvGUI<?> invGUI){
        for(InvGUIItem item : invGUI.items){
            if(item instanceof RemovableInvGUIItem){
                player.giveItemStack(((RemovableInvGUIItem) item).display);
            }
        }
    }
    public ItemStack display = Items.AIR.getDefaultStack();
    @Override
    public ItemStack getItem(ServerPlayerEntity player, Object argument) { return this.display; }

    @Override
    public ClickConsumer<?> onClick() {
        return (slotIndex, button, actionType, player, thisInv, argument) -> {
            //todo qwq
            var temp = player.currentScreenHandler.getCursorStack();
            player.currentScreenHandler.setCursorStack(this.display);
            this.display = temp;

            thisInv.getHandler().refresh();
        };
    }
}
