import java.io.*;

public class Main {

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
	// write your code here

	    if(args[0].equals("c"))
	    {

		    try
		    {
			    BufferedInputStream fis = new BufferedInputStream(new FileInputStream("in"));
			    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("out.comp"));
			    Compressor c = new Compressor(bos);

			    int z = fis.read();
			    while (z != -1)
			    {
				    //System.err.println(z);
				    c.process((byte) z);
				    z = fis.read();
			    }
			    c.finalise();
			    bos.flush();
			    bos.close();
		    } catch (Exception e)
		    {
			    e.printStackTrace();
		    }
	    }
	    else
	    {

		    
		    try
		    {
                        IUnpacker bu = new BitUnpacker(new BufferedInputStream(new FileInputStream("out.comp")));// DummyUnpack();
			    //BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));



                        bu.UnpackBits();

//			    String temp;
//			    while ((temp = bis.readLine()) != null)
//			    {
//				    if (temp.isEmpty())
//					    continue;
//				    String[] splitted = temp.split("``");
//				    int seq = Integer.parseInt(splitted[0], Character.MAX_RADIX);
//				    byte ch = splitted.length == 1 ? (byte)('\n') : ((byte) (splitted[1].charAt(0)));
//				    decomp.process(seq, ch);
//			    }


		    } catch (Exception e)
		    {
			    e.printStackTrace();
		    }
	    }
            
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("\ntotal thread 1 runtime: "+ totalTime);
    }
}
