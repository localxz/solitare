public class Card {
    private int rank;
    private String suit;
    private Boolean isFaceUp;
    private String[] list = {"Ace", "Two", "Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King"};

    public Card(int r, String s)
    {
        rank = r;
        suit = s;
        isFaceUp = false;
    }
    public int getRank()
    {return rank;}
    public String getSuit()
    {return suit;}
    public boolean isRed()
    {return (suit == "d" || suit == "h");}
    public boolean isFaceUp()
    {return isFaceUp;}
    public void turnUp()
    {isFaceUp = true;}
    public void turnDown()
    {isFaceUp = false;}
    private String rankString()
    { return list[rank-1];}
    public String getFileName()
    {
        String res = ""+rank;
        if(rank == 1)
        res = "a";
        else if(rank == 11)
        res = "j";
        else if(rank == 10)
        res = "t";
        else if(rank == 12)
        res = "q";
        else if(rank == 13)
        res = "k";
        if(isFaceUp() == false)
        {
            return "cards\\back.gif";
        }
        return "cards\\"+res+suit+ ".gif";
    }
    public String toString()
    {
        return getFileName();
    }
}
