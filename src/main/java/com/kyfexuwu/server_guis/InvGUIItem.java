package com.kyfexuwu.server_guis;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public interface InvGUIItem{
    ItemStack getItem(ServerPlayerEntity player, Object argument);
    ClickConsumer<?> onClick();

    class InvGUIEntry{
        public final char key;
        public final Supplier<InvGUIItem> item;

        public InvGUIEntry(char key, InvGUIItem item){
            this.key=key;
            this.item=()->item;
        }
        public InvGUIEntry(char key, Supplier<InvGUIItem> item){
            this.key=key;
            this.item=item;
        }
    }
    InvGUIItem EMPTY = new InvGUIItem() {
        @Override
        public ItemStack getItem(ServerPlayerEntity player, Object argument) {
            return ItemStack.EMPTY;
        }

        @Override
        public ClickConsumer<?> onClick() {
            return ServerGUIs.nothingClick();
        }
    };
    static InvGUIItem[] decode(String map, InvGUIItem.InvGUIEntry... translations){
        var toReturn = new InvGUIItem[map.length()];
        for(int i=0;i<toReturn.length;i++){
            int finalI = i;
            var toSet=Arrays.stream(translations).filter(entry->entry.key==map.charAt(finalI))
                    .findFirst();
            if(toSet.isPresent())
                toReturn[i] = toSet.get().item.get();
            else
                toReturn[i] = EMPTY;
        }

        return toReturn;
    }
}
