package action.dev.project15.entity;

/**
 * Classe da compra/venda ação
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class TradeEntity {

    public static final int TYPE_SELL = 1;
    public static final int TYPE_BUY = 2;

    public String date;

    public int type;

    public double price;
    public double value;

    /* 08/04/2018 Apenas adapter*/
    public String action;

}