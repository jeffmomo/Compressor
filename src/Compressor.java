import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
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
		_packer = new BitPacker(_stream);//DummyPack();
	}

	public void process(byte in) throws IOException
	{
		_prevByte = in;



		int seq = _trie.advance(in);
		if (seq != -1)
		{
			//System.out.println(Integer.toString(seq, Character.MAX_RADIX) + "``" + (char) in);
			_packer.packBytes(seq, in);
			/*String z = "";
			z += (char) in;
			System.out.print(">>>>");
			BytesUtil.printBytes(z.getBytes());*/
		}

	}

	public void finalise()
	{
		if(_trie.finalisable())
			_packer.packBytes(_trie.finalise(), _prevByte);



		_packer.finalisePackedBits();

	}

}
