package htl.at.shoppinglist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pautzi on 19.05.16.
 */
public class InputActivity extends Activity {
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

                new Product(text_title.getText().toString(),text_pieces.getText().toString());

                InputActivity.this.finish();
            }
        });
    }
}
