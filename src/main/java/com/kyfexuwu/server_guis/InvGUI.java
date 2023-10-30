package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class InvGUI<T> {
    @FunctionalInterface
    public interface Builder<T>{
        InvGUI<T> build(ServerPlayerEntity player, Template<T> template, T argument);
    }
    public static class Template<T> {
        public final ServerGUIs.ScreenType type;
        public final Text title;
        public final InvGUIItem[] items;
        private Builder<T> builder = (player, template, arg) -> new InvGUI<>(template.type, template.title, template.items);
        private CloseConsumer<T> onCloseConsumer = (player, thisInv, argument) -> { };
        private ButtonClickConsumer<T> onButtonClickConsumer = (player, thisInv, argument, buttonIndex) -> true;
        private ShiftClickConsumer<T> onShiftClickConsumer = (player, thisInv, argument, slotNum) -> ItemStack.EMPTY;
        private SlotUpdateConsumer<T> onSlotUpdateConsumer = (player, thisInv, argument, slotNum, stack) -> {};
        private PropertyUpdateConsumer<T> onPropertyUpdateConsumer = (player, thisInv, argument, property, value) -> {};
        private AnvilTypeConsumer<T> onAnvilTypeConsumer = (player, thisInv, argument, newText) -> { };
        private BeaconInteractionConsumer<T> onBeaconChangeConsumer = (player, thisInv, argument, effect1, effect2) -> { };
        public Template(ServerGUIs.ScreenType type, Text title, InvGUIItem[] items) {
            this.type = type;
            this.title = title;
            this.items = items;
        }
        public Template<T> toBuild(Builder<T> builder){
            this.builder=builder;
            return this;
        }
        public Template<T> onClose(CloseConsumer<T> onClose){
            this.onCloseConsumer=onClose;
            return this;
        }
        public Template<T> onButtonClick(ButtonClickConsumer<T> onButtonClick){
            this.onButtonClickConsumer=onButtonClick;
            return this;
        }
        public Template<T> onShiftClick(ShiftClickConsumer<T> onShiftClick){
            this.onShiftClickConsumer=onShiftClick;
            return this;
        }
        public Template<T> onSlotUpdate(SlotUpdateConsumer<T> onSlotUpdate){
            this.onSlotUpdateConsumer=onSlotUpdate;
            return this;
        }
        public Template<T> onPropertyUpdate(PropertyUpdateConsumer<T> onPropertyUpdate){
            this.onPropertyUpdateConsumer=onPropertyUpdate;
            return this;
        }
        public Template<T> onAnvilType(AnvilTypeConsumer<T> onAnvilType){
            this.onAnvilTypeConsumer=onAnvilType;
            return this;
        }
        public Template<T> onBeaconChange(BeaconInteractionConsumer<T> onBeaconChange){
            this.onBeaconChangeConsumer=onBeaconChange;
            return this;
        }
        public InvGUI<T> build(ServerPlayerEntity player, T arg){
            var toReturn = this.builder.build(player,this, arg);
            toReturn.onClose=this.onCloseConsumer;
            toReturn.onButtonClick=this.onButtonClickConsumer;
            toReturn.onShiftClick=this.onShiftClickConsumer;
            toReturn.onSlotUpdate=this.onSlotUpdateConsumer;
            toReturn.onPropertyUpdate=this.onPropertyUpdateConsumer;
            toReturn.onAnvilType=this.onAnvilTypeConsumer;
            toReturn.onBeaconChange=this.onBeaconChangeConsumer;
            return toReturn;
        }
        public void buildAndOpen(ServerPlayerEntity player, T arg){
            this.build(player, arg).open(player, arg);
        }
    }
    private ServerGuiHandler handler;
    public ServerGuiHandler getHandler(){ return this.handler; }
    public final ServerGUIs.ScreenType type;
    public final Text title;
    public final InvGUIItem[] items;
    public final PropertyDelegate propertyDelegate;
    private CloseConsumer<T> onClose;
    private ButtonClickConsumer<T> onButtonClick;
    private ShiftClickConsumer<T> onShiftClick;
    private SlotUpdateConsumer<T> onSlotUpdate;
    private PropertyUpdateConsumer<T> onPropertyUpdate;
    private AnvilTypeConsumer<T> onAnvilType;
    private BeaconInteractionConsumer<T> onBeaconChange;
    public void onClose(){
        this.onClose.consume(this.handler.player,
                this, ServerGuiHandler.appeaseCompiler(this.handler.argument));

        //give back removable items
        for(InvGUIItem item : this.items){
            if(item instanceof RemovableInvGUIItem){
                this.handler.player.giveItemStack(((RemovableInvGUIItem) item).display);
            }
        }
    }
    public boolean onButtonClick(int buttonIndex){
        return this.onButtonClick.consume(this.handler.player,
                this,ServerGuiHandler.appeaseCompiler(this.handler.argument),
                buttonIndex);
    }
    public ItemStack onShiftClick(int slotNum){
        return this.onShiftClick.consume(this.handler.player,
                this,ServerGuiHandler.appeaseCompiler(this.handler.argument),
                slotNum);
    }
    public void onSlotUpdate(int slotNum, ItemStack stack){
        this.onSlotUpdate.consume(this.handler.player,
                this, ServerGuiHandler.appeaseCompiler(this.handler.argument),
                slotNum, stack);
    }
    public void onPropertyUpdate(int property, int value){
        this.onPropertyUpdate.consume(this.handler.player,
                this, ServerGuiHandler.appeaseCompiler(this.handler.argument),
                property, value);
    }
    public void onAnvilType(String newText){
        this.anvilText=newText;
        this.onAnvilType.consume(this.handler.player,
                this, ServerGuiHandler.appeaseCompiler(this.handler.argument),
                newText);
    }
    public void onBeaconChange(Optional<StatusEffect> effect1, Optional<StatusEffect> effect2){
        this.onBeaconChange.consume(this.handler.player,
                this, ServerGuiHandler.appeaseCompiler(this.handler.argument),
                effect1, effect2);
    }

    public InvGUI(ServerGUIs.ScreenType type, Text title, InvGUIItem[] items) {
        this.type = type;
        this.title = title;
        this.items = items;

        this.propertyDelegate = new ArrayPropertyDelegate(type.propCount);
    }

    public void open(ServerPlayerEntity player, T argument){
        var thisObj = this;

        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return thisObj.title;
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                thisObj.handler = new ServerGuiHandler(syncId, playerInventory, thisObj.type, thisObj, argument);
                thisObj.handler.addListener(new ScreenHandlerListener() {
                    @Override
                    public void onSlotUpdate(ScreenHandler handler, int slotNum, ItemStack stack) {
                        thisObj.onSlotUpdate(slotNum, stack);
                    }

                    @Override
                    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                        thisObj.onPropertyUpdate(property, value);//todo: make the properties human readable?
                    }
                });

                return thisObj.handler;
            }
        });
    }

    private String anvilText="";
    public String getAnvilText(){
        return anvilText;
    }
}
