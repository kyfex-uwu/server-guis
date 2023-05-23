package com.kyfexuwu.server_guis;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Arrays;

public class InvGUIItem {
    public final ItemStack display;
    public final ClickConsumer onClick;
    public InvGUIItem(ItemConvertible display, Text name, ClickConsumer onClick){
        this.display=display.asItem().getDefaultStack().setCustomName(name);
        this.onClick=onClick;
    }

    public static final InvGUIItem IMMOVABLE = new InvGUIItem(Blocks.BLACK_STAINED_GLASS_PANE,Text.empty(),
            (_1,_2,_3,_4,_5)->{});
    public static final InvGUIItem EMPTY = new InvGUIItem(Blocks.AIR,Text.empty(),
            (_1,_2,_3,_4,_5)->{});

    public record InvGuiEntry(char index, InvGUIItem item){}
    public static InvGUIItem[] decode(String map, InvGuiEntry... translations){
        var toReturn = new InvGUIItem[27];
        for(int i=0;i<27;i++){
            int finalI = i;
            var toSet=Arrays.stream(translations).filter(entry->entry.index==map.charAt(finalI))
                    .findFirst();
            if(toSet.isPresent())
                toReturn[i] = toSet.get().item;
            else
                toReturn[i] = InvGUIItem.EMPTY;
        }

        return toReturn;
    }
}
