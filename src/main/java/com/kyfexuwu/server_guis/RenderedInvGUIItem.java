package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiFunction;

public class RenderedInvGUIItem<T> implements InvGUIItem {
    private final BiFunction<ServerPlayerEntity, T, ItemStack> display;
    private final ClickConsumer<T> clickConsumer;
    public RenderedInvGUIItem(BiFunction<ServerPlayerEntity, T, ItemStack> display, ClickConsumer<T> clickConsumer){
        this.display=display;
        this.clickConsumer=clickConsumer;
    }

    @Override
    public ItemStack getItem(ServerPlayerEntity player, Object parameter) {
        try {
            return this.display.apply(player, (T) parameter);
        }catch(ClassCastException e){
            return this.display.apply(player, null);
        }
    }

    @Override
    public ClickConsumer<?> onClick() {
        return this.clickConsumer;
    }
}
