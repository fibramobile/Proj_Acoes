package action.dev.project15.view;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Locale;

import action.dev.project15.Constants;
import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.VaryingEntity;

/**
 * Fragmento Update
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class UpdateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public MainActivity activity;

    private View view;
    private EditText date;
    private ArrayAdapter<String> adapter;
    private Button finish;

    private EditText v1;
    private EditText v2;
    private EditText v3;

    public ActionEntity actionEntity;
    public VaryingEntity varyingEntity;

    /**
     * Carrega o view
     */
    public void preload() {

        Spinner action;
        DatePickerDialog picker;
        ImageButton date_picker;

        view = activity.getLayoutInflater().inflate(R.layout.update,
                activity.findViewById(R.id.container), false);

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        picker = new DatePickerDialog(activity, this, y, m, d);

        action = view.findViewById(R.id.update_action);
        v1 = view.findViewById(R.id.update_value1);
        v2 = view.findViewById(R.id.update_value2);
        v3 = view.findViewById(R.id.update_value3);
        date = view.findViewById(R.id.update_date);
        finish = view.findViewById(R.id.update_finish);
        date_picker = view.findViewById(R.id.update_date_picker);

        v1.setText("0.0");
        v2.setText("0.0");
        v3.setText("0.0");

        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        action.setAdapter(adapter);

        date_picker.setOnClickListener(v -> picker.show());

        finish.setOnClickListener(v -> {

            String strV1 = v1.getText().toString().replace(",", ".");
            String strV2 = v2.getText().toString().replace(",", ".");
            String strV3 = v3.getText().toString().replace(",", ".");

            String strDate = date.getText().toString();

            if (strV1.length() == 0) {
                activity.showMessage(getString(R.string.update_val_0));
                return;
            }
            if (!strV1.matches(Constants.DOUBLE_REGEX)) {
                activity.showMessage(getString(R.string.update_val_1));
                return;
            }
            if (strV2.length() == 0) {
                activity.showMessage(getString(R.string.update_val_2));
                return;
            }
            if (!strV2.matches(Constants.DOUBLE_REGEX)) {
                activity.showMessage(getString(R.string.update_val_3));
                return;
            }
            if (strV3.length() == 0) {
                activity.showMessage(getString(R.string.update_val_4));
                return;
            }
            if (!strV3.matches(Constants.DOUBLE_REGEX)) {
                activity.showMessage(getString(R.string.update_val_5));
                return;
            }
            if (strDate.length() == 0) {
                activity.showMessage(getString(R.string.update_val_6));
                return;
            }
            if (!strDate.matches(Constants.DATE_REGEX)) {
                activity.showMessage(getString(R.string.update_val_7));
                return;
            }

            if (varyingEntity == null) {

                activity.storage.update(action.getSelectedItem().toString(),
                        strDate, Double.parseDouble(strV1),
                        Double.parseDouble(strV2),
                        Double.parseDouble(strV3));

                activity.showMessage(getString(R.string.update_val_8));

            } else {

                varyingEntity.date = strDate;

                varyingEntity.V1 =  Double.parseDouble(strV1);
                varyingEntity.V2 =  Double.parseDouble(strV2);
                varyingEntity.V3 =  Double.parseDouble(strV3);

                activity.showMessage(getString(R.string.update_val_9));
            }

            v1.setText("0.0");
            v2.setText("0.0");
            v3.setText("0.0");
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        activity.enableBackFragment();

        if (actionEntity == null) {

            activity.toolbar.setTitle(R.string.fragment_title_update);
            finish.setText(R.string.fragment_title_registry);

            v1.setText("");
            v2.setText("");
            v3.setText("");
            date.setText("");

            adapter.clear();
            adapter.addAll(activity.storage.getActions());

        } else {

            activity.toolbar.setTitle(R.string.update_msg_6);
            finish.setText(R.string.update_msg_6);

            v1.setText(String.format(Locale.getDefault(), "%.2f", varyingEntity.V1));
            v2.setText(String.format(Locale.getDefault(), "%.2f", varyingEntity.V2));
            v3.setText(String.format(Locale.getDefault(), "%.2f", varyingEntity.V3));
            date.setText(varyingEntity.date);

            adapter.clear();
            adapter.add(actionEntity.name);
        }

        activity.enableBackFragment();
    }

    /**
     * Define o modo para editação
     *
     * @param actionEntity  ActionEntity
     * @param varyingEntity VaryingEntity
     */
    public void setEditMode(ActionEntity actionEntity, VaryingEntity varyingEntity) {

        this.varyingEntity = varyingEntity;
        this.actionEntity = actionEntity;

        adapter.clear();
        adapter.add(actionEntity.name);
    }

    /**
     * Define o modo para criação
     */
    public void setRegistryMode() {

        varyingEntity = null;
        actionEntity = null;

        adapter.clear();
        adapter.addAll(Constants.actions);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        if (date != null) {

            month++;

            String fd = (day < 10) ? "0" + day : "" + day;
            String fm = (month < 10) ? "0" + month : "" + month;

            date.setText(String.format(Locale.getDefault(), "%s/%s/%d", fd, fm, year));
        }
    }
}