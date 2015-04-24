import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Jeff on 8/04/2015.
 */
public class Decompressor
{
	// Two arrays to store the sequence number and the byte
	private int[] _sequenceArray;
	private byte[] _charArray;

	// Current position in the n-gram array
	private int _position;

	private OutputStream _stream;

	private int _max;


	// Constructs a decompressor from an output stream and the max sequence number (i.e. dictionary size) possible
	public Decompressor(int maxSequence, OutputStream stream)
	{
		_max = maxSequence;
		_sequenceArray = new int[maxSequence];
		_charArray = new byte[maxSequence];
		_stream = stream;
	}

	// Processes the seqNum and byte tuple. Outputs to the stream
	public void process(int seqNum, byte chr) throws IOException
	{
		// Adds the tuple to the array of previously seen n-grams
		if(_position < _max)
		{
			_sequenceArray[++_position] = seqNum;
			_charArray[_position] = chr;
		}

		// Current character being looked at
		byte currentChar = chr;
		// Doubly linked List storing the full list of bytes represented by this tuple
		LightList list = new LightList(currentChar);

		// While the bytearray hasent been fully constructed, keep constructing it by visiting the referenced sequences
		while(seqNum != 0)
		{
			currentChar = _charArray[seqNum];
			seqNum = _sequenceArray[seqNum];

			// Adds the byte in the visited sequence to byte list.
			list = list.add(currentChar);
		}
		// Writes whats in the list out to stream, in the correct order.
		while(list != null)
		{
			_stream.write(list.value);
			list = list.prev;
		}
	}

}
