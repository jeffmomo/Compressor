package com.company;


import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Jeff on 7/04/2015.
 */
public class Compressor
{

	private OutputStream _stream;
	private Trie _trie;

	public Compressor(OutputStream os)
	{
		_stream = os;
		_trie = new Trie();
	}

	public void process(byte in) throws IOException
	{
		int seq = 0;
//		while (z != -1)
//		{
//			//System.err.println(z);
//			seq = _trie.advance(in);
//			if (seq != -1)
//			{
//				_stream.write(seq);
//				_stream.write(z);
//			}
//		}
	}
}
