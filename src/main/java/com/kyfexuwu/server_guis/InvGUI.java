package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class InvGUI<T> {
    @FunctionalInterface
    public interface Builder<T>{
        InvGUI<T> build(ServerPlayerEntity player, Template<T> template, T argument);
    }
    public static <T> Builder<T> defaultBuilder() {
        return (player, template, arg) ->
                new InvGUI<>(template.type, template.title, template.items, template.onClose);
    }
    public static class Template<T> {
        public final ScreenHandlerType<?> type;
        public final Text title;
        public final InvGUIItem[] items;
        private final Builder<T> builder;
        public final CloseConsumer<T> onClose;

        public Template(ScreenHandlerType<?> type, Text title, InvGUIItem[] items, CloseConsumer<T> onClose, Builder<T> builder) {
            this.type = type;
            this.title = title;
            this.items = items;
            this.builder = builder;
            this.onClose = onClose;
        }
        public InvGUI<T> build(ServerPlayerEntity player, T arg){
            return this.builder.build(player,this, arg);
        }
        public void buildAndOpen(ServerPlayerEntity player, T arg){
            this.build(player, arg).open(player, arg);
        }
    }
    private ServerGuiHandler handler;
    public ServerGuiHandler getHandler(){ return this.handler; }
    public final ScreenHandlerType<?> type;
    public final Text title;
    public final InvGUIItem[] items;
    public final CloseConsumer<T> onClose;
    public InvGUI(ScreenHandlerType<?> type, Text title, InvGUIItem[] items, CloseConsumer<T> onClose) {
        this.type = type;
        this.title = title;
        this.items = items;
        this.onClose = onClose;
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

                return thisObj.handler;
            }
        });
    }
}
