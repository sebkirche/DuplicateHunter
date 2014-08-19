package fr.seki.duphunter;

import java.io.IOException;

/**
 * A console progressor when the total amount of steps is known
 * @author Sebastien
 */
public class DefinedConsoleProgressor implements Progressor {

    /**
     * maximum value represented
     */
    private long totalCount;
    /**
     * width of the visual representation in characters
     */
    private final int width;
    private byte[] data;

    public DefinedConsoleProgressor(long total) {
        totalCount = total;
        width = 50;
        allocData(width);
    }

    public DefinedConsoleProgressor(long total, int visualWidth) {
        totalCount = total;
        width = visualWidth;
        allocData(width);
    }

    private void allocData(int width) {
        data = new byte[width + 1];
        data[0] = '\r';
        data[1] = '[';
        data[width] = ']';
    }

    public void setTotalCount(long total) {
        totalCount = total;
    }

    public void progress(long current) {
        int p;

        p = (int) ((double) current / totalCount * width);

        int i = 2;
        for (; i <= p - 1; i++) {
            data[i] = '.';
        }
        for (; i < width; i++) {
            data[i] = ' ';
        }
        try {
            System.out.write(data);
        } catch (IOException ex) {
            //Logger.getLogger(DefinedConsoleProgressor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	/** 
	 * progressor main class, to make some tests
	 */
    public static void main(String[] args) throws InterruptedException {
        int max = 100, visu = 50;

        System.out.println("Usage: DefinedConsoleProgressor [max] [width]");
        if (args.length > 0) {
            max = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            visu = Integer.parseInt(args[1]);
        }
        DefinedConsoleProgressor p = new DefinedConsoleProgressor(max, visu);
        for (int i = 0; i <= max; i++) {
            p.progress(i);
            Thread.sleep(50);
        }
        System.out.println(" Done.");
    }
}
