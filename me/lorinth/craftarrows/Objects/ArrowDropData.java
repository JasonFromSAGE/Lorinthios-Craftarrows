package me.lorinth.craftarrows.Objects;

import java.util.Random;

public class ArrowDropData {
    public boolean DropFixedAmount;
    public int Amount;
    public int Min;
    public int Max;
    private Random random = new Random();

    public int getValue(){
        if(DropFixedAmount)
            return Amount;
        return random.nextInt(Max-Min) + Min;
    }
}

