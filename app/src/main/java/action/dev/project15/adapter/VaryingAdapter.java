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
import action.dev.project15.entity.VaryingEntity;

/**
 * Recycle View do VaryingEntity
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class VaryingAdapter extends RecyclerView.Adapter<VaryingAdapter.MHolder> {

    public interface Callback {

        /**
         * Botão Remover
         *
         * @param varying VaryingEntity
         */
        void remove(VaryingEntity varying);

        /**
         * Botão Editar
         *
         * @param varying VaryingEntity
         */
        void edit(VaryingEntity varying);

    }

    public List<VaryingEntity> varying = new ArrayList<>();

    private Callback callback;

    /**
     * Inicializa o adapter
     *
     * @param callback Callback
     */
    public VaryingAdapter(Callback callback) {
        this.callback = callback;
    }

    /**
     * Holder
     */
    static class MHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView v1;
        private TextView v2;
        private TextView v3;
        private TextView remove;
        private TextView edit;

        MHolder(View v) {

            super(v);

            date = v.findViewById(R.id.varying_date);
            v1 = v.findViewById(R.id.varying_v1);
            v2 = v.findViewById(R.id.varying_v2);
            v3 = v.findViewById(R.id.varying_v3);
            remove = v.findViewById(R.id.varying_remove);
            edit = v.findViewById(R.id.varying_edit);
        }
    }

    @Override
    public VaryingAdapter.MHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_varying, parent, false));
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {

        holder.date.setText(varying.get(position).date);
        holder.v1.setText(String.format(Locale.getDefault(), "%.2f R$",
                varying.get(position).V1));
        holder.v2.setText(String.format(Locale.getDefault(), "%.2f R$",
                varying.get(position).V2));
        holder.v3.setText(String.format(Locale.getDefault(), "%.2f R$",
                varying.get(position).V3));

        holder.remove.setOnClickListener(v -> callback.remove(varying.get(position)));
        holder.edit.setOnClickListener(v -> callback.edit(varying.get(position)));
    }

    @Override
    public int getItemCount() {

        return varying.size();
    }
}
