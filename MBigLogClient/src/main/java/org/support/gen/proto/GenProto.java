package org.support.gen.proto;



import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.UnknownFieldSet;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * @author wangzhanwei
 */
 @SuppressWarnings("unchecked")
public class GenProto {
    private final String OUTPUT_PATH;
    private final String OUTPUT_PACKAGE;
    private List<File> protoFileList = new ArrayList();
    private List<File> descFileList = new ArrayList();
    private List<MessageInfo> messageInfos = new ArrayList();
    protected String pageEncoding = "UTF-8";

    public static void main(String[] args) throws Exception {
        String outputPath;
        String outputPackage;
        if(args.length<2){
            outputPath = "./src/main/java/";
            outputPackage = "org.gen";
        }else{
            outputPath = args[0];
            outputPackage = args[1];
        }
        GenProto parse = new GenProto(outputPath, outputPackage);
        parse.gen();
    }

    public GenProto(String outputPath, String outputPackage) {
        this.OUTPUT_PATH = outputPath;
        this.OUTPUT_PACKAGE = outputPackage;
    }

    public void gen() throws Exception {
        this.clearDescFile();

        try {
            this.processProtoFile();
            this.genProtoDesc();
            this.genMsgUtil();
            this.genProtoClass();
        } catch (Exception e) {
            throw e;
        } finally {
            this.clear();
        }

    }

    private void clearDescFile() {
        File dir = new File("./msg//protol//server/");
        File[] descFiles = dir.listFiles((d, n) -> {
            return n.endsWith(".desc");
        });
        if (descFiles != null) {
            File[] fliterDescFiles = descFiles;
            int length = descFiles.length;

            for (int i = 0; i < length; ++i) {
                File descFile = fliterDescFiles[i];
                descFile.delete();
            }
        }

    }

    private void processProtoFile() throws IOException {
        File optionsProtoFile = new File("./msg//protol//server/", "options.proto");
        if(!optionsProtoFile.exists()) {
            System.err.println("目录：" + optionsProtoFile.getAbsolutePath() + " 不存在options.proto文件，生成失败");
        } else {
            this.protoFileList.add(optionsProtoFile);
            String packageStr = "package " + this.OUTPUT_PACKAGE + ";\n\n";
            File dir = new File("./msg//protol/");
            File[] protoFiles = dir.listFiles((d, n) -> {
                return n.endsWith(".proto");
            });
            File[] filterProtoFiles = protoFiles;
            int length = protoFiles.length;

            for(int i = 0; i < length; ++i) {
                File protoFile = filterProtoFiles[i];
                String fileName = protoFile.getName();
                String[] temp = fileName.split("_");
                if(temp.length != 3) {
                    System.out.println("ignore:" + protoFile.getPath());
                } else {
                    fileName = temp[2];
                    StringBuilder sb = new StringBuilder(fileName);
                    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                    fileName = "Msg" + sb.toString();
                    File newFile = new File(protoFile.getParent() + "/server/", fileName);
                    System.out.println("newFile:" + newFile);
                    String[] header;
                    if(temp[2].equalsIgnoreCase("def.proto")) {
                        header = new String[]{"import \"options.proto\";\n", packageStr};
                        this.fileCopy(protoFile, newFile, header);
                    } else {
                        header = new String[]{"import \"options.proto\";\n", "import \"MsgDef.proto\";\n", packageStr};
                        this.fileCopy(protoFile, newFile, header);
                    }

                    this.protoFileList.add(newFile);
                }
            }
        }
    }

     public void genProtoDesc() throws Exception {
        Iterator iterator = this.protoFileList.iterator();

        while(iterator.hasNext()) {
            File protoFile = (File)iterator.next();
            this.genProtoDesc(protoFile);
        }

    }

    private void genProtoDesc(File protoFile) throws Exception {
        String descFileDir = protoFile.getName().replaceAll(".proto", ".desc");
        StringBuffer cmd = new StringBuffer();
        File protoc = new File("./msg/");
        cmd.append(protoc.getAbsolutePath() + "/protoc.exe");
        cmd.append(" -I=").append(protoc.getAbsolutePath() + "/protol/" + "/server/");
        cmd.append(" --descriptor_set_out=").append(protoc.getAbsolutePath() + "/protol/" + "/server/" + descFileDir);
        cmd.append(" ").append(protoc.getAbsolutePath() + "/protol/" + "/server/" + protoFile.getName());
        String failMsg = "gen desc file error.";
        this.executeCommand(cmd.toString(), failMsg);
        this.descFileList.add(new File(protoc.getAbsolutePath() + "/protol/" + "/server/" + descFileDir));
    }

