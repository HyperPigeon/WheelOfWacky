package net.hyper_pigeon.wacky_wheel.token;

import net.minecraft.item.Item;

public class Token {
    private Item item;
    private int count;

    public Token(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem(){
        return item;
    }

    public int getCount(){
        return count;
    }
}
