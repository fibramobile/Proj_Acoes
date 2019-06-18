package action.dev.project15.fragment.radar;

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

public class RadarAdapter extends RecyclerView.Adapter<RadarAdapter.MHolder> {

    public List<RadarQuote> actions = new ArrayList<>();

    static class MHolder extends RecyclerView.ViewHolder {

        private TextView valueA;
        private TextView valueB;

        MHolder(View v) {
            super(v);
            valueA = v.findViewById(R.id.valueA);
            valueB = v.findViewById(R.id.valueB);
        }
    }

    @NonNull
    @Override
    public RadarAdapter.MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_radar_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        holder.valueA.setText(actions.get(position).name);
        holder.valueB.setText(String.format(Locale.getDefault(), "%.2f R$",
                actions.get(position).price));
    }

    @Override
    public int getItemCount() { return actions.size(); }

}