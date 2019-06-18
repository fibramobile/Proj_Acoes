package action.dev.project15.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Calendar;

import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.adapter.SearchAdapter;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.TradeEntity;

/**
 * Fragmento Trade
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class SeachFragment extends Fragment implements View.OnClickListener {

    public MainActivity activity;
    private SearchAdapter adapter;

    private View view;

    private EditText mes;
    private EditText ano;

    /**
     * Fragmento das trocas
     */
    public SeachFragment() {
        adapter = new SearchAdapter();
    }

    /**
     * Carrega o view
     */
    public void preload() {

        view = activity.getLayoutInflater().inflate(R.layout.search,
                activity.findViewById(R.id.container), false);

        view.findViewById(R.id.buscar).setOnClickListener(this);
        mes = view.findViewById(R.id.code);
        ano = view.findViewById(R.id.ano);

        Calendar calendar = Calendar.getInstance();
        mes.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        ano.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        RecyclerView recyclerView = view.findViewById(R.id.list_action);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        activity.toolbar.setTitle(R.string.fragment_title_search);
        activity.enableBackFragment();

        // Busca com o mes e ano atual
        onClick(null);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.trades.clear();
        adapter.notifyDataSetChanged();
        System.gc();
    }

    @Override
    public void onClick(View v) {

        if (mes.getText().length() == 0 || ano.getText().length() == 0) {
            activity.showMessage("Digite o mês e ano para buscar!");
            return;
        }
        if (ano.getText().length() != 4) {
            activity.showMessage("Digite o ano completo!");
            return;
        }

        int m = 0;
        try {
            m = Integer.parseInt(mes.getText().toString());
        } catch (Exception e){
            activity.showMessage("Mês incorreto!");
            return;
        }
        if (m == 0) {
            activity.showMessage("Inicie o mês com 1 ou 01");
            return;
        }
        if (m > 12) {
            activity.showMessage("Requer no máximo 12 no campo mês!");
            return;
        }
        adapter.trades.clear();

        String MesBusca = m < 10 ? "0" + m : String.valueOf(m);
        String AnoBusca = ano.getText().toString();

        for (ActionEntity action : activity.storage.toList()) {
            for (TradeEntity entity : action.trade) {
                if (entity.date != null){
                    String[] parts = entity.date.split("/");
                    if(parts[1].endsWith(MesBusca) && parts[2].equals(AnoBusca)){
                        if (!adapter.trades.contains(entity)) {
                            entity.action = action.name;
                            adapter.trades.add(entity);
                        }
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (adapter.trades.isEmpty()){
            activity.showMessage("Sem resultados!");
        }
    }
}
