import java.io.*;

/**
 * Created by Jeff on 23/04/2015.
 */
public class DummyPack implements IPacker
{
	private OutputStream _output;

	public DummyPack(OutputStream os)
	{
		_output = os;
	}

	public void packBytes(int parseNumber, byte character)
	{
		try
		{
			_output.write((Integer.toString(parseNumber) + "``" + (char) character + "\n").getBytes());
		}catch(Exception e){}
		//out += (Integer.toString(parseNumber) + "``" + (char) character + "\n");
	}

	public void finalisePackedBits()
	{

	}

	//public byte[] returnPackedBits()
	//{
	//	return out.getBytes();
	//}
}


