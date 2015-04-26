import java.io.*;
import java.util.List;

/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class BitUnpacker implements IUnpacker
{
    
    private int dictionarySize = 0;
    private int bufferCount = 0;
    private int bufferIndex = 0;
    private byte[] byteBuffer = new byte[1024];
    private InputStream inputStream;
    private OutputStream outputStream;
    private Decompressor _decomp;
   
    public BitUnpacker(InputStream inputStream, OutputStream outputStream)
    {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        try
        {
            _decomp = new Decompressor(100000000,  outputStream);
        }catch(Exception e){}
    }
    
    public void UnpackBits()
    {
        int phraseNumber = 0;
        int leftover = 0;
        int byteSequence = 0;
        int buffer;
        int phraseNumLength;
        boolean foundPhraseNum = false;
        
        try
        {
            buffer = nextByte();
            // loop
            while(buffer != -1)
            {
                
                // finding phrase number
				
                phraseNumLength = BytesUtil.getBitsNeeded(dictionarySize);
                
                while (!foundPhraseNum)
                {
                    // if the dictionary size is zero then there is no phrase number
                    if(phraseNumLength == 0)
                    {
                        // skip and look for byte array
                        foundPhraseNum = true;
                        leftover = 0;
                        break;
                    } else
                    // if there is carry over, concat that to the phrase num
                    if(leftover > 0)
                    {
                        // shift and shift back to remove already processed bits
                        buffer = (buffer << 32-leftover) >>> (32-leftover);
                        // set phrase number and leftover based on which is larger
                        if (phraseNumLength < leftover)
                        {
                            phraseNumber = (buffer << (8-leftover)) >>> (8-phraseNumLength);
                            leftover -= phraseNumLength;
                            foundPhraseNum = true;
                            break;
                        }
                        else
                        {
                            phraseNumLength -= leftover;
                            phraseNumber = buffer;
                            buffer = nextByte();
                            leftover = 0;
                        }
                    }
                    // if phrase number is less than 8 bits long then the byte read will contain some of the byte sequence
                    if(phraseNumLength <= 8)
                    { 
                        phraseNumber = (phraseNumber << phraseNumLength) | (buffer >>> 8-phraseNumLength);                        
                        leftover = 8 - phraseNumLength;
                        foundPhraseNum = true;
                        break;
                    } else
                    // otherwise the phrase number takes multiple bytes and will need to be built
                    {
                        phraseNumber = phraseNumber << 8 | buffer;            
                        phraseNumLength -= 8;
                        buffer = nextByte();
                    }
                }
                

                // finding byte sequence
                
                if(leftover > 0)
                {
                    //BytesUtil.printIntBits(buffer);
                    
                    // move leftover into position, shift and shift back to remove what was already used to find phrase number
                    int l = (((buffer << (32 - leftover))) >>> (32 - leftover)) << (8-leftover);
                    
                    // readin rest of byte sequence
                    buffer = nextByte();
					
                    // break out of loop if the end of file has been reached and avoid outputting
                    if(buffer == -1)
                        break;
						
                    // temp int with rest of byte sequence shifted into position
                    int d = buffer >>> leftover;
                    
                    byteSequence = l | d;
                    
                    // leftover is still the same
                } else
                if(phraseNumLength == 0)
                {
                    // if there is nothing in dictionary then there is no phrase number
                    byteSequence = buffer;
                }else
                // no leftover and dictionary size is not zero
                {
                    buffer = nextByte();
                    // break out of loop if the end of file has been reached and avoid outputting
                    if(buffer == -1)
                        break;
						
                    byteSequence = buffer;
                }
				
                dictionarySize++;
                foundPhraseNum = false;
                
				// pass values to decompressor
                _decomp.process(phraseNumber, BytesUtil.intToBytes(byteSequence,1)[0]);
               
                // clear phrase number so no residual bits are left
                phraseNumber = 0;
				// read next byte if all bits have been used
                if(leftover == 0)
                {
                    buffer = nextByte();
                }
            }
            
            
            outputStream.flush();
            outputStream.close();
        }catch (Exception ex){ex.printStackTrace();};
        
    }
    
    // returns next byte in the buffer
    private int nextByte()
    {
        int b = -1;
        // if the end of the buffer has been reached then fill the buffer with the file
        if(bufferIndex >= bufferCount)
        {
            try 
            {
                bufferCount = inputStream.read(byteBuffer);
                bufferIndex = 0;
            }catch(Exception e){}
        }
        // if end of file is not reached then return next byte
        if(bufferCount > 0)
        {
            b = (byte)byteBuffer[bufferIndex] & 0xFF;
			bufferIndex++;
        }
        
        // if end of file was reached, then -1 would be returned
        return b;
    }
    
}