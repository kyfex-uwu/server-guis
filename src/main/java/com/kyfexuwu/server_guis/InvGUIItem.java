package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public interface InvGUIItem{
    ItemStack getItem(ServerPlayerEntity player, InvGUI<?> gui, Object argument);
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
    static InvGUIItem[] decode(String map, InvGUIItem.InvGUIEntry... translations){
        var toReturn = new InvGUIItem[map.length()];
        for(int i=0;i<toReturn.length;i++){
            int finalI = i;
            var toSet=Arrays.stream(translations).filter(entry->entry.key==map.charAt(finalI))
                    .findFirst();
            if(toSet.isPresent())
                toReturn[i] = toSet.get().item.get();
            else
                toReturn[i] = ServerGUIs.EMPTY;
        }

        return toReturn;
    }
}
