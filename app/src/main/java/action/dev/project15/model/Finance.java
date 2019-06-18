package action.dev.project15.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import action.dev.project15.fragment.radar.RadarQuote;

/**
 * API Cotação
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class Finance {

    public interface Callback {

        /**
         * Executa após o download das cotações
         */
        void run();
    }

    public interface Callback2 {

        /**
         * Executa após o download das cotações
         */
        void run(List<RadarQuote> itens);
    }

    private RequestQueue queue;

    /**
     * Inicia o volley
     *
     * @param context Contexto
     */
    public Finance(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    public void radarQuote(Callback2 callback, List<String> names) {
        StringBuilder sb = new StringBuilder();
        for (String action : names) sb.append(action).append(".sa,");
        if (sb.length() == 0) {
            callback.run(new ArrayList<>());
            return;
        }
        String url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=%s&fields=regularMarketPrice";
        StringRequest req = new StringRequest(Request.Method.GET,
                String.format(url, sb.substring(0, sb.length() - 1)),
                response -> {
                    if (response == null) {
                        return;
                    }
                    List<RadarQuote> resposta = new ArrayList<>();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.has("quoteResponse")) {
                            return;
                        }
                        JSONObject quoteResponse = object.getJSONObject("quoteResponse");
                        if (!quoteResponse.has("result")) {
                            return;
                        }
                        JSONArray array = quoteResponse.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String name = obj.getString("symbol")
                                    .toUpperCase()
                                    .replace(".SA", "");

                            RadarQuote quote = new RadarQuote();
                            quote.name = name;
                            quote.price = obj.getDouble("regularMarketPrice");
                            resposta.add(quote);
                        }
                    } catch (JSONException e) {
                        callback.run(new ArrayList<>());
                    } finally {
                        callback.run(resposta);
                    }
                },
                error -> callback.run(new ArrayList<>()));

        queue.add(req);
    }

    /**
     * Busca a cotação
     *
     * @param callback Callback Resposta
     * @param storage  Storage armazenamento
     */
    public void getQuote(Callback callback, Storage storage) {

        StringBuilder sb = new StringBuilder();

        for (String action : storage.pooMap.keySet()) {
            sb.append(action).append(".sa,");
        }

        if (sb.length() == 0) {
            callback.run();
            return;
        }

        String url = "https://query1.finance.yahoo.com/v7/finance/quote?" +
                "symbols=%s&fields=regularMarketPrice";

        StringRequest req = new StringRequest(Request.Method.GET,
                String.format(url, sb.substring(0, sb.length() - 1)),
                response -> {
                    if (response == null) {
                        return;
                    }
                    try {

                        JSONObject object = new JSONObject(response);
                        if (!object.has("quoteResponse")) {
                            return;
                        }

                        JSONObject quoteResponse = object.getJSONObject("quoteResponse");
                        if (!quoteResponse.has("result")) {
                            return;
                        }
                        JSONArray array = quoteResponse.getJSONArray("result");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String name = obj.getString("symbol")
                                    .toUpperCase()
                                    .replace(".SA", "");
                            storage.pooMap.get(name)
                                    .price = obj.getDouble("regularMarketPrice");
                        }

                    } catch (JSONException e) {

                        callback.run();

                    } finally {

                        callback.run();
                    }

                },
                error -> callback.run());

        queue.add(req);
    }
}
