package com.company;

import java.util.LinkedList;

/**
 * Created by Jeff on 7/04/2015.
 */
public class LinkedListCharFinder implements ICharFinder
{
	private PointerLinkedList<Character> _baseList;

	public LinkedListCharFinder()
	{
		_baseList = new PointerLinkedList<Character>();
	}

	@Override
	public PointerLinkedList find(char toFind)
	{
		return _baseList.find(toFind);
	}

	@Override
	public void add(int seqNum, char chr)
	{
		_baseList.add(seqNum, chr);
	}
}
