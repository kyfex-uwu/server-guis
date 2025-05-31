package com.kyfexuwu.server_guis;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Optional;

public class WrittenBookFactory {
    private final String title;
    private final String author;
    private int generation;
    private final ArrayList<Text> pageTexts = new ArrayList<>();

    public WrittenBookFactory(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public WrittenBookFactory(String title, String author, Integer generation) {
        this(title, author);
        this.generation = generation == null ? 0 : generation;
    }

    public WrittenBookFactory appendPage(Text page) {
        this.pageTexts.add(page);
        return this;
    }

    public ItemStack build() {
        var toReturn = Items.WRITTEN_BOOK.getDefaultStack();
        toReturn.set(DataComponentTypes.WRITTEN_BOOK_CONTENT, new WrittenBookContentComponent(
                RawFilteredPair.of(FilteredMessage.permitted(this.title)),
                this.author,
                this.generation,
                this.pageTexts.stream().map(text ->
                        new RawFilteredPair<>(text, Optional.of(text))).toList(),
                true
        ));
        return toReturn;
    }
}
