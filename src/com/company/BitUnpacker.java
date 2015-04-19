
import java.io.*;
import java.util.List;


/**
 *
 * @author FRANKIE
 */
public class BitUnpacker {
    
    private int dictionarySize = 0;
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
            printIntBits(inputStream.read());*/
            
            BitUnpacker b = new BitUnpacker();
            b.dictionarySize = 7;
            b.UnpackBits(inputStream);
            
        } catch(Exception e){};
        
        
    }
    
    
    
    public BitUnpacker()
    {
        
    }
    
    public void UnpackBits(FileInputStream inputStream)
    {
        
        int parseNumber = 0;
        int leftover = 0;
        int byteSequence = 0;
        int buffer;
        boolean foundParseNum = false;

        
        try
        {
            
            buffer = inputStream.read();
            System.out.println("buffer " + buffer);
            printIntBits(buffer);
            // loop
            while(buffer != -1)
            {
                
                // finding parse number
                
                int parseNumLength = getBitsNeeded(dictionarySize);
                System.out.println("parse num length " + parseNumLength);
                
                while (!foundParseNum)
                {
                    // if the ditionary size is zero then there is no parse number
                    if(parseNumLength == 0)
                    {
                        
                        System.out.println("parse number is zero");
                        // skip and look for byte array
                        foundParseNum = true;
                        leftover = 0;
                        buffer = inputStream.read();
                        break;
                    } else
                    // if there is carry over, concat that to the parse num
                    if(leftover > 0)
                    {
                        // shift and shift back to remove already processed bits
                        parseNumber = (buffer << 32-leftover) >>> (32-leftover);
                        parseNumLength -= leftover;
                        leftover = 0;
                    } else
                    // if parse number is less than 8 bits long then the byte read will contain some of the byte sequence
                    if(parseNumLength <= 8)
                    {
                        parseNumber = (parseNumber << parseNumLength) | (buffer >>> 8-parseNumLength);                        
                        leftover = 8 - parseNumLength;
                        parseNumLength = 0;
                        foundParseNum = true;
                        break;
                    } else
                    // otherwise the parse number takes multiple bytes and will need to be built
                    {
                        System.out.println("parse number spans more than 1 byte");
                        parseNumber = parseNumber << (parseNumLength - 8 )| buffer;            
                        parseNumLength -= 8;
                        buffer = inputStream.read();
                    }
                }
                System.out.println("leftover size " + leftover);

                // finding byte sequence
                if(leftover > 0)
                {
                    // shift and shift back to remove what was already used
                    buffer = (buffer << 32 - leftover) >>> (32 - leftover);
                    byteSequence = buffer << (8-leftover);
                    // go on to next byte
                    buffer = inputStream.read();
                    byteSequence = byteSequence | (buffer >>> leftover);
                    
                    // break out of loop if the end of file has been reached
                    if(buffer == -1)
                        break;                    
                    // leftover is still the same
                } else
                {
                    byteSequence = inputStream.read();
                }
                dictionarySize++;
                foundParseNum = false;
                
                // TODO chuck output shit here
                System.out.println("final parse number" + parseNumber);
                BytesUtil.printBytes(BytesUtil.intToBytes(byteSequence, 1));
                
               
                if(leftover == 0)
                {
                    buffer = inputStream.read();
                }
            }
            
        }catch (Exception ex){ex.printStackTrace();};
        
    }
    
    // prints int bits
    public static void printIntBits(int num)
    {
         int mask = 1 << 31;
         for(int i=1; i<=32; i++) 
         {
            if( (mask & num) != 0 )
                System.out.print(1);
            else
                System.out.print(0);

            if( (i % 4) == 0 )
                System.out.print(" ");

            mask = mask >>> 1;
        }
         System.out.println();
    }    
    // returns number of bytes needed to store phrase number
    public static int getBytesNeeded(int bits)
    {
        return (int) Math.ceil((double)(bits)/8);
    }
    
    // returns number of bits needed to store bits
    public static int getBitsNeeded(int dictonarySize)
    {
        int bitsNeeded = 0;
        while((dictonarySize)/(Math.pow(2, bitsNeeded)) >= 1)
        {
            bitsNeeded++;
        }
        return bitsNeeded;
    }
    
}