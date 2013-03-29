/**
 * 
 */
package de.geymer.je.Gameplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import android.content.SharedPreferences;
import android.graphics.Point;

/**
 * @author babkamen
 * 
 */
public class Suggestions {
    SharedPreferences dict,addDict;
    long start;

    private  List paths = new ArrayList();
    
    Set<String> parts = new HashSet<String>();
    /**
     * @param dict
     * @param addDict
     */
    public Suggestions(SharedPreferences dict, SharedPreferences addDict) {
        super();
        this.dict = dict;
        this.addDict = addDict;
    }
    private  void findAllpaths(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != ' ')
                    continue;
                if (j < matrix[0].length - 1 && matrix[i][j + 1] != ' '
                        || j > 0 && matrix[i][j - 1] != ' '
                        || i < matrix.length - 1 && matrix[i + 1][j] != ' '
                        || i > 0 && matrix[i - 1][j] != ' ') {
                    findAllpaths(matrix, i, j);
                }

            }
    }
public void initStart() {
    start=System.currentTimeMillis();
}
    public void divide(String string) {
        long st = System.currentTimeMillis();
        for (int i = 0; i < Math.pow(2, string.length()); i++) {
            String s = "";
            if (!check(i))continue;
            for (int j = 0; j < string.length(); j++) {

                if ((i & 1 << j) > 0) {
                    s = s.concat(String.valueOf(string.charAt(j)));
                }
            }

            if (s.length() > 0) {
                parts.add(s);
                if (s.length() > 1) {
                    parts.add(new StringBuilder(s).reverse().toString());
                }
            }
        }
    }

    private boolean check(int i) {
        String s = Integer.toBinaryString(i);
        int start = s.indexOf("1");
        if (start == -1)
            return true;
        int next = s.indexOf("1", start);
        while (next != -1) {
            if (new String(s.substring(start, next)).contains("0"))
                return false;
            start = next;
            next = s.indexOf("1", start + 1);
        }

        return true;
    }

    private void findAllpaths(char[][] arr, int i, int j) {
        if (j > 0 && arr[i][j - 1] != ' ') {
            findAllpaths(arr, i, j - 1, "", new ArrayList());
        }
        if (j < arr[0].length - 1 && arr[i][j + 1] != ' ') {
            findAllpaths(arr, i, j + 1, "", new ArrayList());
        }

        if (i < arr.length - 1 && arr[i + 1][j] != ' ') {
            findAllpaths(arr, i + 1, j, "", new ArrayList());
        }

        if (i > 0 && arr[i - 1][j] != ' ') {
            findAllpaths(arr, i - 1, j, "", new ArrayList());
        }

    }

    private void findAllpaths(char[][] arr, int i, int j, String path,
            List usedPosition) {
        path += String.valueOf(arr[i][j]);
        paths.add(path);
        usedPosition.add(new Point(i, j));
        if (j > 0 && arr[i][j - 1] != ' '
                && !usedPosition.contains(new Point(i, j - 1))) {
            findAllpaths(arr, i, j - 1, path, new ArrayList(usedPosition));
        }
        if (j < arr[0].length - 1 && arr[i][j + 1] != ' '
                && !usedPosition.contains(new Point(i, j + 1))) {
            findAllpaths(arr, i, j + 1, path, new ArrayList(usedPosition));
        }

        if (i < arr.length - 1 && arr[i + 1][j] != ' '
                && !usedPosition.contains(new Point(i + 1, j))) {
            findAllpaths(arr, i + 1, j, path, new ArrayList(usedPosition));
        }

        if (i > 0 && arr[i - 1][j] != ' '
                && !usedPosition.contains(new Point(i - 1, j))) {
            findAllpaths(arr, i - 1, j, path, new ArrayList(usedPosition));
        }

    }
    
    private  void printTime(long start) {
        double time;
        time = (double) (System.currentTimeMillis() - start);
        System.out.println("Time " + time);
    }


    private  List<String> findSuggestions(List<String> patterns) {
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < patterns.size(); i++) {
            String pat=patterns.get(i);
            String part;
           // System.out.println(res);
            //System.out.println("Pattern "+pat);
        if(pat.length()==1){
            if ((part = addDict.getString("." + patterns.get(i),""))!="") {
                String s[] = part.split(" ");
                for (String p : s)
                    if (p != null)
                        res.add(p + pat);

            }
            if ((part = addDict.getString(patterns.get(i) + ".","")) != "") {
                              String s[] = part.split(" ");
                for (String p : s)
                    if (p != null)
                        res.add(part + p);
            }
        }else{
            if((part=addDict.getString("."+   pat.substring(1)+".",""))!=""){
                int ind=part.indexOf(pat.charAt(0)+"-");
               if(ind>-1)res.add(pat + part.charAt(ind+2));
            }
            if((part=addDict.getString("."+pat.substring(0,pat.length()-1)+".",""))!=""){
                int ind =part.indexOf("-"+pat.charAt(pat.length()-1));
                if(ind>-1)res.add(part.charAt(ind-1)+pat);            
            }     
        }    
        }

        return res;
    }
    public List<String> getSuggestion(char matrix[][],List<String> usedWords){
        findAllpaths(matrix);
        for(int i=0;i<paths.size();i++)
            divide((String)paths.get(i));
        return findSuggestions(new ArrayList(parts));
    }
}
