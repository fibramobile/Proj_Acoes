package action.dev.project15.fragment.lucro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.TradeEntity;

public class LucroFragment extends Fragment {

    public static LucroFragment newInstance() {
        return new LucroFragment();
    }

    private LucroAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lucro, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = ((MainActivity) Objects.requireNonNull(getActivity()));
        mainActivity.toolbar.setTitle(R.string.fragment_title_lucro);
        mainActivity.enableBackFragment();

        adapter = new LucroAdapter(r -> {
            mainActivity.setFragment(mainActivity.tradeFragment);
            mainActivity.tradeFragment.update(adapter.actions.get(r).action);
        });

        for (ActionEntity action : mainActivity.storage.toList()) {

            Media media = new Media();
            media.name = action.name;

            boolean found = false;

            double buy = 0;
            double buy_value = 0;

            double sell = 0;
            double sell_value = 0;

            for (TradeEntity trade : action.trade) {

                if (trade.type == TradeEntity.TYPE_BUY) {

                    buy = trade.price;
                    buy_value = trade.value;

                } else if (trade.type == TradeEntity.TYPE_SELL) {

                    sell = trade.price;
                    sell_value = trade.value;

                    found = true;
                }
            }
            if (buy_value != 0) {

                // 50 / 2 = 25
                double precoItem = buy / buy_value;

                // 25 * 1 = 25
                double investido = precoItem * sell_value;

                // 70 * 1 = 70
                double total = (sell * sell_value);

                // 70 - 25 = 45
                media.value = total - investido;
                media.porcent = ((total - investido) / (total)) * 100;

                media.action = action;
                if (found) adapter.actions.add(media);
            }
        }

        RecyclerView recyclerView = Objects.requireNonNull(getView())
                .findViewById(R.id.list_lucro);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}
