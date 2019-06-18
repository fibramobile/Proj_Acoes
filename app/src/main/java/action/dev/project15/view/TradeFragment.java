package action.dev.project15.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.adapter.TradeAdapter;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.TradeEntity;

/**
 * Fragmento Trade
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class TradeFragment extends Fragment {

    public MainActivity activity;

    private TradeAdapter adapter;

    private View view;

    private ActionEntity action;

    /**
     * Fragmento das trocas
     */
    public TradeFragment() {

        adapter = new TradeAdapter(new TradeAdapter.Callback() {

            @Override
            public void remove(TradeEntity trade) {

                Snackbar bar = Snackbar.make(activity.findViewById(R.id.root),
                        R.string.trade_msg_7, Snackbar.LENGTH_LONG);
                bar.getView().setBackgroundColor(0xFFFF1818);
                bar.setActionTextColor(0xFFFFFFFF);
                bar.setAction("Sim", v -> {
                    activity.storage.remove(action, trade);
                    update(action);
                });
                bar.show();
            }

            @Override
            public void edit(TradeEntity trade) {

                activity.setFragment(activity.registryFragment);
                activity.registryFragment.setEditMode(action, trade);
            }
        });
    }

    /**
     * Carrega o view
     */
    public void preload() {

        view = activity.getLayoutInflater().inflate(R.layout.trades,
                activity.findViewById(R.id.container), false);

        RecyclerView recyclerView = view.findViewById(R.id.list_action);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {

        activity.toolbar.setTitle(R.string.fragment_title_trade);
        activity.enableBackFragment();

        return view;
    }

    @Override
    public void onStop() {

        super.onStop();

        adapter.trades.clear();
        adapter.notifyDataSetChanged();
        System.gc();
    }

    /**
     * Adiciona as trocas
     */
    public void update(ActionEntity action) {

        this.action = action;

        adapter.trades.clear();
        adapter.trades.addAll(action.trade);
        adapter.notifyDataSetChanged();

        if(action.trade.size() == 0){

            activity.showMessage("Sem trocas!");
            activity.setFragment(activity.homeFragment);
        }
    }
}
