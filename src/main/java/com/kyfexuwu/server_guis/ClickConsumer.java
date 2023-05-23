package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;

@FunctionalInterface
public interface ClickConsumer {
    void consume(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, InvGUI thisInv);
}
