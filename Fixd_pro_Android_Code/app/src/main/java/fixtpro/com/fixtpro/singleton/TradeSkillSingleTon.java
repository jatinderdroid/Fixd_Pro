package fixtpro.com.fixtpro.singleton;

import java.util.ArrayList;

import fixtpro.com.fixtpro.beans.SkillTrade;

/**
 * Created by sony on 09-02-2016.
 */
public class TradeSkillSingleTon  {
    private static TradeSkillSingleTon singleton = new TradeSkillSingleTon( );
    ArrayList<SkillTrade> arrayList = new ArrayList<SkillTrade>();
    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private TradeSkillSingleTon(){ }

    /* Static 'instance' method */
    public static TradeSkillSingleTon getInstance( ) {
        return singleton;
    }
    /* Other methods protected by singleton-ness */
    public  ArrayList<SkillTrade> getList( ) {
        return arrayList;
    }
    public  ArrayList<SkillTrade> getListChecked( ) {
        ArrayList<SkillTrade> arrayListChecked  = new ArrayList<SkillTrade>();
        arrayListChecked.clear();
        for (int i = 0 ; i < arrayList.size() ; i++){
            if (arrayList.get(i).isChecked()){
                arrayListChecked.add(arrayList.get(i));
            }
        }
        return arrayListChecked;
    }
    public  void setList(ArrayList<SkillTrade> arrayList ) {
        this.arrayList = arrayList;
    }
}
