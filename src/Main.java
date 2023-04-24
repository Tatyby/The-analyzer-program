import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static String a = "a";
    public static String b = "b";
    public static String c = "c";
    public static int sizeQuele = 100;
    public static String letters = "abc";
    public static int textLength = 100;
    public static int strCount = 10_000;

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(sizeQuele, true);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(sizeQuele, true);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(sizeQuele, true);

    public static void main(String[] args) throws InterruptedException {


        new Thread(() -> {
            for (int i = 0; i < strCount; i++) {
                try {
                    String str = generateText(letters, textLength);
                    queueA.put(str);
                    queueB.put(str);
                    queueC.put(str);
                } catch (InterruptedException e) {
                    return;
                }
            }
            //printQuele(queue1);
        }).start();



        Thread searchSumbolA = new Thread(() -> {

            searchForTheMaximumNumberOfCharacters(queueA, a);
        });

        searchSumbolA.start();
        searchSumbolA.join();

        Thread searchSumbolB = new Thread(() -> {

            searchForTheMaximumNumberOfCharacters(queueB, b);
        });

        searchSumbolB.start();
        searchSumbolB.join();

        Thread searchSumbolC = new Thread(() -> {

            searchForTheMaximumNumberOfCharacters(queueC, c);
        });

        searchSumbolC.start();
        searchSumbolC.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void searchForTheMaximumNumberOfCharacters(BlockingQueue<String> queue, String symbol) {
        int countMax = 0;
        String str = null;
        for (int i = 0; i < queue.size(); i++) {
            int count;
            try {
                count = queue.take().length() - queue.take().replace(symbol, "").length();
            } catch (InterruptedException e) {
                return;
            }
            if (count > countMax) {
                countMax = count;
                try {
                    str = queue.take();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        System.out.printf("строка с максимальным вхождением символа %s : %s ", symbol, str);
        System.out.println();

    }

}