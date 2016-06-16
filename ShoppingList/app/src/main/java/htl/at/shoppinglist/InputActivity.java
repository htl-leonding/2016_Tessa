package htl.at.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by pautzi on 19.05.16.
 */
public class InputActivity extends Activity {
    private static String BASE_URL="http://"+BuildConfig.LOCAL_IP+":8080/Tessa/rs/shoppinglist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Button button_save = (Button) findViewById(R.id.insert_add);
        Button button_cancel = (Button)findViewById(R.id.insert_cancel);
        final EditText text_title = (EditText) findViewById(R.id.insert_name);
        final EditText text_pieces = (EditText) findViewById(R.id.insert_amount);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputActivity.this.finish();
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            //TODO save to database
            @Override
            public void onClick(View v) {

                try {
                    URL url = new URL(BASE_URL);
                    if (url != null) {
                        PostProduct productTask = new PostProduct(new Product(0L,text_title.getText().toString(),text_pieces.getText().toString()));
                        productTask.execute();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                InputActivity.this.finish();
            }
        });
    }

    public class PostProduct extends AsyncTask<Void, Void, Boolean> {
        private final String TAG = getClass().getSimpleName();
        private Context mContext;
        private Product mProduct;

        public PostProduct( Product product) {
            super();
            this.mProduct = product;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            URL url;
            HttpURLConnection client = null;
            try{
                url = new URL(BASE_URL);
                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json");
                client.setConnectTimeout(1000);
                client.setDoOutput(true);

                BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                outputWriter.append(mProduct.toJsonString());
                outputWriter.flush();
                outputWriter.close();

                int readCode = client.getResponseCode();

                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (client != null) {
                    client.disconnect();
                }
            }
            return null;
        }
    }

}
