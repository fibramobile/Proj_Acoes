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
import action.dev.project15.entity.ActionEntity;

/**
 * Recycle View do ActionEntity
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.MHolder> {

    public interface Callback {

        /**
         * Ação do botão visualizar
         *
         * @param action ActionEntity
         */
        void view(ActionEntity action);

        /**
         * Ação do botão Historico
         *
         * @param action ActionEntity
         */
        void histoy(ActionEntity action);

    }

    public List<ActionEntity> actions = new ArrayList<>();

    private Callback callback;

    /**
     * Inicializa o adapater
     *
     * @param callback Callback
     */
    public ActionAdapter(Callback callback) {
        this.callback = callback;
    }

    /**
     * Holder
     */
    static class MHolder extends RecyclerView.ViewHolder {

        private TextView action;
        private TextView price;
        private TextView media;
        private TextView total;
        private TextView value;
        private TextView history;
        private TextView access;


        MHolder(View v) {

            super(v);

            action = v.findViewById(R.id.adapter_action);
            media = v.findViewById(R.id.adapter_media);
            total = v.findViewById(R.id.adapter_total);
            price = v.findViewById(R.id.adapter_price);
            value = v.findViewById(R.id.adapter_value);
            history = v.findViewById(R.id.adapter_history);
            access = v.findViewById(R.id.adapter_view);
        }
    }

    @Override
    public ActionAdapter.MHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home, parent, false));
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {

        holder.action.setText(actions.get(position).name);
        holder.media.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).media()));
        holder.total.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).total()));
        holder.price.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).price));
        holder.value.setText(String.format(Locale.getDefault(), "%.2f",
                actions.get(position).value()));

        holder.access.setOnClickListener(v -> callback.view(actions.get(position)));
        holder.history.setOnClickListener(v -> callback.histoy(actions.get(position)));
    }

    @Override
    public int getItemCount() {

        return actions.size();
    }
}