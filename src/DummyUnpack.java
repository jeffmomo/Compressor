import java.io.*;

public class DummyUnpack implements IUnpacker
{
	public Decompressor _decomp;

	public DummyUnpack()
	{
		try
		{
			_decomp = new Decompressor(4000000, new FileOutputStream("out.decomp"));
		}catch(Exception e){}
	}

	public void UnpackBits(FileInputStream f)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(f));
		String temp;
		try
		{
			while ((temp = br.readLine()) != null)
			{
				if (temp.isEmpty())
					continue;
				String[] splitted = temp.split("``");
				int seq = Integer.parseInt(splitted[0], Character.MAX_RADIX);
				byte ch = splitted.length == 1 ? (byte)('\n') : ((byte) (splitted[1].charAt(0)));
				_decomp.process(seq, ch);
			}
		}catch(Exception e){}
	}


}