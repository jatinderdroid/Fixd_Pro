package fixtpro.com.fixtpro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView cancel, email, call, feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
    }

    public void setWidgets(){
        cancel = (ImageView)findViewById(R.id.cancel);
        email = (ImageView)findViewById(R.id.email);
        call = (ImageView)findViewById(R.id.call);
        feedback = (ImageView)findViewById(R.id.feedback);
    }

    public void setListeners(){
        cancel.setOnClickListener(this);
        email.setOnClickListener(this);
        call.setOnClickListener(this);
        feedback.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "support@fixdrepair.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fixd-Pro App Support(Ver: 1.0)");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.call:
                String number = "tel:" + "80-01111111";
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
                break;
            case R.id.feedback:
                Intent feedbackemailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "feedback@fixdrepair.com", null));
                feedbackemailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fixd-Pro App Feedback(Ver: 1.0)");
                startActivity(Intent.createChooser(feedbackemailIntent, "Send email..."));
                break;
        }
    }
}
