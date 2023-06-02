package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.text.Text;

public class InvGUI<T> {
    @FunctionalInterface
    public interface Builder<T>{
        InvGUI<T> build(PlayerEntity player, Template<T> template, T argument);
    }
    public static class Template<T> {
        public final ScreenHandlerType<?> type;
        public final Text title;
        public final InvGUIItem[] items;
        private final Builder<T> builder;

        public static final Builder<Void> defaultBuilder = (player, template, arg) ->
                new InvGUI<>(template.type, template.title, template.items);
        public Template(ScreenHandlerType<?> type, Text title, InvGUIItem[] items, Builder<T> builder) {
            this.type = type;
            this.title = title;
            this.items = items;
            this.builder = builder;
        }
        public InvGUI<T> build(PlayerEntity player, T arg){
            return this.builder.build(player,this, arg);
        }
        public void buildAndOpen(PlayerEntity player, T arg){
            this.build(player, arg).open(player, arg);
        }
    }
    private ServerGuiHandler handler;
    private final ScreenHandlerType<?> type;
    public final Text title;
    public final InvGUIItem[] items;
    public InvGUI(ScreenHandlerType<?> type, Text title, InvGUIItem[] items) {
        this.type = type;
        this.title = title;
        this.items = items;
    }

    public void open(PlayerEntity player, T argument){
        var thisObj = this;
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return thisObj.title;
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                thisObj.handler = new ServerGuiHandler(syncId, playerInventory, thisObj.type, thisObj, argument);

                return thisObj.handler;
            }
        });
    }
}
