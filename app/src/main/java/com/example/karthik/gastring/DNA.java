package com.example.karthik.gastring;

import java.util.Random;

import static com.example.karthik.gastring.MainActivity.generation;
import static com.example.karthik.gastring.MainActivity.target;


/**
 * Created by karthik on 9/3/18.
 */

public class DNA {
    char[] genes = new char[target.length()];
    Random random  = new Random();
    float fitness;


    DNA(){
        for(int i = 0;i<genes.length;i++){
            genes[i]= (char)(random.nextInt(128-32+1)+32);
        }
        generation.get(0).add(getPhrase());
    }

    void fitness(){
        int score = 0;
        for(int i =0;i<genes.length;i++){
            if(genes[i]==target.charAt(i)){
                score++;
            }
        }
        fitness=(float)score/target.length();
    }

    DNA crossover(DNA partner){
        DNA child = new DNA();
        int midpoint = random.nextInt(genes.length);
        for (int i = 0; i < genes.length; i++) {
            if (i > midpoint) child.genes[i] = genes[i];
            else              child.genes[i] = partner.genes[i];
        }
        return child;
    }
    void mutate(float mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if (random.nextFloat() < mutationRate) {
                genes[i] = (char)(random.nextInt(128-32+1)+32);
            }
        }
    }
    String getPhrase() {
        return new String(genes);
    }

}
