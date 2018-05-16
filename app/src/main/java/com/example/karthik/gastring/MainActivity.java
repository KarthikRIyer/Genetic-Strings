package com.example.karthik.gastring;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static String target;
    public static float mutationRate=0.01f,fit=0;
    public static int totalPopulation = 150;
    DNA[] population;
    ArrayList<DNA>  matingPool;
    Random random = new Random();
    int sol = 0,fitIndex;
    String maxFit;
    public static ArrayList<ArrayList> generation;
    public static int genIndex = 0;
    ArrayList<String> closestChildren;

    void setup(){
        //target = "to be or not to be";
        //mutationRate = 0.01f;
        generation = new ArrayList<>();
        generation.add(new ArrayList<String>());
        population = new DNA[totalPopulation];
        for(int i = 0;i<population.length;i++){
            population[i]=new DNA();
        }
    }

    void draw(){
        genIndex++;
        for (int i = 0; i < population.length; i++) {
            population[i].fitness();
            if(population[i].fitness>fit){fit = population[i].fitness;fitIndex=i;}
        }
        maxFit = population[fitIndex].getPhrase();
        matingPool = new ArrayList<DNA>();

        for (int i = 0; i < population.length; i++) {

            int n = (int)(population[i].fitness * 100);
            for (int j = 0; j < n; j++) {
                matingPool.add(population[i]);
            }

        }

        for (int i = 0; i < population.length; i++) {
            int a = random.nextInt(matingPool.size());
            int b = random.nextInt(matingPool.size());
            DNA partnerA = matingPool.get(a);
            DNA partnerB = matingPool.get(b);

            DNA child = partnerA.crossover(partnerB);

            child.mutate(mutationRate);

            population[i] = child;
            //generation.
            if(population[i].getPhrase().equals(target)){sol=1;}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                int iter = 0;
                closestChildren = new ArrayList<>();
                ((TextView) findViewById(R.id.textView2)).setText("");
                ((TextView) findViewById(R.id.textView4)).setText("");
                EditText edt = (EditText)findViewById(R.id.editText);
                target = edt.getText().toString();
                if(!target.isEmpty() && !target.equals(""))
                {   long startTime = System.nanoTime();
                    setup();
                    while (sol != 1) {
                        ++iter;
                        ArrayList<String> pop = new ArrayList<>();
                        generation.add(pop);
                        draw();
                        //((TextView)findViewById(R.id.closestChild)).setText(maxFit);
                        closestChildren.add(iter+". "+maxFit);
                    }
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000000;

                    ((TextView) findViewById(R.id.textView2)).setText(iter+" ");
                    ((TextView) findViewById(R.id.textView4)).setText(duration + " s");
                    sol = 0;
                    final ListView listView = (ListView)findViewById(R.id.listView);
                    final StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,closestChildren);
                    listView.setAdapter(adapter);
                }
            }
        });

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView)findViewById(R.id.mutaRate)).setText(((float)i/100f)+" ");
                mutationRate = (float)i/100f;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}


class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String,Integer> mIdMap = new HashMap<>();
    public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        for(int i =0;i<objects.size();i++){
            mIdMap.put(objects.get(i),i);
        }
    }
    @Override
    public long getItemId(int position){
        String item = getItem(position);
        return mIdMap.get(item);
    }
    @Override
    public boolean hasStableIds(){
        return true;
    }

}
