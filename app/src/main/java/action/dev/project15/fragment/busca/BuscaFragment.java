package action.dev.project15.fragment.busca;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.VaryingEntity;

public class BuscaFragment extends Fragment {

    public static BuscaFragment newInstance() {
        return new BuscaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_busca, container, false);
    }

    public BuscaAdapter adapter;

    public void process(String ano){

        Map<Integer, Relatorio> items = new LinkedHashMap<>();

        items.put(1, new Relatorio());
        Objects.requireNonNull(items.get(1)).mes = "jan.";
        items.put(2, new Relatorio());
        Objects.requireNonNull(items.get(2)).mes = "fev.";
        items.put(3, new Relatorio());
        Objects.requireNonNull(items.get(3)).mes = "mar.";
        items.put(4, new Relatorio());
        Objects.requireNonNull(items.get(4)).mes = "abr.";
        items.put(5, new Relatorio());
        Objects.requireNonNull(items.get(5)).mes = "maio.";
        items.put(6, new Relatorio());
        Objects.requireNonNull(items.get(6)).mes = "jun.";
        items.put(7, new Relatorio());
        Objects.requireNonNull(items.get(7)).mes = "jul.";
        items.put(8, new Relatorio());
        Objects.requireNonNull(items.get(8)).mes = "ago.";
        items.put(9, new Relatorio());
        Objects.requireNonNull(items.get(9)).mes = "set.";
        items.put(10, new Relatorio());
        Objects.requireNonNull(items.get(10)).mes = "out.";
        items.put(11, new Relatorio());
        Objects.requireNonNull(items.get(11)).mes = "nov.";
        items.put(12, new Relatorio());
        Objects.requireNonNull(items.get(12)).mes = "dez.";

        MainActivity mainActivity =  ((MainActivity) Objects.requireNonNull(getActivity()));

        for (ActionEntity actions: mainActivity.storage.toList()){

            for(VaryingEntity varying: actions.varying){

                String[] parts = varying.date.split("/");

                if(parts[2].equals(ano)){

                    int value = Integer.valueOf(parts[1]);
                    if (items.containsKey(value)){

                        varying.code = actions.name;

                        Objects.requireNonNull(items.get(value)).value +=
                                (varying.V1 + varying.V2 + varying.V3);

                        Objects.requireNonNull(items.get(value)).varying.add(varying);
                    }
                }
            }
        }

        adapter.actions.clear();
        adapter.actions.addAll(items.values());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity =  ((MainActivity) Objects.requireNonNull(getActivity()));
        mainActivity.toolbar.setTitle(R.string.fragment_title_busca);
        mainActivity.enableBackFragment();

        adapter = new BuscaAdapter(position -> {
                if(adapter.actions.get(position).varying.size() == 0){
                    mainActivity.showMessage("Sem variação!");
                    return;
                }
                mainActivity.setFragment(BuscaResultFragment.newInstance(adapter.actions.get(position)));
        });

        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.list_busca);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        EditText ano = getView().findViewById(R.id.busca_ano);
        getView().findViewById(R.id.busca_pesquisa).setOnClickListener(v -> {
            if (ano.getText().length() != 4) {
                mainActivity.showMessage("Digite o ano completo!");
                return;
            }
            process(ano.getText().toString());

        });
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        ano.setText(year);
        process(year);
    }
}
