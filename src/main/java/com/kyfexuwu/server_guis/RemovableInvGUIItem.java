package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
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
            switch(actionType){
                case SWAP -> {
                    int slot=thisInv.type.slotCount+27+button;
                    if(button==40) break;//offhand
                    var playerStack = player.currentScreenHandler.getSlot(slot).getStack();
                    player.currentScreenHandler.setStackInSlot(slot,0,this.display);
                    this.display=playerStack;
                }
                case THROW -> {//done
                    var decAmt = button==0 ? 1 : this.display.getMaxCount();
                    var newCount = Math.max(0,this.display.getCount()-decAmt);
                    player.dropItem(this.display.copyWithCount(this.display.getCount()-newCount),true);
                    this.display.setCount(newCount);
                }
                case CLONE -> {//done
                    if(!player.isCreative()) break;

                    if(player.currentScreenHandler.getCursorStack().isEmpty()){
                        player.currentScreenHandler.setCursorStack(
                                this.display.copyWithCount(this.display.getMaxCount()));
                    }
                }
                case QUICK_CRAFT -> {
                    //dragging item
                }
                case PICKUP -> {//done
                    if(button==0){
                        var playerStack = player.currentScreenHandler.getCursorStack();
                        if(playerStack.isEmpty() || !playerStack.isOf(this.display.getItem())){
                            player.currentScreenHandler.setCursorStack(this.display);
                            this.display=playerStack;
                        }else{
                            var totalCount = this.display.getCount()+playerStack.getCount();
                            var thisCount=Math.min(playerStack.getMaxCount(),
                                    this.display.getCount()+playerStack.getCount());
                            this.display.setCount(thisCount);
                            playerStack.setCount(totalCount-thisCount);
                        }
                    }else if(button==1){
                        var playerStack = player.currentScreenHandler.getCursorStack();
                        if(playerStack.isEmpty()){
                            player.currentScreenHandler.setCursorStack(
                                    this.display.copyWithCount(this.display.getCount()-this.display.getCount()/2));
                            this.display.setCount(this.display.getCount()/2);
                        }else if(this.display.isEmpty()){
                            this.display=playerStack.copyWithCount(1);
                            playerStack.decrement(1);
                        }else if(playerStack.isOf(this.display.getItem())){
                            if(this.display.getItem().getMaxCount()>=this.display.getCount()+1){
                                this.display.setCount(this.display.getCount()+1);
                                playerStack.decrement(1);
                            }
                        }else{
                            player.currentScreenHandler.setCursorStack(this.display);
                            this.display=playerStack;
                        }
                    }
                }
            }

            System.out.println(button+", "+actionType.name());

            thisInv.getHandler().refresh();
        };
    }
}
