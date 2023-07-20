package com.kyfexuwu.server_guis;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
    public StaticInvGUIItem(ItemStack display, ClickConsumer<?> clickConsumer){
        this.display=display;
        this.clickConsumer=clickConsumer;
    }

    @Override
    public ItemStack getItem(ServerPlayerEntity player, Object argument) {
        return this.display;
    }

    @Override
    public ClickConsumer<?> onClick() {
        return this.clickConsumer;
    }
}
