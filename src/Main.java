import java.io.*;

public class Main {

    public static void main(String[] args)
    {
	// write your code here

	    if(true)
	    {

		    Trie bob = new Trie();
		    try
		    {
			    File f = new File("out.txt");
			    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));

			    int z = 0;//System.in.read();
			    //while (z != -1)
			    for(int i = 0; i < 6; i++)
			    {
				    z = System.in.read();
				    System.err.println(z);
				    int seq = bob.advance((byte) z);
				    if (seq != -1)
				    {

					    bos.write(seq);

					    bos.write(z);
					    System.out.println(Integer.toString(seq, Character.MAX_RADIX) + "``" + (char) z);
				    }

			    }
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

		    Decompressor decomp = new Decompressor(65535, System.out);
		    try
		    {
			    BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

			    String temp;
			    while ((temp = bis.readLine()) != null)
			    {
				    if (temp.isEmpty())
					    continue;
				    String[] splitted = temp.split("``");
				    int seq = Integer.parseInt(splitted[0], Character.MAX_RADIX);
				    byte ch = splitted.length == 1 ? (byte)('\n') : ((byte) (splitted[1].charAt(0)));
				    decomp.process(seq, ch);
			    }


		    } catch (Exception e)
		    {
			    e.printStackTrace();
		    }
	    }
    }
}
