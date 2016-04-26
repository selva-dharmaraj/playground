package com.me.util;

import java.util.*;

/**
 * Created by selva on 4/22/16.
 */
public class TestClass {
    public static void main(String[] args) {
        /*Column1: Video id of 11 characters.
                Column2: uploader of the video of string data type.
        Column3: Interval between day of establishment of Youtube and the date of uploading of the video of
        integer data type.
                Column4: Category of the video of String data type.
        Column5: Length of the video of integer data type.
        Column6: Number of views for the video of integer data type.
        Column7: Rating on the video of float data type.
                Column8: Number of ratings given on the video.
                Column9: Number of comments on the videos in integer data type.
        Column10: Related video ids with the uploaded video.*/

        String toSplit = "1\t2\t3\t4\t5\t6\t7\t8\t9\t10";
        System.out.println(toSplit.split("\t")[5]);

        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("Naveen", 2);
        hm.put("Santosh", 3);
        hm.put("Ravi", 4);
        hm.put("Pramod", 1);
        Set<Map.Entry<String, Integer>> set = hm.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey()+"\t"+entry.getValue());

        }
    }


}
