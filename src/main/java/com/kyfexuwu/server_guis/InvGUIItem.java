package com.kyfexuwu.server_guis;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Arrays;

public class InvGUIItem {
    private final ItemStack display;
    public ItemStack display(PlayerEntity player){
        return this.display;
    }
    public final ClickConsumer onClick;

    public InvGUIItem(ItemConvertible display, Text name, int count, ClickConsumer onClick){
        var d = display.asItem().getDefaultStack().setCustomName(name);
        d.setCount(count);
        this.display=d;
        this.onClick=onClick;
    }
    public InvGUIItem(ItemConvertible display, Text name, ClickConsumer onClick){
        this(display, name, 1, onClick);
    }
    public InvGUIItem(ItemConvertible display, Text name, int count){
        this(display, name, count, ClickConsumer.NOTHING);
    }
    public InvGUIItem(ItemConvertible display, Text name){
        this(display, name, 1, ClickConsumer.NOTHING);
    }

    public static final InvGUIItem IMMOVABLE = new InvGUIItem(Blocks.BLACK_STAINED_GLASS_PANE,Text.empty(),
            ClickConsumer.NOTHING);
    public static final InvGUIItem EMPTY = new InvGUIItem(Blocks.AIR,Text.empty(),
            ClickConsumer.NOTHING);

    public record InvGUIEntry(char index, InvGUIItem item){}
    public static InvGUIItem[] decode(String map, InvGUIEntry... translations){
        var toReturn = new InvGUIItem[map.length()];
        for(int i=0;i<toReturn.length;i++){
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
