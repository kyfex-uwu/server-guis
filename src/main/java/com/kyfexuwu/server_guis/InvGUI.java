package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.text.Text;

import java.util.HashMap;

public class InvGUI {
    @FunctionalInterface
    public interface Builder{
        InvGUI build(PlayerEntity player, Template template);
    }
    public static class Template {
        public final ScreenHandlerType<?> type;
        public final Text title;
        public final InvGUIItem[] items;
        private final Builder builder;

        private static final Builder defaultBuilder = (player, template) ->
                new InvGUI(template.type, template.title, template.items);
        public Template(ScreenHandlerType<?> type, Text title, InvGUIItem[] items) {
            this(type,title,items,defaultBuilder);
        }
        public Template(ScreenHandlerType<?> type, Text title, InvGUIItem[] items, Builder builder) {
            this.type = type;
            this.title = title;
            this.items = items;
            this.builder = builder;
        }
        public InvGUI build(PlayerEntity player){
            return this.builder.build(player,this);
        }
        public void buildAndOpen(PlayerEntity player){
            this.build(player).open(player);
        }
    }
    //todo: change to identifiers
    private static final HashMap<String, Template> guis = new HashMap<>();
    public static void register(String name, Template gui){ guis.put(name, gui); }
    public static Template get(String name){ return guis.get(name); }
    public static void getBuildAndOpen(String name, PlayerEntity player){ guis.get(name).buildAndOpen(player); }
    private ServerGuiHandler handler;
    private final ScreenHandlerType<?> type;
    public final Text title;
    public final InvGUIItem[] items;
    public InvGUI(ScreenHandlerType<?> type, Text title, InvGUIItem[] items) {
        this.type = type;
        this.title = title;
        this.items = items;
    }

    public void open(PlayerEntity player){
        var thisObj = this;
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return thisObj.title;
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                thisObj.handler = new ServerGuiHandler(syncId, playerInventory, thisObj.type, thisObj);

                return thisObj.handler;
            }
        });
    }
}
