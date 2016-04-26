package com.me.youtube;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class MostViewedVideosReduce extends
        Reducer<IntWritable,Text, IntWritable, Text> {
    private static final Logger log = Logger.getLogger(MostViewedVideosReduce.class);
    private HashMap<Integer, ArrayList<String>> tokenMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        tokenMap = new HashMap<Integer, ArrayList<String>>();
    }

    @Override
    public void reduce(IntWritable key, Iterable<Text> values,
                       Context context) throws IOException, InterruptedException {
        for (Text x : values) {
            // Add the rating and related video ids to map.
            addVideoRelatedToView(key.get(),x.toString());
        }
    }

    private void addVideoRelatedToView(int v, String videoId) {
        ArrayList videos = tokenMap.get(v);
        if(videos == null){
            videos = new ArrayList();
            tokenMap.put(v,videos);
        }
        tokenMap.get(v).add(new String(videoId));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        IntWritable numberOfViews = new IntWritable();
        Text videos = new Text();

        // Sort the HashMap by value
        Map<Integer, ArrayList<String>> sortedMap =
                new TreeMap<Integer, ArrayList<String>>(new Comparator<Integer>() {
                    public int compare(Integer noViewOne, Integer noViewTwo) {
                        return (noViewTwo.compareTo(noViewOne));
                    }
                });
        sortedMap.putAll(tokenMap);
        //Map<Integer, ArrayList> sortedMap = new TreeMap<Integer, ArrayList>(tokenMap);

        // Add top top ten videos
        int i=0;
        for (Map.Entry<Integer, ArrayList<String>> entry : sortedMap.entrySet()) {
            //list only top five since it is lot of videos
            if(i==5) break;
            numberOfViews.set(entry.getKey());
            videos.set(entry.getValue().toString());
            context.write(numberOfViews,videos);
            i++;
        }
    }

}