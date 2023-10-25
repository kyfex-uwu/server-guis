package com.kyfexuwu.server_guis;

import com.kyfexuwu.server_guis.consumers.ClickConsumer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.function.TriFunction;

public class RenderedInvGUIItem<T> implements InvGUIItem {
    private final TriFunction<ServerPlayerEntity, InvGUI<?>, T, ItemStack> display;
    private final ClickConsumer<T> clickConsumer;
    public RenderedInvGUIItem(TriFunction<ServerPlayerEntity, InvGUI<?>, T, ItemStack> display, ClickConsumer<T> clickConsumer){
        this.display=display;
        this.clickConsumer=clickConsumer;
    }

    @Override
    public ItemStack getItem(ServerPlayerEntity player, InvGUI<?> gui, Object parameter) {
        try {
            return this.display.apply(player, gui, (T) parameter);
        }catch(ClassCastException e){
            return this.display.apply(player, gui, null);
        }
    }

    @Override
    public ClickConsumer<?> onClick() {
        return this.clickConsumer;
    }
}
