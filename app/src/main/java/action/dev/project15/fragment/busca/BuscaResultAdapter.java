package action.dev.project15.fragment.busca;

import android.support.annotation.NonNull;
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

public class BuscaResultAdapter extends RecyclerView.Adapter<BuscaResultAdapter.Item> {

    public List<VaryingEntity> actions = new ArrayList<>();

    static class Item extends RecyclerView.ViewHolder {

        private TextView valorA;
        private TextView valorB;
        private TextView valorC;
        private TextView date;
        private TextView code;

        Item(View v) {
            super(v);
            code = v.findViewById(R.id.code);
            date = v.findViewById(R.id.varying_date);
            valorA = v.findViewById(R.id.varying_v1);
            valorB = v.findViewById(R.id.varying_v2);
            valorC = v.findViewById(R.id.varying_v3);
        }
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_busca_result_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.date.setText(actions.get(position).date);
        holder.code.setText(actions.get(position).code);
        holder.valorA.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).V1));
        holder.valorB.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).V2));
        holder.valorC.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).V3));
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

}