package com.kyfexuwu.server_guis;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

import java.util.Optional;

public class PropertyHelpers {
    private static void checkGUIType(InvGUI<?> gui, String errorMessage, ServerGUIs.ScreenType... types){
        for(var type : types){
            if(gui.type==type) return;
        }
        throw new IllegalArgumentException(errorMessage);
    }
    private static void throwGuiDataTypeError(String name, Class<?> type){
        ServerGUIs.LOGGER.error("The \""+name+"\" data is invalid! Check if it exists " +
                "and is of type "+type.getSimpleName());
    }

    public static void setBeaconLevel(InvGUI<?> gui, int level){
        checkGUIType(gui, "Tried to set beacon level of gui "+gui+", but the gui is not a beacon gui!",
                ServerGUIs.ScreenType.BEACON);

        gui.propertyDelegate.set(0,level);
    }
    public static void setBeaconEffects(InvGUI<?> gui, Optional<StatusEffect> effect1, Optional<StatusEffect> effect2){
        checkGUIType(gui, "Tried to set status effects of gui "+gui+", but the gui is not a beacon gui!",
                ServerGUIs.ScreenType.BEACON);

        effect1.ifPresent(statusEffect ->
                gui.propertyDelegate.set(1, StatusEffect.getRawId(statusEffect)));
        effect2.ifPresent(statusEffect ->
                gui.propertyDelegate.set(2, StatusEffect.getRawId(statusEffect)));
    }

    public static void setFurnaceCookProgress(InvGUI<?> gui, double cookProgress){
        checkGUIType(gui, "Tried to set cook data of gui "+gui+", but the gui is not a furnace type gui!",
                ServerGUIs.ScreenType.FURNACE, ServerGUIs.ScreenType.BLAST_FURNACE, ServerGUIs.ScreenType.SMOKER);

        if(cookProgress<0) cookProgress=0;
        else if(cookProgress>1) cookProgress=1;
        gui.propertyDelegate.set(2, (int) (1000*cookProgress));
    }
    public static void setFurnaceFuelProgress(InvGUI<?> gui, double fuelProgress){
        checkGUIType(gui, "Tried to set fuel data of gui "+gui+", but the gui is not a furnace type gui!",
                ServerGUIs.ScreenType.FURNACE, ServerGUIs.ScreenType.BLAST_FURNACE, ServerGUIs.ScreenType.SMOKER);

        if(fuelProgress<0) fuelProgress=0;
        else if(fuelProgress>1) fuelProgress=1;
        gui.propertyDelegate.set(0, (int) (1000*fuelProgress));
    }

    public static void setBrewingFuel(InvGUI<?> gui, int fuelAmt){
        checkGUIType(gui, "Tried to set fuel of gui "+gui+", but the gui is not a brewing gui!",
                ServerGUIs.ScreenType.BREWING_STAND);

        gui.propertyDelegate.set(1, fuelAmt);
    }
    public static void setBrewingTime(InvGUI<?> gui, int time){
        checkGUIType(gui, "Tried to set time of gui "+gui+", but the gui is not a brewing gui!",
                ServerGUIs.ScreenType.BREWING_STAND);

        gui.propertyDelegate.set(0, time);
    }

    public static void setBannerPattern(InvGUI<?> gui, int pattern){
        checkGUIType(gui, "Tried to set pattern of gui "+gui+", but the gui is not a loom gui!",
                ServerGUIs.ScreenType.LOOM);

        gui.propertyDelegate.set(0, pattern);
    }

    public static boolean handleBookPageTurning(InvGUI<?> gui, int buttonIndex){
        checkGUIType(gui, "Tried to handle book pages of gui "+gui+", but the gui is not a lectern gui!",
                ServerGUIs.ScreenType.LECTERN);

        if(buttonIndex==1||buttonIndex==2){
            gui.propertyDelegate.set(0, gui.propertyDelegate.get(0)-3+buttonIndex*2);
            return true;
        }
        return false;
    }
    public static void setBookPage(InvGUI<?> gui, int pageIndex){
        checkGUIType(gui, "Tried to set page of gui "+gui+", but the gui is not a lectern gui!",
                ServerGUIs.ScreenType.LECTERN);

        gui.propertyDelegate.set(0, pageIndex);
    }

    public static class EnchantmentData{
        public int id;
        public int level;
        public int xpAmt;

        private static final Random random = Random.create();
        private static int calcRandomExperience(int slot){
            return Math.min(30, Math.max(1,
                    (random.nextInt(8) + random.nextInt(16) + 8)*(slot+1)/3)); //clamps between 1 and 30
        }

        public EnchantmentData(int id, int level, int xpAmt){
            this.id=id;
            this.level=level;
            this.xpAmt=xpAmt;
        }

        public static class EnchantmentDataBuilder{
            private int id=-1;
            private int level=1;
            private int xpAmt=-1;
            private EnchantmentDataBuilder(){}

