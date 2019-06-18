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

import java.util.Objects;

import action.dev.project15.MainActivity;
import action.dev.project15.R;

public class BuscaResultFragment extends Fragment {

    private Relatorio relatorio;

    public static BuscaResultFragment newInstance(Relatorio relatorio) {
        BuscaResultFragment fragment = new BuscaResultFragment();
        fragment.relatorio = relatorio;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_busca_result, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = ((MainActivity) Objects.requireNonNull(getActivity()));
        mainActivity.toolbar.setTitle(R.string.fragment_title_busca);
        mainActivity.enableBackFragment();

        BuscaResultAdapter adapter = new BuscaResultAdapter();
        adapter.actions = relatorio.varying;

        RecyclerView recyclerView = Objects.requireNonNull(getView())
                .findViewById(R.id.list_busca);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}