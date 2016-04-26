package com.me.youtube;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TopTenRatedVideosMap extends Mapper<LongWritable, Text, FloatWritable, Text> {
    private static final FloatWritable rating = new FloatWritable();
    private static final Text text = new Text();
    public void map(LongWritable key, Text value, Context context) throws IOException,
            InterruptedException {
        String line = value.toString();
        String[] tokens = line.split("\\t");
        try {
            rating.set(Float.valueOf(tokens[6].trim())); //7th token is ratting
            text.set(tokens[0].trim()); //1st token is video id
            context.write(rating,text);
        }catch (Exception e){
            System.out.println("Array Issue");
        }
    }
}