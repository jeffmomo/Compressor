package com.company;

/**
 * Created by Jeff on 8/04/2015.
 */
public class Decompressor
{
	private String _output = "";
	private int[] _sequenceArray;
	private char[] _charArray;
	private int _position;


	public Decompressor(int maxSequence)
	{
		_sequenceArray = new int[maxSequence];
		_charArray = new char[maxSequence];
	}

	public void process(int seqNum, char chr)
	{
		_sequenceArray[++_position] = seqNum;
		_charArray[_position] = chr;

		char currentChar = chr;
		String tempString = "";
		while(seqNum != 0)
		{
			currentChar = _charArray[seqNum];
			seqNum = _sequenceArray[seqNum];
			tempString = currentChar + tempString;
		}
		_output += tempString + chr;
	}

	public String getOutput()
	{
		return _output;
	}

	private String getString(int seq)
	{
		if(seq == 0)
			return "";
		else
			return getString(_sequenceArray[seq]) + _charArray[seq];
	}

}
