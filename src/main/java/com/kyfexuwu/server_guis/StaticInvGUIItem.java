package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class StaticInvGUIItem implements InvGUIItem{
    private final ItemStack display;
    private final ClickConsumer<?> clickConsumer;

    public StaticInvGUIItem(ItemConvertible display, Text name, int count, ClickConsumer<?> clickConsumer){
        var d = display.asItem().getDefaultStack().setCustomName(name);
        d.setCount(count);
        this.display=d;
        this.clickConsumer=clickConsumer;
    }

    @Override
    public ItemStack getItem(PlayerEntity player, Object argument) {
        return this.display;
    }

    @Override
    public ClickConsumer<?> onClick() {
        return this.clickConsumer;
    }
}
