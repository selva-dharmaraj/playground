package com.me.youtube;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;

public class YoutubeAnalysisDriver {

    public static void main(String[] args) throws Exception {

        if (args.length<3) {
            System.out.println("Argument is wrong");
            System.exit(1);
        }

        // Job configuration
        Job job = new Job(new Configuration());
        job.setJarByClass(YoutubeAnalysisDriver.class);

        // Delete output folder if already persist
        Path outputPath = new Path(args[2]);
        outputPath.getFileSystem(job.getConfiguration()).delete(outputPath);
        job.setJobName(args[0]);

        if("top5categories".equalsIgnoreCase(args[0])){
            executeJob(job,args[1],args[2],TopFiveCategoriesMap.class,TopFiveCategoriesReduce.class,Text.class,IntWritable.class);
        } else if("top10videos".equalsIgnoreCase(args[0])){
            executeJob(job,args[1],args[2],TopTenRatedVideosMap.class,TopTenRatedVideosReduce.class,FloatWritable.class,Text.class);
        } else if("mostViewed".equalsIgnoreCase(args[0])){
            executeJob(job,args[1],args[2],MostViewedVideosMap.class,MostViewedVideosReduce.class,IntWritable.class,Text.class);
        }

    }
    private static void executeJob(Job job, String dataPath, String outPath,Class<? extends Mapper> mapperClass,  Class<? extends Reducer> reducerClass, Class outputKeyClass, Class outputValueClass) throws Exception {
        try {
            FileInputFormat.addInputPath(job, new Path(dataPath));
            FileOutputFormat.setOutputPath(job, new Path(outPath));
            job.setMapperClass(mapperClass);
            job.setReducerClass(reducerClass);
            job.setOutputKeyClass(outputKeyClass);
            job.setOutputValueClass(outputValueClass);
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
