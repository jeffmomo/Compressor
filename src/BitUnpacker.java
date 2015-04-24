
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
        // test case should return 7 as parse number with a dictonary size of 7 and a byte of 1111 0101 (245 as an int)
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
        
        int parseNumber = 0;
        int leftover = 0;
        int byteSequence = 0;
        int buffer;
        int parseNumLength;
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
                // finding parse number
                
                parseNumLength = BytesUtil.getBitsNeeded(dictionarySize);
                System.out.println("dictionary size " + dictionarySize + " parse num length " + parseNumLength);
                
                while (!foundParseNum)
                {
                    System.out.println("finding parse num");
                    // if the ditionary size is zero then there is no parse number
                    if(parseNumLength == 0)
                    {
                        System.out.println("parse number is zero");
                        // skip and look for byte array
                        foundParseNum = true;
                        leftover = 0;
                        break;
                    } else
                    // if there is carry over, concat that to the parse num
                    if(leftover > 0)
                    {
                        // shift and shift back to remove already processed bits
                        buffer = (buffer << 32-leftover) >>> (32-leftover);
                        System.out.println("leftover buffer ");
                        BytesUtil.printIntBits(buffer);
                        // set parse number and leftover based on which is larger
                        if (parseNumLength < leftover)
                        {
                            parseNumber = (buffer << (8-leftover)) >>> (8-parseNumLength);
                            leftover -= parseNumLength;
                            foundParseNum = true;
                            
                            System.out.println("leftover size " + leftover);
                            break;
                        }
                        else
                        {
                            parseNumLength -= leftover;
                            System.out.println("parsenumlength " + parseNumLength);
                            parseNumber = buffer;
                            buffer = inputStream.read();
                            System.out.println("buffer " + buffer);
                            BytesUtil.printIntBits(buffer); 
                            leftover = 0;//8-parseNumLength;
                            System.out.println("leftover set " + leftover);
                        }
                    } else
                    // if parse number is less than 8 bits long then the byte read will contain some of the byte sequence
                    if(parseNumLength <= 8)
                    { 
                        System.out.println("parse number less than a byte");
                        parseNumber = (parseNumber << parseNumLength) | (buffer >>> 8-parseNumLength);                        
                        leftover = 8 - parseNumLength;
                        foundParseNum = true;
                        break;
                    } else
                    // otherwise the parse number takes multiple bytes and will need to be built
                    {
                        System.out.println("parse number spans more than 1 byte");
                        parseNumber = parseNumber << 8 | buffer;            
                        parseNumLength -= 8;
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
                if(parseNumLength == 0)
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
                System.out.println("parse num " + parseNumber);
                System.out.println("byte seq  " + byteSequence);
                BytesUtil.printBytes(BytesUtil.intToBytes(byteSequence, 1));
	            _decomp.process(parseNumber, (byte) byteSequence);
                // TODO chuck output shit here
               
                if(leftover == 0)
                {
                    System.out.println("no left");
                    buffer = inputStream.read();
                }
            }
            
        }catch (Exception ex){ex.printStackTrace();};
        
    }
    
    
    
}