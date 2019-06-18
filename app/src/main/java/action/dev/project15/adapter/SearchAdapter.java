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
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MHolder> {

    public List<TradeEntity> trades = new ArrayList<>();

    /**
     * Holder
     */
    static class MHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView type;
        private TextView value;
        private TextView price;

        private TextView action;

        MHolder(View v) {

            super(v);
            action = v.findViewById(R.id.action);

            date = v.findViewById(R.id.trades_date);
            type = v.findViewById(R.id.trades_type);
            value = v.findViewById(R.id.trades_value);
            price = v.findViewById(R.id.trades_price);
            value = v.findViewById(R.id.trades_value);
        }
    }

    @Override
    public SearchAdapter.MHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search, parent, false));
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {

        if (trades.get(position).type == TradeEntity.TYPE_BUY) {
            holder.type.setText(R.string.trade_msg_1);
        } else {
            holder.type.setText(R.string.trade_msg_0);
        }
        holder.action.setText(trades.get(position).action);
        holder.date.setText(trades.get(position).date);
        holder.value.setText(String.format(Locale.getDefault(), "%.2f R$",
                trades.get(position).value));
        holder.price.setText(String.format(Locale.getDefault(), "%.2f R$",
                trades.get(position).price));
    }

    @Override
    public int getItemCount() {
        return trades.size();
    }
}
