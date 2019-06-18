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
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Locale;

import action.dev.project15.Constants;
import action.dev.project15.MainActivity;
import action.dev.project15.R;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.TradeEntity;

/**
 * Fragmento do Registro

 * Finalizado: OK
 * Revisado: OK
 */
public class RegistryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public MainActivity activity;

    private ArrayAdapter<String> adapter;
    private View view;

    public EditText date;
    public EditText price;
    public EditText value;

    private RadioButton buy;
    private RadioButton sell;
    private Button finish;

    public TradeEntity tradeEntity;
    public ActionEntity actionEntity;

    /**
     * Carrega a view do fragmento
     */
    public void preload() {

        Spinner action;
        DatePickerDialog picker;

        view = activity.getLayoutInflater().inflate(R.layout.registry,
                activity.findViewById(R.id.container),
                false);

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        picker = new DatePickerDialog(activity, this, y, m, d);

        action = view.findViewById(R.id.registry_action);
        price = view.findViewById(R.id.registry_price);
        value = view.findViewById(R.id.registry_value);
        date = view.findViewById(R.id.registry_date);
        buy = view.findViewById(R.id.registry_buy);
        sell = view.findViewById(R.id.registry_sell);

        finish = view.findViewById(R.id.registry_finish);
        ImageButton date_picker = view.findViewById(R.id.registry_date_picker);

        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        action.setAdapter(adapter);

        date_picker.setOnClickListener(v -> picker.show());

        finish.setOnClickListener(v -> {

            String strPrice = price.getText().toString().replace(",", ".");
            String strValue = value.getText().toString().replace(",", ".");
            String strDate = date.getText().toString();

            if (strPrice.length() == 0) {
                activity.showMessage(getString(R.string.registry_val_0));
                return;
            }
            if (!strPrice.matches(Constants.DOUBLE_REGEX)) {
                activity.showMessage(getString(R.string.registry_val_1));
                return;
            }
            if (strValue.length() == 0) {
                activity.showMessage(getString(R.string.registry_val_2));
                return;
            }
            if (!strValue.matches(Constants.DOUBLE_REGEX)) {
                activity.showMessage(getString(R.string.registry_val_3));
                return;
            }
            if (strDate.length() == 0) {
                activity.showMessage(getString(R.string.registry_val_4));
                return;
            }
            if (!strDate.matches(Constants.DATE_REGEX)) {
                activity.showMessage(getString(R.string.registry_val_5));
                return;
            }

            if (tradeEntity != null) {

                // Edit

                tradeEntity.date = strDate;
                tradeEntity.price = Double.parseDouble(strPrice);
                tradeEntity.value = Double.parseDouble(strValue);

                if (buy.isChecked()) {

                    tradeEntity.type = TradeEntity.TYPE_BUY;

                } else {

                    tradeEntity.type = TradeEntity.TYPE_SELL;
                }

                activity.showMessage(getString(R.string.registry_val_7));

            } else {

                // Create

                TradeEntity trade = new TradeEntity();
                trade.date = strDate;
                trade.price = Double.parseDouble(strPrice);
                trade.value = Double.parseDouble(strValue);

                if (buy.isChecked()) {

                    trade.type = TradeEntity.TYPE_BUY;

                } else {

                    trade.type = TradeEntity.TYPE_SELL;
                }

                activity.storage.create(trade, action.getSelectedItem().toString());

                action.setSelection(0);
                price.setText("");
                value.setText("");
                date.setText("");
                buy.setChecked(true);

                activity.showMessage(getString(R.string.registry_val_6));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {

        view.invalidate();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        if (tradeEntity == null) {

            activity.toolbar.setTitle(R.string.fragment_title_registry);

            finish.setText(R.string.fragment_title_registry);

            price.setText("");
            value.setText("");
            date.setText("");

            buy.setChecked(true);

        } else {

            activity.toolbar.setTitle(R.string.registry_msg_6);

            finish.setText(R.string.registry_msg_6);

            price.setText(String.format(Locale.getDefault(), "%.2f", this.tradeEntity.price));
            value.setText(String.format(Locale.getDefault(), "%.2f", this.tradeEntity.value));
            date.setText(tradeEntity.date);

            if (this.tradeEntity.type == TradeEntity.TYPE_BUY) {

                buy.setChecked(true);

            } else {

                sell.setChecked(true);
            }
        }

        activity.enableBackFragment();
    }

    /**
     * Define o modo para editação
     *
     * @param actionEntity ActionEntity
     * @param tradeEntity  TradeEntity
     */
    public void setEditMode(ActionEntity actionEntity, TradeEntity tradeEntity) {

        this.tradeEntity = tradeEntity;
        this.actionEntity = actionEntity;

        adapter.clear();
        adapter.add(actionEntity.name);
    }

    /**
     * Define o modo para criação
     */
    public void setRegistryMode() {

        tradeEntity = null;
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