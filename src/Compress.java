import java.io.*;
/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class Compress 
{
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        try
        {
                BufferedInputStream fis = null;
                BufferedOutputStream bos = null;
                if(args.length >0)
                {
                    fis = new BufferedInputStream(new FileInputStream(args[0]));
                    bos = new BufferedOutputStream(new FileOutputStream(args[0] + ".lz78"));
                }
                else
                {
                    fis = new BufferedInputStream(System.in);
                    bos = new BufferedOutputStream(System.out);
                }
                
                Compressor c = new Compressor(bos);

                int z = fis.read();
                while (z != -1)
                {
                        //System.err.println(z);
                        c.process((byte) z);
                        z = fis.read();
                }
                c.finalise();
                bos.flush();
                bos.close();
        } catch (Exception e)
        {
                e.printStackTrace();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\ntotal runtime: "+ totalTime);
    }
}
