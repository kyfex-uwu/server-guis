package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public class RenderedInvGUIItem<T> implements InvGUIItem {
    private final BiFunction<PlayerEntity, T, ItemStack> display;
    private final ClickConsumer<T> clickConsumer;
    public RenderedInvGUIItem(BiFunction<PlayerEntity, T, ItemStack> display, ClickConsumer<T> clickConsumer){
        this.display=display;
        this.clickConsumer=clickConsumer;
    }

    @Override
    public ItemStack getItem(PlayerEntity player, Object parameter) {
        try {
            return this.display.apply(player, (T) parameter);
        }catch(Exception e){
            return this.display.apply(player, null);
        }
    }

    @Override
    public ClickConsumer onClick() {
        return this.clickConsumer;
    }
}
