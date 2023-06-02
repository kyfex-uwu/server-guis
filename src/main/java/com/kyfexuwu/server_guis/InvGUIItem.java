package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;

public interface InvGUIItem{
    ItemStack getItem(PlayerEntity player, Object argument);
    ClickConsumer onClick();

    InvGUIItem IMMOVABLE = new InvGUIItem() {
        @Override
        public ItemStack getItem(PlayerEntity player, Object argument) {
            return Items.BLACK_STAINED_GLASS_PANE.getDefaultStack();
        }

        @Override
        public ClickConsumer onClick() {
            return ClickConsumer.NOTHING;
        }
    };
    InvGUIItem EMPTY = new InvGUIItem() {
        @Override
        public ItemStack getItem(PlayerEntity player, Object argument) {
            return Items.AIR.getDefaultStack();
        }

        @Override
        public ClickConsumer onClick() {
            return ClickConsumer.NOTHING;
        }
    };

    class InvGUIEntry{
        public final char key;
        public final InvGUIItem item;

        public InvGUIEntry(char key, InvGUIItem item){
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
                toReturn[i] = toSet.get().item;
            else
                toReturn[i] = InvGUIItem.EMPTY;
        }

        return toReturn;
    }
}
