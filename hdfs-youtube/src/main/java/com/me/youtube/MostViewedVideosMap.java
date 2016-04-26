package com.me.youtube;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MostViewedVideosMap extends Mapper<LongWritable, Text, IntWritable, Text> {
    private static final IntWritable views = new IntWritable();
    private static final Text videoId = new Text();
    public void map(LongWritable key, Text value, Context context) throws IOException,
            InterruptedException {
        String line = value.toString();
        String[] tokens = line.split("\\t");
        try {
            views.set(Integer.valueOf(tokens[5].trim())); //6th token is number of views
            videoId.set(tokens[0].trim()); //1st token is video id
            context.write(views,videoId);
        }catch (Exception e){
            System.out.println("Array Issue");
        }
    }
}