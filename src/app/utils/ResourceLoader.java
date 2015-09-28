package app.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jesse on 9/27/15.
 */
class ResourceLoader {

    static String getResource(String rsc) {
        String val = "";

        try {
            InputStream i = ResourceLoader.class.getResourceAsStream(rsc);
            BufferedReader r = new BufferedReader(new InputStreamReader(i));

            String l;
            while((l = r.readLine()) != null) {
                val = val + l;
            }
            i.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return val;
    }
}
