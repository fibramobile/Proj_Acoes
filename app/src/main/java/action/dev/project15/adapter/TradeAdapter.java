package action.dev.project15.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import action.dev.project15.R;
import action.dev.project15.entity.TradeEntity;

/**
 * Recycle View do ActionEntity
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.MHolder> {

    public interface Callback {

        /**
         * Botão Remover
         *
         * @param action ActionEntity
         */
        void remove(TradeEntity action);

        /**
         * Botão Editar
         *
         * @param action ActionEntity
         */
        void edit(TradeEntity action);

    }

    public List<TradeEntity> trades = new ArrayList<>();

    private Callback callback;

    /**
     * Inicializa o adapter
     *
     * @param callback Callback
     */
    public TradeAdapter(Callback callback) {
        this.callback = callback;
    }

    /**
     * Holder
     */
    static class MHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView type;
        private TextView value;
        private TextView price;
        private TextView remove;
        private TextView edit;

        MHolder(View v) {

            super(v);

            date = v.findViewById(R.id.trades_date);
            type = v.findViewById(R.id.trades_type);
            value = v.findViewById(R.id.trades_value);
            price = v.findViewById(R.id.trades_price);
            value = v.findViewById(R.id.trades_value);
            remove = v.findViewById(R.id.trades_remove);
            edit = v.findViewById(R.id.trades_edit);
        }
    }

    @Override
    public TradeAdapter.MHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_trades, parent, false));
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {

        if (trades.get(position).type == TradeEntity.TYPE_BUY) {
            holder.type.setText(R.string.trade_msg_1);
        } else {
            holder.type.setText(R.string.trade_msg_0);
        }

        holder.date.setText(trades.get(position).date);
        holder.value.setText(String.format(Locale.getDefault(), "%.2f",
                trades.get(position).value));
        holder.price.setText(String.format(Locale.getDefault(), "%.2f R$",
                trades.get(position).price));

        holder.remove.setOnClickListener(v -> callback.remove(trades.get(position)));
        holder.edit.setOnClickListener(v -> callback.edit(trades.get(position)));
    }

    @Override
    public int getItemCount() {

        return trades.size();
    }
}
