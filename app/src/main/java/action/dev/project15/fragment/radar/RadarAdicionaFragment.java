package action.dev.project15.fragment.radar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;

import java.util.Objects;

import action.dev.project15.Constants;
import action.dev.project15.MainActivity;
import action.dev.project15.R;

public class RadarAdicionaFragment extends Fragment {

    public static RadarAdicionaFragment newInstance() {
        return new RadarAdicionaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_radar_adiciona, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        SearchableSpinner spinner = Objects.requireNonNull(getView()).findViewById(R.id.radar_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, Constants.actions);

        spinner.setAdapter(adapter);
        spinner.setTitle("Selecione");
        spinner.setPositiveButton("OK");

        getView().findViewById(R.id.radar_voltar).setOnClickListener(v ->
                ((MainActivity) getActivity()).setFragment(RadarListaFragment.newInstance()));

        getView().findViewById(R.id.radar_adiciona).setOnClickListener(v -> {
            if(spinner.getSelectedItem() == null || spinner.getSelectedItem().toString().length() == 0){
                ((MainActivity) getActivity()).showMessage("Selecione uma ação!");
                return;
            }
            Store store = new Store();
            try {
                store.load(getActivity());
                store.map.put(spinner.getSelectedItem().toString(), true);
                store.save(getActivity());
                ((MainActivity) getActivity()).showMessage("Adicionado!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((MainActivity) getActivity()).setFragment(RadarListaFragment.newInstance());
        });
    }
}
