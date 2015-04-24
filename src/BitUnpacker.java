
import java.io.*;
import java.util.List;


/**
 *
 * @author FRANKIE
 */
public class BitUnpacker implements IUnpacker
{
    
    private int dictionarySize = 0;
	private Decompressor _decomp;
    //public List<Pair> = new List<Pair>();
    
     public static void main(String[] args) 
    {
        // test case should return 7 as phrase number with a dictonary size of 7 and a byte of 1111 0101 (245 as an int)
        // 111 1111 0101
        // 1111 1110 1011 1111
        
        
        try
        {
            
            File file = new File("f");
            
            /*FileOutputStream f = new FileOutputStream("f");
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            b.write(BytesUtil.intToBytes(254, 1));
            b.write(BytesUtil.intToBytes(191, 1));
            
            f.write(b.toByteArray());
            b.close();
            f.close();*/
            
            
            FileInputStream inputStream = new FileInputStream(file);
            
            /*
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());
            printIntBits(inputStream.read());*/
            
            BitUnpacker b = new BitUnpacker();
            //b.dictionarySize = 7;
            b.UnpackBits(inputStream);
            
        } catch(Exception e){};
        
        
    }
    
    
    
    public BitUnpacker()
    {
	    try
	    {
		    _decomp = new Decompressor(4000000, new FileOutputStream("out.decomp"));
	    }catch(Exception e){}
    }
    
    public void UnpackBits(FileInputStream inputStream)
    {
        
        int phraseNumber = 0;
        int leftover = 0;
        int byteSequence = 0;
        int buffer;
        int phraseNumLength;
        boolean foundParseNum = false;

        
        try
        {
            
            buffer = inputStream.read();
            System.out.println("buffer " + buffer);
            BytesUtil.printIntBits(buffer);
            // loop
            while(buffer != -1)
            {
                
                System.out.println("Start of loop buffer ");
                BytesUtil.printIntBits(buffer);
                // finding phrase number
                
                phraseNumLength = BytesUtil.getBitsNeeded(dictionarySize);
                System.out.println("dictionary size " + dictionarySize + " phrase num length " + phraseNumLength);
                
                while (!foundParseNum)
                {
                    System.out.println("finding phrase num");
                    // if the ditionary size is zero then there is no phrase number
                    if(phraseNumLength == 0)
                    {
                        System.out.println("phrase number is zero");
                        // skip and look for byte array
                        foundParseNum = true;
                        leftover = 0;
                        break;
                    } else
                    // if there is carry over, concat that to the phrase num
                    if(leftover > 0)
                    {
                        // shift and shift back to remove already processed bits
                        buffer = (buffer << 32-leftover) >>> (32-leftover);
                        System.out.println("leftover buffer ");
                        BytesUtil.printIntBits(buffer);
                        // set phrase number and leftover based on which is larger
                        if (phraseNumLength < leftover)
                        {
                            phraseNumber = (buffer << (8-leftover)) >>> (8-phraseNumLength);
                            leftover -= phraseNumLength;
                            foundParseNum = true;
                            
                            System.out.println("leftover size " + leftover);
                            break;
                        }
                        else
                        {
                            phraseNumLength -= leftover;
                            System.out.println("phrasenumlength " + phraseNumLength);
                            phraseNumber = buffer;
                            buffer = inputStream.read();
                            System.out.println("buffer " + buffer);
                            BytesUtil.printIntBits(buffer); 
                            leftover = 0;//8-phraseNumLength;
                            System.out.println("leftover set " + leftover);
                        }
                    }
                    // if phrase number is less than 8 bits long then the byte read will contain some of the byte sequence
                    if(phraseNumLength <= 8)
                    { 
                        System.out.println("phrase number less than a byte");
                        phraseNumber = (phraseNumber << phraseNumLength) | (buffer >>> 8-phraseNumLength);                        
                        leftover = 8 - phraseNumLength;
                        foundParseNum = true;
                        break;
                    } else
                    // otherwise the phrase number takes multiple bytes and will need to be built
                    {
                        System.out.println("phrase number spans more than 1 byte");
                        phraseNumber = phraseNumber << 8 | buffer;            
                        phraseNumLength -= 8;
                        buffer = inputStream.read();
                        System.out.println("buffer " + buffer);
                        BytesUtil.printIntBits(buffer);
                    }
                }
                

                // finding byte sequence
                
                System.out.println("finding byte seq");
                if(leftover > 0)
                {
                    BytesUtil.printIntBits(buffer);
                    
                    System.out.println("leftover " + leftover);
                    // move leftover into position
                    int l = (((buffer << (32 - leftover))) >>> (32 - leftover)) << (8-leftover);
                    
                    // readin rest of byte sequence
                    buffer = inputStream.read();
                    
                    // shift into position
                    int d = buffer >>> leftover;
                    
                    
                    byteSequence = l | d;
                    
                    // shift and shift back to remove what was already used
                    
                    /*
                    //buffer = (buffer << 32 - leftover) >>> (32 - leftover);
                    byteSequence = (buffer << 32 - leftover) >>> (32 - leftover) << (8-leftover);
                    System.out.println("leftover byte seq len" + leftover);
                    printIntBits(byteSequence);
                    
                    // go on to next byte
                    buffer = inputStream.read();
                    System.out.println("buffer " + buffer);
                    printIntBits(buffer);
                    byteSequence = byteSequence | (buffer >>> leftover);
                    
                    System.out.println("byte seq + leftover ");
                    printIntBits(byteSequence);*/
                    
                    // break out of loop if the end of file has been reached
                    if(buffer == -1)
                        break;
                    // leftover is still the same
                    
                } else
                if(phraseNumLength == 0)
                {
                    byteSequence = buffer;
                }else
                {
                    buffer = inputStream.read();
                    System.out.println("buffer " + buffer);
                    BytesUtil.printIntBits(buffer);
                    byteSequence = buffer;
                }
                dictionarySize++;
                foundParseNum = false;
                
                // TODO chuck output shit here
                System.out.println("phrase num " + phraseNumber);
                System.out.println("byte seq  " + byteSequence);
                BytesUtil.printBytes(BytesUtil.intToBytes(byteSequence, 1));
	            _decomp.process(phraseNumber, BytesUtil.intToBytes(byteSequence,1)[0]);
                // TODO chuck output shit here
               
                // clear phrase number so no residual bits are left
                phraseNumber = 0;
                if(leftover == 0)
                {
                    System.out.println("no left");
                    buffer = inputStream.read();
                }
            }
            
        }catch (Exception ex){ex.printStackTrace();};
        
    }
    
    
    
}