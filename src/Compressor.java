import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Jeff on 7/04/2015.
 */
public class Compressor
{

	private OutputStream _stream;
	private Trie _trie;
	private IPacker _packer;
	private byte _prevByte;

	public Compressor(OutputStream os)
	{
		_stream = os;
		_trie = new Trie(4000000);
		_packer = new Bitpacker();//DummyPack();
	}

	public void process(byte in) throws IOException
	{
		_prevByte = in;



		int seq = _trie.advance(in);
		if (seq != -1)
		{
			//System.out.println(Integer.toString(seq, Character.MAX_RADIX) + "``" + (char) in);
			_packer.packBytes(seq, in);
			String z = "";
			z += (char) in;
			System.out.print(">>>>");
			BytesUtil.printBytes(z.getBytes());
		}

	}

	public byte[] finalise()
	{
		if(_trie.finalisable())
			_packer.packBytes(_trie.finalise(), _prevByte);



		return _packer.returnPackedBits();

	}

}
