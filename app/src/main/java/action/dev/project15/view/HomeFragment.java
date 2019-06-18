package action.dev.project15.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.adapter.ActionAdapter;
import action.dev.project15.entity.ActionEntity;

/**
 * Fragmento Inicial
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class HomeFragment extends Fragment {

    public MainActivity activity;

    private ActionAdapter adapter;
    private View view;

    /**
     * Inicializa o fragmento
     */
    public HomeFragment() {

        adapter = new ActionAdapter(new ActionAdapter.Callback() {

            @Override
            public void view(ActionEntity action) {
                activity.setFragment(activity.tradeFragment);
                activity.tradeFragment.update(action);
            }

            @Override
            public void histoy(ActionEntity action) {
                activity.setFragment(activity.varyingFragment);
                activity.varyingFragment.update(action);
            }
        });
    }

    /**
     * Carrega a view do fragmento
     */
    public void preload(){

        view = activity.getLayoutInflater().inflate(R.layout.home, activity.findViewById(R.id.container),false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = view.findViewById(R.id.list_action);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {

        return view;
    }

    @Override
    public void onResume(){

        super.onResume();

        activity.toolbar.setTitle(R.string.fragment_title_home);
        activity.disableBackFragment();

        update();
    }

    @Override
    public void onStop() {

        super.onStop();

        adapter.actions.clear();
        adapter.notifyDataSetChanged();

        System.gc();
    }

    /**
     * Atualiza os dados
     */
    public void update() {

        if (!isVisible()) return;

        adapter.actions = activity.storage.toList();
        adapter.notifyDataSetChanged();
    }
}