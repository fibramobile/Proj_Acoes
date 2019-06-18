package action.dev.project15.fragment.radar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import action.dev.project15.MainActivity;
import action.dev.project15.R;

public class RadarListaFragment extends Fragment {

    public Store store = new Store();
    public RadarAdapter adapter = new RadarAdapter();

    public static RadarListaFragment newInstance() {
        return new RadarListaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_radar_lista, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getView()).findViewById(R.id.radar_add)
                .setOnClickListener(v -> ((MainActivity) getActivity())
                        .setFragment(RadarAdicionaFragment.newInstance()));
        store.load(getActivity());

        List<String> keys = new ArrayList<>();
        for (Iterator<String> it = store.map.keys(); it.hasNext(); ) {
            String key = it.next();
            keys.add(key);
        }

        MainActivity mainActivity = ((MainActivity) Objects.requireNonNull(getActivity()));
        mainActivity.toolbar.setTitle(R.string.fragment_title_radar);
        mainActivity.enableBackFragment();

        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.radar_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        mainActivity.finance.radarQuote((itens) -> {
            adapter.actions.clear();
            adapter.actions.addAll(itens);
            adapter.notifyDataSetChanged();
        }, keys);
    }

    @Override
    public void onStop() {
        super.onStop();
        store.save(getActivity());
    }
}
