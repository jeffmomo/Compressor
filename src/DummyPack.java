import java.io.*;

/**
 * Created by Jeff on 23/04/2015.
 */
public class DummyPack implements IPacker
{
	private String out = "";

	public void packBytes(int parseNumber, byte character)
	{
		out += (Integer.toString(parseNumber) + "``" + (char) character + "\n");
	}

	public byte[] returnPackedBits()
	{
		return out.getBytes();
	}
}


