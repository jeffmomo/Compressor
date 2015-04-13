package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args)
    {
	// write your code here

//	    Trie bob = new Trie();
//	    try
//	    {
//		    File f = new File("out.zip");
//		    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
//
//		    int z = System.in.read();
//		    while (z != -1)
//		    {
//			    //System.err.println(z);
//			    int seq = bob.advance((char) z);
//			    if(seq != -1)
//			    {
//
//				    bos.write(seq);
//
//				    bos.write(z);
//				    System.out.println(Integer.toString(seq, Character.MAX_RADIX) + "," + (char) z);
//			    }
//			    z = System.in.read();
//		    }
//			bos.flush();
//		    bos.close();
//	    }
//	    catch (Exception e){
//		    e.printStackTrace();
//	    }

	    Decompressor decomp = new Decompressor(65535);
	    try
	    {
		    BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

		    String temp;
		    while((temp = bis.readLine()) != null)
		    {
			    if(temp.isEmpty())
				    continue;
			    String[] splitted = temp.split(",");
			    int seq = Integer.parseInt(splitted[0], Character.MAX_RADIX);
			    char ch = splitted.length == 1 ? '\n' : splitted[1].charAt(0);
			    decomp.process(seq, ch);
		    }

		    System.out.println(decomp.getOutput());
		    System.out.flush();
	    }
	    catch(Exception e){e.printStackTrace();}
    }
}