package fixtpro.com.fixtpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import fixtpro.com.fixtpro.adapters.TradeSkillAdapter;
import fixtpro.com.fixtpro.beans.SkillTrade;
import fixtpro.com.fixtpro.singleton.TradeSkillSingleTon;
import fixtpro.com.fixtpro.utilites.GetApiResponseAsync;
import fixtpro.com.fixtpro.utilites.Preferences;
import fixtpro.com.fixtpro.utilites.Utilities;

public class SelectTradeLicence extends AppCompatActivity {
    private Context _context = this;
    TextView txtBack,txtDone;
    ArrayList<SkillTrade> skillTrades ;
    SkillTrade selectedTradeSkill  = null ;
    TradeSkillAdapter tradeSkillAdapter = null;
    ListView lstTradeSkill ;
    SharedPreferences _prefs = null ;
    Typeface fontfamily;
    private static final int TRADE_LICENCE_NUMBER = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trade_licence);
        setWidgets();
        setListeners();
        setTypeface();

        skillTrades = TradeSkillSingleTon.getInstance().getList();
        _prefs = Utilities.getSharedPreferences(_context);
        if(skillTrades.size() == 0 ){
            GetApiResponseAsync getApiResponseAsync = new GetApiResponseAsync("POST",getTradeSkillListener,SelectTradeLicence.this,"Getting.");
            getApiResponseAsync.execute(getTradeSkillRequestParams());
        }else{
            setListAdapter();
        }
        lstTradeSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position -= lstTradeSkill.getHeaderViewsCount();
                selectedTradeSkill = skillTrades.get(position);
                if (selectedTradeSkill.isChecked()){
                    skillTrades.get(position).setIsChecked(false);
                }else{
                    skillTrades.get(position).setIsChecked(true);
                }
                tradeSkillAdapter.notifyDataSetChanged();
//                _prefs.edit().putInt(Preferences.SELECTED_TRADE_ID,selectedTradeSkill.getId()).commit();
            }
        });
    }
    private void setWidgets(){
        lstTradeSkill = (ListView)findViewById(R.id.lstTradeSkill);
        txtBack = (TextView)findViewById(R.id.txtBack);
        txtDone = (TextView)findViewById(R.id.txtDone);
    }
    private  void setTypeface(){
        fontfamily = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        txtBack.setTypeface(fontfamily);
        txtDone.setTypeface(fontfamily);
    }
    private void setListeners(){
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(TRADE_LICENCE_NUMBER,intent);
                finish();
            }
        });

    }
    ResponseListener getTradeSkillListener = new ResponseListener() {
        @Override
        public void handleResponse(JSONObject Response) {
            Log.e("","Response"+Response);
        }
    };
    private HashMap<String,String> getTradeSkillRequestParams(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("api","read");
        hashMap.put("object","services");
        hashMap.put("select","^*");
        hashMap.put("per_page","999");
        hashMap.put("page","1");
        return hashMap;
    }
    private void setAddHeader(){
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.trade_skill_item_header, lstTradeSkill, false);
        TextView txtTitle = (TextView)header.findViewById(R.id.txtTitle);
//        txtTitle.setTypeface(fontfamily);
        lstTradeSkill.addHeaderView(header, null, false);
    }
    private void setListAdapter(){
        setAddHeader();
        tradeSkillAdapter = new TradeSkillAdapter(this,skillTrades,getResources());
        lstTradeSkill.setAdapter(tradeSkillAdapter);

    }
}
