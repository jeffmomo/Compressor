package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

/**
 * Created by Jeff on 8/04/2015.
 */
public class Decompressor
{
	private int[] _sequenceArray;
	private byte[] _charArray;
	private int _position;
	private OutputStream _stream;


	public Decompressor(int maxSequence, OutputStream stream)
	{
		_sequenceArray = new int[maxSequence];
		_charArray = new byte[maxSequence];
		_stream = stream;
	}

	public void process(int seqNum, byte chr) throws IOException
	{
		_sequenceArray[++_position] = seqNum;
		_charArray[_position] = chr;

		byte currentChar = chr;
		LightList list = new LightList(currentChar);
		while(seqNum != 0)
		{
			currentChar = _charArray[seqNum];
			seqNum = _sequenceArray[seqNum];

			list = list.add(currentChar);
		}
		while(list != null)
		{
			_stream.write(list.value);
			list = list.prev;
		}
	}

}
