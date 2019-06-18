package action.dev.project15.fragment.lucro;

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

public class LucroAdapter extends RecyclerView.Adapter<LucroAdapter.MHolder> {

    public List<Media> actions = new ArrayList<>();

    public interface Callback {

        void call(int position);
    }

    private Callback callback;

    LucroAdapter(Callback callback) {
        this.callback = callback;
    }

    static class MHolder extends RecyclerView.ViewHolder {

        private TextView valueA;
        private TextView valueB;
        private TextView valueC;
        private ConstraintLayout root;

        MHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            valueA = v.findViewById(R.id.valueA);
            valueB = v.findViewById(R.id.valueB);
            valueC = v.findViewById(R.id.valueC);
        }
    }

    @NonNull
    @Override
    public LucroAdapter.MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_lucro_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        holder.valueA.setText(actions.get(position).name);
        holder.valueB.setText(String.format(Locale.getDefault(), "%.2f",
                actions.get(position).porcent));
        holder.valueC.setText(String.format(Locale.getDefault(), "%.2f R$",
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