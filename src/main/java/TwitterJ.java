import twitter4j.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TwitterJ {
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    private List<String> terms;
    private String popularWord;
    private int frequencyMax;

    public TwitterJ(PrintStream console)
    {
        // Makes an instance of Twitter - this is re-useable and thread safe.
        // Connects to Twitter and performs authorizations.
        twitter = TwitterFactory.getSingleton();
        consolePrint = console;
        statuses = new ArrayList<>();
        terms = new ArrayList<>();
        //these were added because they weren't intialized in the constructor
        popularWord = "";
        frequencyMax = 0;
    }

    /*  Part 1 */
    /*
     * This method tweets a given message.
     * @param String  a message you wish to Tweet out
     */
    public void tweetOut(String message) throws TwitterException, IOException
    {
        twitter.updateStatus(message);
    }


    /*  Part 2 */
    /*
     * This method queries the tweets of a particular user's handle.
     * @param String  the Twitter handle (username) without the @sign
     */
    @SuppressWarnings("unchecked")
    public void queryHandle(String handle) throws TwitterException, IOException
    {
        statuses.clear();
        terms.clear();
        fetchTweets(handle);
        splitIntoWords();
        removeCommonEnglishWords();
        sortAndRemoveEmpties();
    }

    /*
     * This method fetches the most recent 2,000 tweets of a particular user's handle and
     * stores them in an arrayList of Status objects.  Populates statuses.
     * @param String  the Twitter handle (username) without the @sign
     */
    public void fetchTweets(String handle) throws TwitterException, IOException
    {
        // Creates file for dedebugging purposes
        PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt"));
        Paging page = new Paging (1,200);
        int p = 1;
        while (p <= 10)
        {
            page.setPage(p);
            statuses.addAll(twitter.getUserTimeline(handle,page));
            p++;
        }
        int numberTweets = statuses.size();
        fileout.println("Number of tweets = " + numberTweets);

        int count=1;
        for (Status j: statuses)
        {
            fileout.println(count+".  "+j.getText());
            count++;
        }
    }

    /*
     * This method takes each status and splits them into individual words.
     * Remove punctuation by calling removePunctuation, then store the word in terms.
     */
    public void splitIntoWords()
    {
        String temp = "";
        String[] tempArr;
        for (Status s: statuses) {
            temp = removePunctuation(s.getText());
            tempArr = temp.split(" ");
            for (String str : tempArr) {
                terms.add(str);
            }
        }
    }

    /*
     * This method removes common punctuation from each individual word.
     * Consider reusing code you wrote for a previous lab.
     * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
     * @ param String  the word you wish to remove punctuation from
     * @ return String the word without any punctuation
     */
    private String removePunctuation( String s )
    {
        //In cases where there's stuff at the end (ex: "hello!!") the !! isn't deleted but works for rest
        // ' also doesn't work in any case
        s = s.trim();
        for (int i = 0; i < s.length(); i++) {
            if (i == s.length()) {
                if (s.indexOf(",") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("!") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("'") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("?") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("$") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("%") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("^") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("&") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("*") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("(") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf(")") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("-") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("_") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf("/") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf(".") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf(";") == i) {
                    s = s.substring(0,i);
                } else if (s.indexOf(":") == i) {
                    s = s.substring(0,i);
                }
            } else {
                if (s.indexOf(",") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("!") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("'") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("?") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("$") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("%") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("^") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("&") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("*") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("(") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf(")") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("-") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("_") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf("/") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf(".") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf(";") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                } else if (s.indexOf(":") == i) {
                    s = s.substring(0,i) + s.substring(i+1);
                }
            }
        }
        return s;

    }

    /*
     * This method removes common English words from the list of terms.
     * Remove all words found in commonWords.txt  from the argument list.
     * The count will not be given in commonWords.txt. You must count the number of words in this method.
     * This method should NOT throw an exception.  Use try/catch.
     */
    @SuppressWarnings("unchecked")
    private void removeCommonEnglishWords() {
        try (FileReader file = new FileReader("commonWords.txt");
             BufferedReader reader = new BufferedReader(file);
             FileWriter fileW = new FileWriter("output.txt");
             BufferedWriter writer = new BufferedWriter(fileW)
        ) {
            String line;
                while ((line = reader.readLine()) != null) {
                    for (int i = terms.size() - 1; i >=0 ; i--) {
                        if (line.equalsIgnoreCase(terms.get(i))) {
                            terms.remove(i);
                        }
                    }
                }
            //printing remaining terms in new file
            for (String term: terms) {
                writer.write(term + "\n");
            }
        } catch (FileNotFoundException badFile) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("error");
        }

//        for (int i = 0; i < terms.size; i++) {
//            terms.set(i, i.get(i))
//        }
    }

    /*
     * This method sorts the words in terms in alphabetically (and lexicographic) order.
     * You should use your sorting code you wrote earlier this year.
     * Remove all empty strings while you are at it.
     */
    @SuppressWarnings("unchecked")
    public void sortAndRemoveEmpties()
    {
        String temp = "";
        String early = "";
        int earlyI = 0;

        //this is adding random spaces but it's in order
        for (int i = 0; i < terms.size()/2; i++) {
            early = terms.get(i);
            earlyI = i;
            for (int j = i; j < terms.size(); j++) {
                //finding the first word
                if (terms.get(j).compareTo(early) < 0) {
                    early = terms.get(j);
                    earlyI = j;
                }
            }
            temp = terms.get(i);
            terms.set(i, early);
            terms.set(earlyI, temp);


            for (int t = terms.size() - 1; t >= 0; t--){
                if (terms.get(t).equals("") || terms.get(t).equals(" ") || terms.get(t) == null){
                    terms.remove(t);
                }
            }

        }
        for (int t = terms.size() - 1; t >= 0; t--){
            if (terms.get(t) == null || terms.get(t).equals(" ")){
                terms.remove(t);
            }
        }


    }

    /*
     * This method returns the most common word from terms.
     * Consider case - should it be case sensitive?  The choice is yours.
     * @return String the word that appears the most times
     * @post will populate the frequencyMax variable with the frequency of the most common word
     */
    @SuppressWarnings("unchecked")
    public String mostPopularWord()
    {
        removeCommonEnglishWords();
        sortAndRemoveEmpties();
        int count = 1;
        popularWord = terms.get(0);
        frequencyMax = 1;
        //skips to each new word
        for (int i = 0; i < terms.size(); i+= count) {
            int j = i;
            count = 1;
            while (j + 1 < terms.size() && terms.get(j + 1).equalsIgnoreCase(terms.get(j))) {
                count++;
                j++;
            }
            if (count > frequencyMax) {
                popularWord = terms.get(i);
                frequencyMax = count;
            }
        }
        return popularWord;


        /*alternate way
        ArrayList<String> temp = new ArrayList<>();
        for (String s: terms) {
            temp.add(s);
        }
        //this is a high potential spot for bugs
        for (int i = 0; i < temp.size(); i++) {
            int current = 0;
            //Goes through temp list checking if it matches current one being looked at; skips over current one being looked at
            for (int j = temp.size() - 1 ; j >= 0; j--) {
                if (j!= i && (temp.get(i).equalsIgnoreCase(temp.get(j)))) {
                    current++;
                    temp.remove(j);
                }
            }
            //adds the count of the current term being looked at
            current = current + 1;
            if (current > frequencyMax) {
                popularWord = temp.get(i);
                frequencyMax = current;
            }
        }
        return popularWord;
        */
    }

    /*
     * This method returns the number of times the most common word appears.
     * Note:  variable is populated in mostPopularWord()
     * @return int frequency of most common word
     */
    public int getFrequencyMax()
    {
        return frequencyMax;
    }


    /*  Part 3 */
    private boolean isInList (ArrayList<String> arr, String target) {
        for (String s: arr) {
            if (target.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void investigate ()
    {
        try {
            List<Status> mentions = new ArrayList<>();
            mentions = twitter.getMentionsTimeline();
            ArrayList<String> usernames = new ArrayList<>();
            ArrayList<Integer> counts = new ArrayList<>();
            String maxUsername = "";
            int maxCount = 0;
            int count = 0;
            for (int i = 0; i < mentions.size(); i++) {
                for (int j = 0; j < mentions.size(); j++) {
                    if ((mentions.get(j).getUser().getName().equals(mentions.get(i).getUser().getName()))
                            && !isInList(usernames,mentions.get(j).getUser().getName())) {
                        count++;
                    }
                }
                usernames.add(mentions.get(i).getUser().getName());
                counts.add(count);
            }

            for (int i = 0; i < counts.size()/2; i++) {
                int max = 0;
                int maxIndex = 0;
                for (int j = 0; j < counts.size(); j++) {
                    if (counts.get(j) > counts.get(j + 1)){
                        max = counts.get(j);
                        maxIndex = j;
                    }
                }
                int temp = count;
                count = max;
                counts.set(maxIndex, temp);
                String tempU = usernames.get(i);
                usernames.set(i, usernames.get(maxIndex));
                usernames.set(maxIndex, tempU);
            }

            //print out ordered mentions
            for (int i = 0; i < usernames.size(); i ++) {
                System.out.println("Username" + i + ":" + usernames.get(i));
                System.out.println("Number of mentions:" + counts.get(i));
                System.out.println();
            }

        } catch (TwitterException t) {
            t.printStackTrace();
        }
    }

    /*
     * This method determines how many people near Churchill Downs
     * tweet about the Kentucky Derby.
     */
    public void sampleInvestigate ()
    {
        Query query = new Query("Kentucky Derby");
        query.setCount(100);
        query.setGeoCode(new GeoLocation(38.2018,-85.7687), 5, Query.MILES);
        query.setSince("2021-5-1");
        try {
            QueryResult result = twitter.search(query);
            System.out.println("Count : " + result.getTweets().size()) ;
            for (Status tweet : result.getTweets()) {
                System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText());
            }
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
