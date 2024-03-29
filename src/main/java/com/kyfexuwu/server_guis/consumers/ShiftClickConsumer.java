package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface ShiftClickConsumer<T> {
    ItemStack consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument, int slotNum);
}
