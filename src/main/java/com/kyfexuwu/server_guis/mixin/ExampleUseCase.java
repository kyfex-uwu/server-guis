package com.kyfexuwu.server_guis.mixin;

import com.kyfexuwu.server_guis.InvGUI;
import com.kyfexuwu.server_guis.InvGUIItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlazedTerracottaBlock.class)
public class ExampleUseCase {

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var gui = new InvGUI().registerScreen("main", InvGUIItem.decode(
                "#########"+
                    "####o####"+
                    "#########",
                new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE),
                new InvGUIItem.InvGuiEntry('o', new InvGUIItem(Blocks.REDSTONE_BLOCK, Text.of("Click me!"),
                        (slotIndex, button, actionType, pPlayer, thisInv)->{
                            thisInv.switchScreen("second");
                        }))))
                .registerScreen("second", InvGUIItem.decode(
                "#########"+
                    "####o####"+
                    "#########",
                new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE),
                new InvGUIItem.InvGuiEntry('o', new InvGUIItem(Blocks.LAPIS_BLOCK, Text.of("Ok, now go back"),
                        (slotIndex, button, actionType, pPlayer, thisInv)->{
                            var screen = InvGUIItem.decode(
                                    "#########"+
                                        "#### ####"+
                                        "#########",
                                    new InvGUIItem.InvGuiEntry('#', InvGUIItem.IMMOVABLE));
                            screen[13] = new InvGUIItem(Items.DIAMOND,
                                    pPlayer.getDisplayName(),(_1, _2, _3, _4, _5)->{
                                thisInv.switchScreen("main");
                            });

                            thisInv.switchToGeneratedScreen(screen);
                        }))));
        gui.open(player, "main");
        return ActionResult.success(world.isClient);
    }
}
