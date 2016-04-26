package com.me.youtube;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TopFiveCategoriesMap extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException,
            InterruptedException {
        String line = value.toString();
        String[] tokens = line.split("\\t");
        try {
            value.set(tokens[3].trim()); //4th token is a category
            context.write(value, new IntWritable(1));
        }catch (Exception e){
            System.out.println("Array Issue ");
        }
    }
}