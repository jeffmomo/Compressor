import java.io.*;

public class Main {

    public static void main(String[] args)
    {
	// write your code here

	    if(false)
	    {

		    Trie bob = new Trie();
		    try
		    {
			    File f = new File("out.comp");
			    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
			    Compressor c = new Compressor(bos);

			    int z = System.in.read();
			    while (z != -1)
			    {

				    System.err.println(z);
				    int seq = bob.advance((byte) z);
				    c.process((byte) z);
				    if (seq != -1)
				    {

					   // bos.write(seq);

					    //bos.write(z);
					    //System.out.println(Integer.toString(seq, Character.MAX_RADIX) + "``" + (char) z);
				    }
				    z = System.in.read();

			    }
			    bos.write(c.finalise());
			    if(bob.finalisable())
				    System.out.println(Integer.toString(bob.finalise(), Character.MAX_RADIX) + "``" + (char) z);
			    bos.flush();
			    bos.close();
		    } catch (Exception e)
		    {
			    e.printStackTrace();
		    }
	    }
	    else
	    {

		    IUnpacker bu = new DummyUnpack();
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
