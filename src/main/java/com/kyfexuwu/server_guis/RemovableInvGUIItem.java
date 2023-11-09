package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class RemovableInvGUIItem implements InvGUIItem{
    public ItemStack display = Items.AIR.getDefaultStack();

    public final boolean canRemove;
    public final boolean canInsert;
    public RemovableInvGUIItem(boolean canRemove, boolean canInsert){
        this.canRemove=canRemove;
        this.canInsert=canInsert;
    }
    public RemovableInvGUIItem(){
        this(true, true);
    }

    @Override
    public ItemStack getItem(ServerPlayerEntity player, InvGUI<?> gui, Object argument) { return this.display; }

    @Override
    public ClickConsumer<?> onClick() {
        return (slotIndex, button, actionType, player, thisInv, argument) -> {
            switch(actionType){
                case SWAP -> {
                    int slot=thisInv.type.slotCount+27+button;
                    if(!this.display.isEmpty()&&!this.canRemove) break;

                    if(button==40){
                        var playerStack = player.getOffHandStack();
                        if(!playerStack.isEmpty()&&!this.canInsert) break;

                        player.getInventory().offHand.set(0,this.display);
                        this.display=playerStack;
                    }else{
                        var playerStack = player.currentScreenHandler.getSlot(slot).getStack();
                        if(!playerStack.isEmpty()&&!this.canInsert) break;

                        player.currentScreenHandler.setStackInSlot(slot,0,this.display);
                        this.display=playerStack;
                    }
                }
                case THROW -> {
                    if(!this.canRemove) break;

                    var decAmt = button==0 ? 1 : this.display.getMaxCount();
                    var newCount = Math.max(0,this.display.getCount()-decAmt);
                    player.dropItem(this.display.copyWithCount(this.display.getCount()-newCount),true);
                    this.display.setCount(newCount);
                }
                case CLONE -> {
                    if(!player.isCreative()) break;

                    if(player.currentScreenHandler.getCursorStack().isEmpty()){
                        player.currentScreenHandler.setCursorStack(
                                this.display.copyWithCount(this.display.getMaxCount()));
                    }
                }
                case QUICK_CRAFT -> {
                    //dragging item
                }
                case PICKUP_ALL -> {
                    var playerStack = player.currentScreenHandler.getCursorStack();
                    if (!playerStack.isEmpty() && this.display.isEmpty() && this.canRemove) {
                        var newCount=playerStack.getCount();
                        var maxCount = playerStack.getMaxCount();
                        for(var slot : thisInv.getHandler().slots){
                            var stack = slot.getStack();
                            if(!slot.canTakeItems(player)||
                                    !ItemStack.canCombine(playerStack, stack)) continue;

                            var nextNewCount=Math.min(maxCount, newCount+stack.getCount());
                            stack.decrement(nextNewCount-newCount);
                            newCount=nextNewCount;
                            if(newCount>=maxCount) break;
                        }
                        playerStack.setCount(newCount);
                    }
                }
                case PICKUP -> {
                    if(button==0){
                        var playerStack = player.currentScreenHandler.getCursorStack();

                        if(playerStack.isEmpty() || !ItemStack.canCombine(playerStack, this.display)){
                            if(!this.canRemove&&!this.display.isEmpty()) break;
                            if(!this.canInsert&&!playerStack.isEmpty()) break;

                            player.currentScreenHandler.setCursorStack(this.display);
                            this.display=playerStack;
                        }else{
                            if(!this.canInsert) break;

                            var totalCount = this.display.getCount()+playerStack.getCount();
                            var thisCount=Math.min(playerStack.getMaxCount(),
                                    this.display.getCount()+playerStack.getCount());
                            this.display.setCount(thisCount);
                            playerStack.setCount(totalCount-thisCount);
                        }
                    }else if(button==1){
                        var playerStack = player.currentScreenHandler.getCursorStack();
                        if(playerStack.isEmpty()){
                            if(!this.canRemove) break;

                            player.currentScreenHandler.setCursorStack(
                                    this.display.copyWithCount(this.display.getCount()-this.display.getCount()/2));
                            this.display.setCount(this.display.getCount()/2);
                        }else if(this.display.isEmpty()){
                            if(!this.canInsert) break;

                            this.display=playerStack.copyWithCount(1);
                            playerStack.decrement(1);
                        }else if(ItemStack.canCombine(playerStack, this.display)){
                            if(this.display.getItem().getMaxCount()>=this.display.getCount()+1&&this.canInsert){
                                this.display.setCount(this.display.getCount()+1);
                                playerStack.decrement(1);
                            }
                        }else{
                            if(!this.canInsert||!this.canRemove) break;

                            player.currentScreenHandler.setCursorStack(this.display);
                            this.display=playerStack;
                        }
                    }
                }
                case QUICK_MOVE -> {
                    if(!this.canRemove) break;
                    player.giveItemStack(player.currentScreenHandler.getSlot(slotIndex).getStack());
                }
            }

            thisInv.getHandler().refresh();
        };
    }
}
