import java.io.*;

public class Main {

    public static void main(String[] args)
    {
	// write your code here

	    if(args[0].equals("c"))
	    {

		    try
		    {
			    FileInputStream fis = new FileInputStream("in");
			    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("out.comp"));
			    Compressor c = new Compressor(bos);

			    int z = fis.read();
			    while (z != -1)
			    {
				    System.err.println(z);
				    c.process((byte) z);
				    z = fis.read();
			    }
			    bos.write(c.finalise());
			    bos.flush();
			    bos.close();
		    } catch (Exception e)
		    {
			    e.printStackTrace();
		    }
	    }
	    else
	    {

		    IUnpacker bu = new BitUnpacker();// DummyUnpack();
		    try
		    {
			    //BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));



			    bu.UnpackBits(new FileInputStream("out.comp"));

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
    }
}
