package action.dev.project15.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import action.dev.project15.Constants;
import action.dev.project15.entity.ActionEntity;
import action.dev.project15.entity.TradeEntity;
import action.dev.project15.entity.VaryingEntity;

/**
 * Armazenamento
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class Storage {

    private final static byte[] BUFFER = new byte[1024];

    protected Map<String, ActionEntity> pooMap = new HashMap<>();

    /**
     * Salva o json
     *
     * @param context Context
     */
    public void save(Context context) {

        try {

            OutputStream os = context.openFileOutput(Constants.fileName, Context.MODE_PRIVATE);
            os.write(save().getBytes("UTF-8"));
            os.flush();
            os.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Salva em json para uma String
     *
     * @return String json
     */
    public String save() {

        JSONObject object = new JSONObject();

        for (String key : pooMap.keySet()) {
            try {

                ActionEntity action = pooMap.get(key);

                JSONObject jos = new JSONObject();
                jos.put("name", action.name);

                JSONArray vars = new JSONArray();

                for (VaryingEntity varying : action.varying) {

                    JSONObject josT = new JSONObject();
                    josT.put("v1", varying.V1);
                    josT.put("v2", varying.V2);
                    josT.put("v3", varying.V3);
                    josT.put("date", varying.date);

                    vars.put(josT);
                }
                jos.put("varying", vars);

                JSONArray trs = new JSONArray();

                for (TradeEntity trade : action.trade) {

                    JSONObject josT = new JSONObject();
                    josT.put("price", trade.price);
                    josT.put("value", trade.value);
                    josT.put("type", trade.type);
                    josT.put("date", trade.date);

                    trs.put(josT);
                }

                jos.put("trade", trs);
                object.put(key, jos);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        return object.toString();
    }

    /**
     * Carrega os dados
     *
     * @param context Context
     */
    public void load(Context context) {

        try {
            InputStream inputStream = context.openFileInput(Constants.fileName);

            if (inputStream == null) {
                return;
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read(BUFFER, 0, BUFFER.length)) != -1) {
                buffer.write(BUFFER, 0, read);
            }
            inputStream.close();

            byte[] data = buffer.toByteArray();

            String json = new String(data, "UTF-8");
            load(json);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Carrega a partir da string
     *
     * @param json String com o json
     */
    public void load(String json) {
        try {
            JSONObject object = new JSONObject(json);
            pooMap.clear();

            for (Iterator<String> it = object.keys(); it.hasNext(); ) {
                String key = it.next();

                JSONObject jos = object.getJSONObject(key);

                ActionEntity action = new ActionEntity();
                action.name = jos.getString("name");

                JSONArray vars = jos.getJSONArray("varying");

                for (int i = 0; i < vars.length(); i++) {

                    JSONObject josT = vars.getJSONObject(i);

                    VaryingEntity varying = new VaryingEntity();
                    varying.V1 = josT.getDouble("v1");
                    varying.V2 = josT.getDouble("v2");
                    varying.V3 = josT.getDouble("v3");
                    varying.date = josT.getString("date");

                    action.varying.add(varying);
                }

                JSONArray array = jos.getJSONArray("trade");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject josT = array.getJSONObject(i);

                    TradeEntity trade = new TradeEntity();
                    trade.price = josT.getDouble("price");
                    trade.type = josT.getInt("type");
                    trade.value = josT.getDouble("value");
                    trade.date = josT.getString("date");

                    action.trade.add(trade);
                }

                pooMap.put(key, action);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Cadastra a ação
     *
     * @param trade TradeEntity
     * @param name  Ação
     */
    public void create(TradeEntity trade, String name) {

        if (pooMap.containsKey(name)) {

            pooMap.get(name).trade.add(trade);

        } else {

            ActionEntity action = new ActionEntity();
            action.trade.add(trade);
            action.name = name;

            pooMap.put(name, action);
        }
    }

    /**
     * Gera uma atualização
     *
     * @param name Ação
     * @param date Data
     * @param v1   Valor 1
     * @param v2   Valor 2
     * @param v3   Valor 3
     */
    public void update(String name, String date, double v1, double v2, double v3) {

        if (pooMap.containsKey(name)) {

            VaryingEntity varyingEntity = new VaryingEntity();
            varyingEntity.V1 = v1;
            varyingEntity.V2 = v2;
            varyingEntity.V3 = v3;
            varyingEntity.date = date;

            pooMap.get(name).varying.add(varyingEntity);
        }
    }

    /**
     * Converte o map em list
     *
     * @return List
     */
    public List<ActionEntity> toList() {

        return new ArrayList<>(pooMap.values());
    }

    /**
     * Busca as açãoes
     *
     * @return String[]
     */
    public List<String> getActions() {

        return new ArrayList<>(pooMap.keySet());
    }

    /**
     * Verifica se não está vazio
     *
     * @return Boolean
     */
    public boolean isNotEmpty() {
        return !pooMap.isEmpty();
    }

    /**
     * Remove a troca
     *
     * @param action Ação
     * @param trade  Troca compra ou venda
     */
    public void remove(ActionEntity action, TradeEntity trade) {

        if (!pooMap.containsKey(action.name)) {
            return;
        }

        action.trade.remove(trade);

        if (action.trade.size() == 0) {

            pooMap.remove(action.name);
        }
    }

    /**
     * Remove a variação
     *
     * @param action Ação
     * @param varing Variação
     */
    public void remove(ActionEntity action, VaryingEntity varing) {

        if (!pooMap.containsKey(action.name)) {
            return;
        }

        action.varying.remove(varing);

        if (action.trade.size() == 0) {

            pooMap.remove(action.name);
        }
    }
}
