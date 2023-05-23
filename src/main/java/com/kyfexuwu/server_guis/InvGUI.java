package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InvGUI {
    private ServerGuiHandler handler;
    public InvGUI(){}

    private Map<String, InvGUIItem[]> screens = new HashMap<>();
    private Map<String, Supplier<InvGUIItem[]>> complexScreens = new HashMap<>();
    public InvGUI registerScreen(String name, InvGUIItem[] items){
        this.screens.put(name, items);
        return this;
    }
    public InvGUI registerComplexScreen(String name, Supplier<InvGUIItem[]> items){
        this.complexScreens.put(name, items);
        return this;
    }

    public void switchScreen(String name){
        var thisScreen = this.screens.get(name);
        for(int i=0;i<27;i++){
            this.handler.putInvGUIItem(i, thisScreen[i]);
        }
    }
    public void switchToComplexScreen(String name){
        var thisScreen = this.complexScreens.get(name).get();
        for(int i=0;i<27;i++){
            this.handler.putInvGUIItem(i, thisScreen[i]);
        }
    }
    public void switchToGeneratedScreen(InvGUIItem[] items){
        for(int i=0;i<27;i++){
            this.handler.putInvGUIItem(i, items[i]);
        }
    }

    public void open(PlayerEntity player, String screen){
        if(!screens.containsKey(screen)) return;

        var thisObj = this;
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.of("ServerGUI");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                thisObj.handler = new ServerGuiHandler(syncId, playerInventory, thisObj) {
                    @Override
                    public ItemStack quickMove(PlayerEntity player1, int slot) { return ItemStack.EMPTY; }

                    @Override
                    public boolean canUse(PlayerEntity player1) { return true; }
                };

                thisObj.switchScreen(screen);

                return thisObj.handler;
            }
        });
    }
}
