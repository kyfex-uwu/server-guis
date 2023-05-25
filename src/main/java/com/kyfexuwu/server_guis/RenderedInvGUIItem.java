package com.kyfexuwu.server_guis;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Function;

public class RenderedInvGUIItem extends InvGUIItem{
    private final Function<PlayerEntity, ItemStack> display;
    @Override
    public ItemStack display(PlayerEntity player){
        return this.display.apply(player);
    }
    public RenderedInvGUIItem(Function<PlayerEntity, ItemStack> display, ClickConsumer onClick) {
        super(Blocks.AIR, Text.empty(), onClick);
        this.display=display;
    }
}
