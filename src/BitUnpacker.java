
import java.io.*;
import java.util.List;


/**
 *
 * @author FRANKIE
 */
public class BitUnpacker
{
    
    private int dictionarySize = 0;
    private int bufferCount = 0;
    private int bufferIndex = 0;
    private byte[] byteBuffer = new byte[1000];
    private FileInputStream inputStream;
    private Decompressor _decomp;
   
    public BitUnpacker(FileInputStream inputStream)
    {
        this.inputStream = inputStream;
        try
        {
            _decomp = new Decompressor(4000000, new FileOutputStream("out.decomp"));
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
            //System.out.println("buffer " + buffer);
            //BytesUtil.printIntBits(buffer);
            // loop
            while(buffer != -1)
            {
                
                //System.out.println("Start of loop buffer ");
                //BytesUtil.printIntBits(buffer);
                // finding phrase number
                
                phraseNumLength = BytesUtil.getBitsNeeded(dictionarySize);
                //System.out.println("dictionary size " + dictionarySize + " phrase num length " + phraseNumLength);
                
                while (!foundPhraseNum)
                {
                    //System.out.println("finding phrase num");
                    // if the ditionary size is zero then there is no phrase number
                    if(phraseNumLength == 0)
                    {
                        //System.out.println("phrase number is zero");
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
                        //System.out.println("leftover buffer ");
                        //BytesUtil.printIntBits(buffer);
                        // set phrase number and leftover based on which is larger
                        if (phraseNumLength < leftover)
                        {
                            phraseNumber = (buffer << (8-leftover)) >>> (8-phraseNumLength);
                            leftover -= phraseNumLength;
                            foundPhraseNum = true;
                            
                            //System.out.println("leftover size " + leftover);
                            break;
                        }
                        else
                        {
                            phraseNumLength -= leftover;
                            //System.out.println("phrasenumlength " + phraseNumLength);
                            phraseNumber = buffer;
                            buffer = nextByte();
                            //System.out.println("buffer " + buffer);
                            //BytesUtil.printIntBits(buffer); 
                            leftover = 0;//8-phraseNumLength;
                            //System.out.println("leftover set " + leftover);
                        }
                    }
                    // if phrase number is less than 8 bits long then the byte read will contain some of the byte sequence
                    if(phraseNumLength <= 8)
                    { 
                        //System.out.println("phrase number less than a byte");
                        phraseNumber = (phraseNumber << phraseNumLength) | (buffer >>> 8-phraseNumLength);                        
                        leftover = 8 - phraseNumLength;
                        foundPhraseNum = true;
                        break;
                    } else
                    // otherwise the phrase number takes multiple bytes and will need to be built
                    {
                        //System.out.println("phrase number spans more than 1 byte");
                        phraseNumber = phraseNumber << 8 | buffer;            
                        phraseNumLength -= 8;
                        buffer = nextByte();
                        //System.out.println("buffer " + buffer);
                        //BytesUtil.printIntBits(buffer);
                    }
                }
                

                // finding byte sequence
                
                //System.out.println("finding byte seq");
                if(leftover > 0)
                {
                    //BytesUtil.printIntBits(buffer);
                    
                    //System.out.println("leftover " + leftover);
                    // move leftover into position, shift and shift back to remove what was already used to find phrase number
                    int l = (((buffer << (32 - leftover))) >>> (32 - leftover)) << (8-leftover);
                    
                    // readin rest of byte sequence
                    buffer = nextByte();
                    
                    // temp int with rest of byte sequence shifted into position
                    int d = buffer >>> leftover;
                    
                    byteSequence = l | d;
                    
                    // break out of loop if the end of file has been reached and avoid outputing
                    if(buffer == -1)
                        break;
                    // leftover is still the same
                } else
                if(phraseNumLength == 0)
                {
                    // if there is nothing in dictonary then there is no phrase number
                    byteSequence = buffer;
                }else
                // no leftover and dictonary size is not zero
                {
                    buffer = nextByte();
                    // break out of loop if the end of file has been reached and avoid outputing
                    if(buffer == -1)
                        break;
                    //System.out.println("buffer " + buffer);
                    //BytesUtil.printIntBits(buffer);
                    byteSequence = buffer;
                }
                dictionarySize++;
                foundPhraseNum = false;
                
                // TODO chuck output shit here
                //System.out.println("phrase num " + phraseNumber);
                //System.out.println("byte seq  " + byteSequence);
                //BytesUtil.printBytes(BytesUtil.intToBytes(byteSequence, 1));
                _decomp.process(phraseNumber, BytesUtil.intToBytes(byteSequence,1)[0]);
                // TODO chuck output shit here
               
                // clear phrase number so no residual bits are left
                phraseNumber = 0;
                if(leftover == 0)
                {
                    //System.out.println("no left");
                    buffer = nextByte();
                }
            }
            
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
                //System.out.println("bytes in buffer "+ bufferCount);
                bufferIndex = 0;
            }catch(Exception e){}
        }
        // if end of file is not reached then return next byte
        if(bufferCount > 0)
        {
            //System.out.println("bytes index "+ bufferIndex + " out of " + bufferCount);
            b = BytesUtil.bytesToInt(byteBuffer, bufferIndex, 1);
            bufferIndex++;
        }
        
        // if end of file was reached, then -1 would be returned
        return b;
    }
    
}