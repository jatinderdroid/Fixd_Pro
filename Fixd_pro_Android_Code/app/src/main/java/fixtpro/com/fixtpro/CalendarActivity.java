package fixtpro.com.fixtpro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.HashMap;

import fixtpro.com.fixtpro.adapters.CalendarAdapter;
import fixtpro.com.fixtpro.beans.CalendarCollection;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class CalendarActivity extends AppCompatActivity {
    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month, back;
    private Context context= this;
    Typeface fontfamily ;

    /*Reschedule Dialog*/
    TextView txtRescheduleDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        getSupportActionBar().hide();
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);

        setRescheduleDialog();

        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM", cal_month) +"\n"+ android.text.format.DateFormat.format("yyyy", cal_month));

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
                getEventJobs();

            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
                getEventJobs();
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*","");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarActivity.this);
            }

        });


    }

    private void setRescheduleDialog() {
        txtRescheduleDialog = (TextView)findViewById(R.id.txtRescheduleDialog);
        txtRescheduleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }

    private void ShowDialog() {
        final Dialog dialog = new Dialog(CalendarActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reschedule);
        dialog.setCanceledOnTouchOutside(false);
        ImageView imgWatch = (ImageView)dialog.findViewById(R.id.imgWatch);
        TextView titletext = (TextView)dialog.findViewById(R.id.titletext);
        TextView txtSelectDate = (TextView)dialog.findViewById(R.id.txtSelectDate);
        TextView txtSelectTime = (TextView)dialog.findViewById(R.id.txtSelectTime);
        final   DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
        ImageView img_Reschedule = (ImageView)dialog.findViewById(R.id.img_Reschedule);
        titletext.setTypeface(fontfamily);
        txtSelectDate.setTypeface(fontfamily);
        txtSelectTime.setTypeface(fontfamily);
        datePicker.setVisibility(View.VISIBLE);
        txtSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
            }
        });
        txtSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });
        img_Reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    dialog.show();
    }

    private void getEventJobs(){
        GetApiResponseAsync responseAsync = new GetApiResponseAsync("POST", eventListener, CalendarActivity.this, "Loading");
        responseAsync.execute(getEventsRequestParams());
    }
    ResponseListener eventListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
                      Log.e("","Response"+Response.toString());
;        }
    };
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
//        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        tv_month.setText(android.text.format.DateFormat.format("MMMM", cal_month) +"\n"+ android.text.format.DateFormat.format("yyyy", cal_month));

    }
    private HashMap<String,String> getEventsRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","jobs");
        hashMap.put("object","calendar");
        hashMap.put("select", "^*");
        hashMap.put("month",cal_month.get(GregorianCalendar.MONTH)+"");
        hashMap.put("year", cal_month.get(GregorianCalendar.YEAR) + "");
        hashMap.put("token", Utilities.getSharedPreferences(this).getString(Preferences.AUTH_TOKEN, null));

        return hashMap;
    }
}
