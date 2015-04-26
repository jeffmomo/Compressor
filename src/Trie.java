/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class Trie
{
	// The current sequence number
	private int _count = 0;

	// Maximum sequence number
	private int _max;

	// The base list
	private PointerLinkedList<Byte> _base;

	// List currently being looked at
	private PointerLinkedList<Byte> _current;

	// Whether to continue looking in same layer
	private boolean _sameLayer = true;

	// Represents the previous matching sequence number.
	private PointerLinkedList<Byte> _prevSeen = null;

	public Trie(int max)
	{
		_base = new PointerLinkedList<Byte>();
		_current = _base;
		_max = max;
	}

	public boolean finalisable()
	{
		return _prevSeen != null;
	}

	// Outputs the final sequence
	public int finalise()
	{
		return (_prevSeen == _base && _sameLayer) ? 0 : _prevSeen.getSequenceNumber();
	}

	// Advances a byte through the trie. Returns either the sequence number or -1 which indicates its still in a matching sequence
	public int advance(byte chr)
	{
		PointerLinkedList<Byte> next;
		int nextSeqNum = -1;

		// First look in the current layer to find a matching char
		if(_sameLayer)
			// Set next to be the node representing the byte being looked for
			next = _current.find(chr);
		// If not looking in the same layer
		else
		{
			// See if a lower layer exists and sets next to it.
			next = _current.getBelow();
			// If it doesnt exist, then create a new layer below.
			if(next == null)
			{
				nextSeqNum = _current.getSequenceNumber();
				_current = _current.addBelow(new PointerLinkedList<Byte>());
			}
			// If the lower layer exists but does not contain the byte, then add the current byte to that layer.
			// Gets the sequence number and outputs it. And also start the next search from the base again.
			else if(next.find(chr) == null)
			{
				if(_count < _max)
					next.add(++_count, chr);
				_sameLayer = true;
				int out = _current.getSequenceNumber();
				_current = _base;
				_prevSeen = null;
				return out;
			}
		}

		// If byte being looked for is found
		if(next != null)
		{
			// Go down the trie
			_sameLayer = false;
			_prevSeen = _current;
			_current = next;
			// Returns -1 meaning it is still matching sequence
			return -1;
		}
		// If not found, then adds to the current layer of the trie, and outputs the appropriate sequence number. Also starts the next search from base again.
		else
		{
			if(_count < _max)
				_current.add(++_count, chr);
			int out = (_current == _base && _sameLayer) ? 0 : nextSeqNum;
			_current = _base;
			_sameLayer = true;
			_prevSeen = null;
			return out;
		}

	}

}
