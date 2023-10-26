package com.kyfexuwu.server_guis;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class WrittenBookFactory {
    private final String title;
    private final String author;
    private Integer generation;
    private final ArrayList<Text> pageTexts = new ArrayList<>();
    public WrittenBookFactory(String title, String author){
        this.title=title;
        this.author=author;
    }
    public WrittenBookFactory(String title, String author, Integer generation){
        this(title, author);
        this.generation=generation;
    }
    public WrittenBookFactory appendPage(Text page){
        this.pageTexts.add(page);
        return this;
    }

    public ItemStack build(){
        var toReturn = Items.WRITTEN_BOOK.getDefaultStack();
        var compound = new NbtCompound();

        compound.put("title",NbtString.of(this.title));
        compound.put("author",NbtString.of(this.author));
        if(this.generation!=null)
            compound.put("generation", NbtInt.of(this.generation));

        var pages = new NbtList();
        for(var page : this.pageTexts){
            pages.add(NbtString.of(Text.Serializer.toJson(page)));
        }

        compound.put("pages", pages);
        toReturn.setNbt(compound);
        return toReturn;
    }
}
