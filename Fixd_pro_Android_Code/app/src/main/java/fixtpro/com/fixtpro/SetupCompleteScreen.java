package fixtpro.com.fixtpro;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetupCompleteScreen extends AppCompatActivity {
    private TextView text_setupcomplete, text_belowthumb, text_inthe,text_voila, text_watch_video;
    Typeface fontfamily;
    LinearLayout container ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_complete_screen);

        // set widget ids,.....
        setWidgetIDS();
        // set typeface....
        setTypeFace();
        // set ClickListner....
        setCLickListner();

    }

    private void setCLickListner() {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login_Register_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setTypeFace() {
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        text_setupcomplete.setTypeface(fontfamily);
        text_belowthumb.setTypeface(fontfamily);
        text_inthe.setTypeface(fontfamily);
        text_voila.setTypeface(fontfamily);
        text_watch_video.setTypeface(fontfamily);
    }

    private void setWidgetIDS() {
        text_setupcomplete = (TextView)findViewById(R.id.text_setupcomplete);
        text_belowthumb = (TextView)findViewById(R.id.text_belowthumb);
        text_inthe = (TextView)findViewById(R.id.text_inthe);
        text_voila = (TextView)findViewById(R.id.text_voila);
        text_watch_video = (TextView)findViewById(R.id.text_watch_video);
        container = (LinearLayout)findViewById(R.id.container);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup_complete_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
