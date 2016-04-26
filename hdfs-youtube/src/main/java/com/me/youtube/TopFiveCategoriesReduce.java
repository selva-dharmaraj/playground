package com.me.youtube;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class TopFiveCategoriesReduce extends
        Reducer<Text, IntWritable, Text, IntWritable> {

    private Map<String,Integer> tokenMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        tokenMap = new HashMap<String, Integer>();
    }

    @Override
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context) throws IOException,
            InterruptedException {
        int sum = 0;
        for (IntWritable x : values) {
            sum += x.get();
        }
        tokenMap.put(key.toString(),new Integer(sum));
        //context.write(key, new IntWritable(sum));

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        IntWritable writableCount = new IntWritable();
        Text text = new Text();

        // Sort the HashMap by value
        Set<Map.Entry<String, Integer>> set = tokenMap.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Add top five categories from sorted map
        int i=0;
        for (Map.Entry<String, Integer> entry : list) {
            i++;
            //System.out.println(entry.getKey()+"\t"+entry.getValue());

            text.set(entry.getKey());
            writableCount.set(entry.getValue());
            context.write(text,writableCount);

            if(i==5) break;

        }
    }


}