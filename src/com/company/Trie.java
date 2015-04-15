package com.company;

/**
 * Created by Jeff on 7/04/2015.
 */
public class Trie
{
	public final static int MAX_COUNT = 65535;
	public final static PointerLinkedList<Character> LAMBDA = new PointerLinkedList<Character>();

	private int _count = 0;
	private PointerLinkedList<Byte> _base;
	//private PointerLinkedList<Byte> _next;
	private PointerLinkedList<Byte> _current;
	private boolean _sameLayer = true;

	public Trie()
	{
		_base = new PointerLinkedList<Byte>();
		//_next = _base;
		_current = _base;
	}

	public int advance(byte chr)
	{
		PointerLinkedList next;
		int nextSeqNum = -1;

		if(_sameLayer)
			next = _current.find(chr);
		else
		{
			next = _current.getBelow();
			if(next == null)
			{
				nextSeqNum = _current.getSequenceNumber();
				_current = _current.addBelow(new PointerLinkedList<Byte>());
			}
			else if(next.find(chr) == null)
			{
				next.add(++_count, chr);
				_sameLayer = true;
				int out = _current.getSequenceNumber();
				_current = _base;
				return out;

			}
		}

		if(next != null)
		{
			_sameLayer = false;
			_current = next;
			return -1;
		}
		else
		{
			_current.add(++_count, chr);
			int out = (_current == _base && _sameLayer) ? 0 : nextSeqNum;
			_current = _base;
			_sameLayer = true;
			return out;
		}

	}

	//public void findClosestMatch(String )
}
