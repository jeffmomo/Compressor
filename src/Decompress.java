import java.io.*;
/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class Decompress
{
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            IUnpacker bu = null;
            if(args.length >0)
            {
                bu = new BitUnpacker(new BufferedInputStream(new FileInputStream(args[0]))
                                    ,new BufferedOutputStream(new FileOutputStream(args[0].replace(".lz78", ""))));
            } else
            {
                bu = new BitUnpacker(new BufferedInputStream(System.in)
                                    ,new BufferedOutputStream(System.out));
            }
            bu.UnpackBits();
        } catch (Exception e)
        {
                e.printStackTrace();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\ntotal runtime: "+ totalTime);
    }
}