    public void genMsgUtil() throws Exception {
        File dir = new File("./msg//protol//server/");
        File[] protoFiles = dir.listFiles((d, n) -> {
            return n.endsWith(".desc");
        });
        File[] filterProtoFiles = protoFiles;
        int length = protoFiles.length;

        for(int i = 0; i < length; ++i) {
            File protoFile = filterProtoFiles[i];
            this.getMsgInfo(protoFile);
        }

        Configuration cfg = new Configuration();
        TemplateLoader loader = new FileTemplateLoader(new File("./src/main/java/org/support/gen/template/"));
        cfg.setTemplateLoader(loader);
        cfg.setEncoding(Locale.getDefault(), this.pageEncoding);
        Map<String, Object> valueMap = new HashMap();
        valueMap.put("messageInfos", this.messageInfos);
        valueMap.put("packageName", this.OUTPUT_PACKAGE);
        Template template = cfg.getTemplate("MsgUtil.ftl", this.pageEncoding);
        template.setEncoding(this.pageEncoding);
        File filePath = new File(this.OUTPUT_PATH, this.OUTPUT_PACKAGE.replace(".", File.separator));
        if(!filePath.exists()) {
            filePath.mkdirs();
        }

        File targetFile = new File(filePath, "MsgIds.java");
        if(targetFile.exists()) {
            targetFile.delete();
        }

        template.process(valueMap, new OutputStreamWriter(new FileOutputStream(targetFile, false), this.pageEncoding));
    }

    public void genProtoClass() throws Exception {
        Iterator iterator = this.protoFileList.iterator();

        while(iterator.hasNext()) {
            File protoFile = (File)iterator.next();
            this.genProtoClass(protoFile);
        }

    }

     private void genProtoClass(File protoFile) throws Exception {
        StringBuffer cmd = new StringBuffer();
        File protoc = new File("./msg/");
        cmd.append(protoc.getAbsolutePath() + "/protoc.exe");
        cmd.append(" -I=").append(protoc.getAbsolutePath() + "/protol/" + "/server/");
        cmd.append(" --java_out=").append(this.OUTPUT_PATH);
        cmd.append(" ").append(protoc.getAbsolutePath() + "/protol/" + "/server/" + protoFile.getName());
        this.executeCommand(cmd.toString(), "gen class error.");
    }
     public void clear() {
        Iterator iterator = this.protoFileList.iterator();

        File descFile;
        while(iterator.hasNext()) {
            descFile = (File)iterator.next();
            if(!descFile.getName().equals("options.proto")) {
                descFile.delete();
            }
        }

        iterator = this.descFileList.iterator();

        while(iterator.hasNext()) {
            descFile = (File)iterator.next();
            if(descFile.exists()) {
                descFile.delete();
            }
        }

    }
    private void getMsgInfo(File descFile) throws Exception {
        FileInputStream in = new FileInputStream(descFile);
        Throwable exception = null;

        try {
            DescriptorProtos.FileDescriptorSet fdSet = DescriptorProtos.FileDescriptorSet.parseFrom(in);
            Iterator fdSetIterator = fdSet.getFileList().iterator();

            label123:
            while(fdSetIterator.hasNext()) {
                DescriptorProtos.FileDescriptorProto fdp = (DescriptorProtos.FileDescriptorProto)fdSetIterator.next();
                String className = fdp.getName().replaceAll(".proto", "");
                Iterator iterator = fdp.getMessageTypeList().iterator();

                while(true) {
                    DescriptorProtos.DescriptorProto dp;
                    DescriptorProtos.MessageOptions options;
                    do {
                        if(!iterator.hasNext()) {
                            continue label123;
                        }

                        dp = (DescriptorProtos.DescriptorProto)iterator.next();
                        options = dp.getOptions();
                    } while(options == null);

                    String name = dp.getName();
                    Integer id = null;
                    UnknownFieldSet uf = options.getUnknownFields();

                    UnknownFieldSet.Field field;
                    for(Iterator ufIterator = uf.asMap().entrySet().iterator(); ufIterator.hasNext(); id = Integer.valueOf(((Long)field.getVarintList().get(0)).intValue())) {
                        Map.Entry<Integer, UnknownFieldSet.Field> entry = (Map.Entry)ufIterator.next();
                        field = (UnknownFieldSet.Field)entry.getValue();
                    }

                    if(id != null) {
                        this.messageInfos.add(new MessageInfo(id.intValue(), name, className));
                    }
                }
            }
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            if(in != null) {
                if(exception != null) {
                    try {
                        in.close();
                    } catch (Throwable e) {
                        exception.addSuppressed(e);
                    }
                } else {
                    in.close();
                }
            }

        }

    }

    private void executeCommand(String cmd, String failMsg) throws Exception {
        Runtime run = Runtime.getRuntime();
        Process p = run.exec(cmd);
        if(p.waitFor() != 0 && p.exitValue() == 1) {
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = "";

            while((line = in.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println();
        }

    }
    public void fileCopy(File s, File t, String[] header) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            int length = header.length;

            for(int i = 0; i < length; ++i) {
                String str = header[i];
                out.write(ByteBuffer.wrap(str.getBytes()));
            }

            in.transferTo(0L, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
