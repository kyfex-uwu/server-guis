package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface ClickConsumer<T> {
    void consume(int slotIndex, int button, SlotActionType actionType, ServerPlayerEntity player, InvGUI<?> thisInv, T argument);
}
