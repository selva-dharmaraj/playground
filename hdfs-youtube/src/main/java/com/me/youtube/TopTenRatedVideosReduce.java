package com.me.youtube;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class TopTenRatedVideosReduce extends
        Reducer<FloatWritable,Text, FloatWritable, Text> {
    private static final Logger log = Logger.getLogger(TopTenRatedVideosReduce.class);
    private HashMap<Float, ArrayList<String>> tokenMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        tokenMap = new HashMap<Float, ArrayList<String>>();
    }

    @Override
    public void reduce(FloatWritable key, Iterable<Text> values,
                       Context context) throws IOException, InterruptedException {
        for (Text x : values) {
            // Add the rating and related video ids to map.
            int ratingVideoSize = addVideoToRelatedRating(key.get(),x.toString());
            // Break the flow if the this ratting has more than 10 video already.
            if(ratingVideoSize==10) break;
        }
    }

    private int addVideoToRelatedRating(float v, String videoId) {
        int size = 10;
        ArrayList videos = tokenMap.get(v);
        if(videos == null){
            videos = new ArrayList();
            tokenMap.put(v,videos);
        }
        size = tokenMap.get(v).size();
        if(size == 10){
            return size;
        }
        tokenMap.get(v).add(new String(videoId));

        return size;
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        FloatWritable writableRating = new FloatWritable();
        Text text = new Text();

        // Sort the HashMap by value
        Map<Float, ArrayList<String>> sortedMap =
                new TreeMap<Float, ArrayList<String>>(new Comparator<Float>() {
                    public int compare(Float ratingOne, Float ratingTwo) {
                        return (ratingTwo.compareTo(ratingOne));
                    }
                });
        sortedMap.putAll(tokenMap);
        //Map<Float, ArrayList> sortedMap = new TreeMap<Float, ArrayList>(tokenMap);

        // Add top top ten videos
        for (Map.Entry<Float, ArrayList<String>> entry : sortedMap.entrySet()) {
            writableRating.set(entry.getKey());
            text.set(entry.getValue().toString());
            context.write(writableRating,text);
            //Break it since we need only top ten videos with rating of 5 star (* * * * *)
            break;
        }
    }


}