package action.dev.project15.helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileIO {

    public static void save(Context context, String text, String name) {
        try {
            OutputStream os = context.openFileOutput(name, Context.MODE_PRIVATE);
            os.write(text.toString().getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String load(Context context, String name) {
        try {
            InputStream is = context.openFileInput(name);
            if (is == null) return null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String next;
            StringBuilder builder = new StringBuilder();
            while ((next = br.readLine()) != null) {
                builder.append(next).append("\n");
            }
            br.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
