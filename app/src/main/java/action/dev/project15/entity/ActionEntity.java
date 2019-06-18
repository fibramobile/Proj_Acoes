package action.dev.project15.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade da ação
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class ActionEntity {

    public String name = null;

    public double price = 0;

    public List<TradeEntity> trade = new ArrayList<>();
    public List<VaryingEntity> varying = new ArrayList<>();

    /**
     * Calcula a média dos valoers
     *
     * @return double
     */
    public double media() {
        if (trade.size() == 0) return 0;
        double total = 0;
        int total_items = 0;
        for (TradeEntity poo : trade) {
            if (poo.type == TradeEntity.TYPE_SELL) continue;
            total += poo.price;
            total_items++;
        }
        return total / total_items;
    }


    /**
     * Calcula o valor total das ações
     *
     * @return double
     */
    public double value() {
        if (trade.size() == 0) return 0;
        double total = 0;
        for (TradeEntity poo : trade) {
            if (poo.type == TradeEntity.TYPE_BUY) {
                total += poo.value;
            } else {
                total -= poo.value;
            }
        }
        return total;
    }

    /**
     * Retorna a soma de v1 + v2 + v3
     *
     * @return double
     */
    public double total() {

        if (varying.size() == 0) return 0;

        int total = 0;
        double value = 0;

        for (VaryingEntity poo : varying) {

            total++;
            value += (poo.V1 + poo.V2 + poo.V3) / 3;

        }
        return value / total;
    }
}