            public EnchantmentDataBuilder setEnchantmentId(int id){
                if(id<0) return this;

                this.id=id;
                return this;
            }
            public EnchantmentDataBuilder setEnchantment(Enchantment enchantment){
                this.id= Registries.ENCHANTMENT.getRawId(enchantment);
                return this;
            }

            public EnchantmentDataBuilder setLevel(int level){
                if(level<1) return this;

                this.level=level;
                return this;
            }

            public EnchantmentDataBuilder setXPAmt(int amt){
                if(amt<0) return this;

                this.xpAmt=amt;
                return this;
            }
            public EnchantmentDataBuilder setRandomXPAmt(int slot){
                this.xpAmt=calcRandomExperience(slot);
                return this;
            }

            public EnchantmentData build(){
                if(this.xpAmt==-1) this.xpAmt = calcRandomExperience(0);
                return new EnchantmentData(this.id, this.level, this.xpAmt);
            }
        }
        public static EnchantmentDataBuilder builder(){
            return new EnchantmentDataBuilder();
        }
    }
    public static void setEnchatingTableEnchantments(InvGUI<?> gui,
        Optional<EnchantmentData> ench1, Optional<EnchantmentData> ench2, Optional<EnchantmentData> ench3){
        checkGUIType(gui, "Tried to set enchanting data of gui "+gui+", but the gui is not an enchanting table gui!",
                ServerGUIs.ScreenType.ENCHANTMENT);

        ench1.ifPresent(data->{
            gui.propertyDelegate.set(0, data.xpAmt);
            gui.propertyDelegate.set(4, data.id);
            gui.propertyDelegate.set(7, data.level);
        });
        ench2.ifPresent(data->{
            gui.propertyDelegate.set(1, data.xpAmt);
            gui.propertyDelegate.set(5, data.id);
            gui.propertyDelegate.set(8, data.level);
        });
        ench3.ifPresent(data->{
            gui.propertyDelegate.set(2, data.xpAmt);
            gui.propertyDelegate.set(6, data.id);
            gui.propertyDelegate.set(9, data.level);
        });
    }

