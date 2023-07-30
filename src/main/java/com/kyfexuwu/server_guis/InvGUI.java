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
                new InvGUI<>(template.type, template.title, template.items);
    }
    public static class Template<T> {
        public final ServerGUIs.ScreenType type;
        public final Text title;
        public final InvGUIItem[] items;
        private final Builder<T> builder;
        private CloseConsumer<T> onCloseConsumer = (player, thisInv, argument) -> { };
        private AnvilTypeConsumer<T> onAnvilTypeConsumer = (player, thisInv, argument, newText) -> { };

        public Template(ServerGUIs.ScreenType type, Text title, InvGUIItem[] items, Builder<T> builder) {
            this.type = type;
            this.title = title;
            this.items = items;
            this.builder = builder;
        }
        public Template<T> onClose(CloseConsumer<T> onClose){
            this.onCloseConsumer=onClose;
            return this;
        }
        public Template<T> onAnvilType(AnvilTypeConsumer<T> onAnvilType){
            this.onAnvilTypeConsumer=onAnvilType;
            return this;
        }
        public InvGUI<T> build(ServerPlayerEntity player, T arg){
            var toReturn = this.builder.build(player,this, arg);
            toReturn.onClose=this.onCloseConsumer;
            toReturn.onAnvilType=this.onAnvilTypeConsumer;
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
    private CloseConsumer<T> onClose;
    private AnvilTypeConsumer<T> onAnvilType;
    public void onClose(){
        this.onClose.consume(this.getHandler().player, this, ServerGuiHandler.appeaseCompiler(this.getHandler().argument));
    }
    public void onAnvilType(String newText){
        this.anvilText=newText;
        this.onAnvilType.consume(this.getHandler().player, this, ServerGuiHandler.appeaseCompiler(this.getHandler().argument),
                newText);
    }

    public InvGUI(ServerGUIs.ScreenType type, Text title, InvGUIItem[] items) {
        this.type = type;
        this.title = title;
        this.items = items;
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
                thisObj.handler = new ServerGuiHandler(syncId, playerInventory, thisObj.type.get(), thisObj, argument);

                return thisObj.handler;
            }
        });
    }

    private String anvilText="";
    public String getAnvilText(){
        return anvilText;
    }
}
