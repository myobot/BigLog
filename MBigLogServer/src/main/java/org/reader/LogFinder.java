package org.reader;


import org.connection.ClientLinker;
import org.gen.MsgTrans;
import org.support.Config;
import org.support.Utils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.support.Utils.transString;

/**
 * @author wangzhanwei
 */
public class LogFinder extends Thread {

    private String sentence;
    private int count;
    private int orderModel;
    private boolean run = true;
    private long id;
    private int matchCase;
    private int wholeWords;
    private int showRowNum;
    private int totalCount;
    private int usePattern;
    private RandomAccessFile randomAccessFile;
    private ClientLinker clientLinker;
    private String currentLine = "";

    public LogFinder(long id, int orderModel, String sentence, int count, int totalCount, int matchCase, int wholeWords, int showRowNum, int usePattern, ClientLinker clientLinker) {
        this.id = id;
        this.orderModel = orderModel;
        this.sentence = sentence;
        this.count = count;
        this.matchCase = matchCase;
        this.wholeWords = wholeWords;
        this.showRowNum = showRowNum;
        this.usePattern = usePattern;
        this.totalCount = totalCount;
        this.clientLinker = clientLinker;
        try {
            randomAccessFile = new RandomAccessFile(Config.PATH, "r");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Override
//    public void run(){
//        MsgTrans.SCFindLogPush.Builder msg = MsgTrans.SCFindLogPush.newBuilder();
//        msg.setId(id);
//        if(run){
//            msg.setAnswer(grep());
//        }else{
//            msg.setAnswer(null);
//        }
//        clientLinker.sendMsg(msg);
//    }
//    private String grep(){
//        String command = "grep ";
//        if(showRowNum == 1){
//            command = command + " -n ";
//        }
//        if(usePattern == 1){
//            command = command + " -e '" +sentence+"'";
//        }else{
//            if(matchCase == 2){
//                command = command + " -i ";
//            }
//            if(wholeWords == 1){
//                command = command + " -w ";
//            }
//        }
//        if(orderModel == 1){
//            command = command + " '" + sentence + "' " + Config.PATH + " | " + " head -n " + count;
//        }else {
//            command = command + " '" + sentence + "' " + Config.PATH + " | " + " tail -n " + count;
//        }
//        System.out.println(command);
//
//        try {
//            Process process = Runtime.getRuntime().exec(command);
//
//            System.out.println("1");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder stringBuilder = new StringBuilder();
//            int c = -1;
//            while(true){
//                c = bufferedReader.read();
//                if(c == -1){
//                    System.out.println(-1);
//                    break;
//                }
//                stringBuilder.append((char)c);
//            }
//            System.out.println(2);
//            return stringBuilder.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    @Override
    public void run() {
        MsgTrans.SCFindLogPush.Builder msg = MsgTrans.SCFindLogPush.newBuilder();
        msg.setId(id);
        if (run) {
            switch (orderModel) {
                case 1:
                    orderModel1(msg);
                    break;
                case 2:
                    orderModel2(msg);
                    break;
            }
        } else {
            msg.setAnswer("");
            clientLinker.sendMsg(msg);
        }
    }

    public boolean filter(String line) {
        String matchRegx = sentence;
        String l = line;
        if (usePattern == 2) {
            matchRegx = transString(matchRegx);
            if (wholeWords == 1) {
                matchRegx = ".*\\b(" + matchRegx + ")\\b.*";
            } else {
                matchRegx = ".*" + matchRegx + ".*";
            }
            if (matchCase == 2) {
                matchRegx = matchRegx.toLowerCase();
                l = line.toLowerCase();
            }
        }
        Pattern pattern = Pattern.compile(matchRegx);
        Matcher m = pattern.matcher(l);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private class Model2Reader {
        RandomAccessFile randomAccessFile;
        long nextend;

        public Model2Reader(RandomAccessFile randomAccessFile) {
            try {
                this.randomAccessFile = randomAccessFile;
                this.nextend = randomAccessFile.length() - 1;
                this.randomAccessFile.seek(nextend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String nextLine() throws Exception {
            if (nextend < 0) {
                return null;
            }

            int c;
            while (nextend >= 0) {
                c = randomAccessFile.read();
                if (c == '\n') {
                    String line = randomAccessFile.readLine();
                    if (line != null) {
                        line = new String(line.getBytes("8859_1"), "utf-8");
                        return line;
                    }
                }
                if (nextend == 0) {
                    randomAccessFile.seek(0);
                    String line = randomAccessFile.readLine();
                    if (line != null) {
                        line = new String(line.getBytes("8859_1"), "utf-8");
                        return line;
                    }
                    nextend = -1;
                } else {
                    randomAccessFile.seek(nextend - 1);
                }
                nextend--;
            }

            return null;
        }
    }

    public void orderModel2(MsgTrans.SCFindLogPush.Builder msg) {
        int nowCount = 0;
        int nowTotal = 0;
        Model2Reader model2Reader = new Model2Reader(randomAccessFile);
        String log;
        try {
            while ((log = getLastLog(model2Reader)) != null) {
                if (filter(log)) {
                    msg.setAnswer(log);
                    clientLinker.sendMsg(msg);
                    nowCount++;
                    if (nowCount == count) {
                        break;
                    }
                }
                nowTotal++ ;
                if(nowTotal >= totalCount){
                    break;
                }
            }
        } catch (Exception e) {
            msg.setAnswer("");
            clientLinker.sendMsg(msg);
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MsgTrans.SCFindLogFinish.Builder msg1 = MsgTrans.SCFindLogFinish.newBuilder();
            msg1.setId(this.id);
            clientLinker.sendMsg(msg1);
        }
    }

    public void orderModel1(MsgTrans.SCFindLogPush.Builder msg) {
        int nowCount = 0;
        int nowTotal = 0;
        try {
            String log;
            while ((log = getNextLog(randomAccessFile)) != null) {
                if (filter(log)) {
                    msg.setAnswer(new String(log.getBytes("8859_1"), "utf-8"));
                    clientLinker.sendMsg(msg);
                    nowCount++;
                    if (nowCount == count) {
                        break;
                    }
                }
                nowTotal++;
                if(nowTotal >= totalCount){
                    break;
                }
            }
        } catch (Exception e) {
            msg.setAnswer("");
            clientLinker.sendMsg(msg);
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MsgTrans.SCFindLogFinish.Builder msg1 = MsgTrans.SCFindLogFinish.newBuilder();
            msg1.setId(this.id);
            clientLinker.sendMsg(msg1);
        }
    }

    private String getNextLog(RandomAccessFile fileReader) throws IOException {
        if (!this.isLog(this.currentLine)) {
            while ((this.currentLine = fileReader.readLine()) != null && !this.isLog(this.currentLine)) {}
        }
        if (!this.isLog(this.currentLine)) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(this.currentLine).append("\n");
            while ((this.currentLine = fileReader.readLine()) != null && !this.isLog(this.currentLine)) {
                stringBuilder.append(this.currentLine).append("\n");
            }
            return stringBuilder.toString();
        }
    }
    private String getLastLog(Model2Reader model2Reader) throws Exception {
        if (!this.isLog(this.currentLine)) {
            while ((this.currentLine = model2Reader.nextLine()) != null && !this.isLog(this.currentLine)) {
            }
        }
        this.currentLine = model2Reader.nextLine();
        if(isLog(this.currentLine)){
            return this.currentLine+"\n";
        }
        StringBuilder stringBuilder = new StringBuilder(this.currentLine).insert(0,"\n");
        while ((this.currentLine = model2Reader.nextLine()) != null && !this.isLog(this.currentLine)) {
            stringBuilder.insert(0,this.currentLine).insert(0,"\n");
        }
        stringBuilder.insert(0,this.currentLine);
        return stringBuilder.toString();

    }

    private boolean isLog(String line) {
        return line == null ? false : Utils.logPattern.matcher(line).find();
    }
}
