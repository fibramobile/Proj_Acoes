package action.dev.project15.fragment.busca;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import action.dev.project15.R;

public class BuscaAdapter extends RecyclerView.Adapter<BuscaAdapter.Item> {

    public List<Relatorio> actions = new ArrayList<>();

    public interface Callback {

        void call(int position);
    }

    private Callback callback;

    BuscaAdapter(Callback callback) {
        this.callback = callback;
    }

    static class Item extends RecyclerView.ViewHolder {

        private ConstraintLayout root;
        private TextView valorA;
        private TextView valorB;

        Item(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            valorA = v.findViewById(R.id.valueA);
            valorB = v.findViewById(R.id.valueB);
        }
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Item(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_busca_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.valorA.setText(actions.get(position).mes);
        holder.valorB.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).value));
        holder.root.setOnClickListener(v -> {
            if (callback != null) {
                callback.call(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

}