    public static void setTrades(InvGUI<?> gui,
        boolean displaysLevel, int merchantLevel, int progressToNextLevel, boolean canRefresh,
        TradeOfferList trades){
        checkGUIType(gui, "Tried to set trades of gui "+gui+", but the gui is not a merchant gui!",
                ServerGUIs.ScreenType.MERCHANT);

        var toSend = new SetTradeOffersS2CPacket(gui.getHandler().syncId,
                trades, merchantLevel, progressToNextLevel, displaysLevel, canRefresh);

        gui.setData("builtin.trades", trades);
        gui.getHandler().player.networkHandler.sendPacket(toSend);
    }
    public static boolean handleTradeButtons(InvGUI<?> gui, int buttonIndex){
        checkGUIType(gui, "Tried to handle trade buttons of gui "+gui+", but the gui is not a merchant gui!",
                ServerGUIs.ScreenType.MERCHANT);

        if(!(gui.items[0] instanceof RemovableInvGUIItem &&
                gui.items[1] instanceof RemovableInvGUIItem &&
                gui.items[2] instanceof RemovableInvGUIItem))
            throw new IllegalStateException("All of this gui's slots must be RemovableInvGUIItems!");

        TradeOfferList trades;
        try{
            trades = gui.getDataOfType("builtin.trades", TradeOfferList.class);
        }catch(ClassCastException e){
            throwGuiDataTypeError("builtin.trades", TradeOfferList.class);
            return true;
        }

        if (buttonIndex >= 0 && buttonIndex < trades.size()) {
            var handler = gui.getHandler();

            for(int counter=0;counter<2;counter++){
                ItemStack toInsert = gui.items[counter].getItem(
                        gui.getHandler().player, gui, gui.getHandler().argument);
                int[] addVals = new int[36];
                int insertAmt=toInsert.getCount();
                ItemStack currStack;

                for(int i=0;i<36;i++){
                    currStack=handler.inventory.getStack(handler.type.slotCount+i);
                    if(ItemStack.canCombine(currStack, toInsert)){
                        addVals[i]=Math.min(currStack.getMaxCount()-currStack.getCount(),insertAmt);
                        insertAmt-=addVals[i];

                        if(insertAmt<=0) break;
                    }
                }
                if(insertAmt>0) return true;
                for(int i=0;i<36;i++)
                    handler.inventory.getStack(handler.type.slotCount+i).increment(addVals[i]);
            }

            ItemStack toTake1 = trades.get(buttonIndex).getAdjustedFirstBuyItem();
            int toTakeC1 = handler.player.getInventory().remove(stack->stack.isOf(toTake1.getItem()),
                    toTake1.getCount(), handler.player.getInventory());
            ((RemovableInvGUIItem) handler.gui.items[0]).display = toTake1.copyWithCount(toTakeC1);

            ItemStack toTake2 = trades.get(buttonIndex).getSecondBuyItem();
            int toTakeC2 = handler.player.getInventory().remove(stack->stack.isOf(toTake2.getItem()),
                    toTake2.getCount(), handler.player.getInventory());
            ((RemovableInvGUIItem) handler.gui.items[1]).display = toTake1.copyWithCount(toTakeC2);

            gui.setData("builtin.merchantSellingItem", ItemStack.EMPTY);

        }

        gui.setData("builtin.merchantTradeIndex", buttonIndex);
        return true;
    }
    public static void handleTradeSlots(InvGUI<?> gui){
        checkGUIType(gui, "Tried to handle trade slots of gui "+gui+", but the gui is not a merchant gui!",
                ServerGUIs.ScreenType.MERCHANT);

        if(!(gui.items[0] instanceof RemovableInvGUIItem &&
                gui.items[1] instanceof RemovableInvGUIItem &&
                gui.items[2] instanceof RemovableInvGUIItem))
            throw new IllegalStateException("All of this gui's slots must be RemovableInvGUIItems!");

        ItemStack sellingItem=ItemStack.EMPTY;
        try {
            sellingItem=gui.getDataOfType("builtin.merchantSellingItem", ItemStack.class);
            if(sellingItem==null) sellingItem=ItemStack.EMPTY;
        }catch(ClassCastException e){
            throwGuiDataTypeError("builtin.merchantSellingItem", ItemStack.class);
        }
        if(!sellingItem.isEmpty()&&!ItemStack.areEqual(sellingItem,((RemovableInvGUIItem) gui.items[2]).display)){
            gui.getHandler().player.giveItemStack(((RemovableInvGUIItem) gui.items[2]).display);
            try {
                var trade = gui.getDataOfType("builtin.merchantTrade", TradeOffer.class);
                if(trade==null) return;

                if(((RemovableInvGUIItem) gui.items[0]).display.isOf(trade.getAdjustedFirstBuyItem().getItem())) {
                    ((RemovableInvGUIItem) gui.items[0]).display.decrement(trade.getAdjustedFirstBuyItem().getCount());
                    ((RemovableInvGUIItem) gui.items[1]).display.decrement(trade.getSecondBuyItem().getCount());
                }else{
                    ((RemovableInvGUIItem) gui.items[1]).display.decrement(trade.getAdjustedFirstBuyItem().getCount());
                    ((RemovableInvGUIItem) gui.items[0]).display.decrement(trade.getSecondBuyItem().getCount());
                }

            }catch(ClassCastException e){
                throwGuiDataTypeError("builtin.merchantTrade", TradeOffer.class);
                return;
            }

            gui.setData("builtin.merchantTrade", null);
            gui.setData("builtin.merchantSellingItem", ItemStack.EMPTY);
        }

        ItemStack primarySell = ((RemovableInvGUIItem) gui.items[0]).display;
        ItemStack secondarySell = ((RemovableInvGUIItem) gui.items[1]).display;
        if(primarySell.isEmpty()){
            primarySell = secondarySell;
            secondarySell = Items.AIR.getDefaultStack();
        }

        if (primarySell.isEmpty()) {
            ((RemovableInvGUIItem) gui.items[2]).display = ItemStack.EMPTY;
            gui.setData("builtin.merchantTrade", null);
        } else {
            TradeOfferList tradeOfferList;
            try{
                tradeOfferList=gui.getDataOfType("builtin.trades", TradeOfferList.class);
            }catch(ClassCastException e){
                throwGuiDataTypeError("builtin.trades", TradeOfferList.class);
                return;
            }

            int index=-1;
            try{
                index=gui.getDataOfType("builtin.merchantTradeIndex", Integer.class);
            }catch(ClassCastException | NullPointerException ignored){}

            TradeOffer tradeOffer = tradeOfferList.getValidOffer(primarySell, secondarySell, index);
            if (tradeOffer == null || tradeOffer.isDisabled()) {
                tradeOffer = tradeOfferList.getValidOffer(secondarySell, primarySell, index);
            }
            if (tradeOffer != null && !tradeOffer.isDisabled()) {
                ((RemovableInvGUIItem) gui.items[2]).display = tradeOffer.copySellItem();
                gui.setData("builtin.merchantTrade", tradeOffer);
            } else {
                ((RemovableInvGUIItem) gui.items[2]).display = ItemStack.EMPTY;
                gui.setData("builtin.merchantTrade", null);
            }

            gui.setData("builtin.merchantSellingItem", ((RemovableInvGUIItem) gui.items[2]).display.copy());
            gui.getHandler().refresh();
        }
    }
    public static TradeOfferList createTrades(TradeOffer... trades){
        var nbt = new NbtCompound();

        var tradesNBT = new NbtList();
        nbt.put("Recipes", tradesNBT);

        for (TradeOffer trade : trades) {
            tradesNBT.add(trade.toNbt());
        }

        return new TradeOfferList(nbt);
    }
}
