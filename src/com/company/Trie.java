package com.company;

/**
 * Created by Jeff on 7/04/2015.
 */
public class Trie
{
	public final static int MAX_COUNT = 65535;
	public final static PointerLinkedList<Character> LAMBDA = new PointerLinkedList<Character>();

	private int _count = 0;
	private PointerLinkedList<Character> _base;
	//private PointerLinkedList<Character> _next;
	private PointerLinkedList<Character> _current;
	private boolean _sameLayer = true;

	public Trie()
	{
		_base = new PointerLinkedList<Character>();
		//_next = _base;
		_current = _base;
	}

	public void addUnseenAt(String at, char toAdd)
	{
		// Increments the sequence number
		_count++;

		PointerLinkedList<Character> start = _base;
		int len = at.length();
		for(int i = 0; i < len; i++)
		{
			start = (PointerLinkedList<Character>) start.find(at.charAt(i));
		}
		start.add(_count, toAdd);
	}

	public int advance(char chr)
	{
		PointerLinkedList next;
		if(_sameLayer)
			next = _current.find(chr);
		else
		{
			next = _current.getBelow();
			if(next == null)
			{
				_current = _current.addBelow(new PointerLinkedList<Character>());
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
			int out = (_current == _base && _sameLayer) ? 0 : _current.getSequenceNumber() - 1;
			_current = _base;
			_sameLayer = true;
			return out;
		}

	}

	//public void findClosestMatch(String )
}
