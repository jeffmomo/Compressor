import java.io.*;

public class DummyUnpack implements IUnpacker
{
	public Decompressor _decomp;
	public OutputStream _outStream;
	private InputStream _inStream;

	public DummyUnpack(InputStream iss)
	{

		try
		{
			_inStream = iss;
			_outStream = new BufferedOutputStream(new FileOutputStream("out.decomp"));
			_decomp = new Decompressor(4000000, _outStream);
		}catch(Exception e){}
	}

	public void UnpackBits()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(_inStream));
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

			_outStream.close();

		}catch(Exception e){}
	}


}