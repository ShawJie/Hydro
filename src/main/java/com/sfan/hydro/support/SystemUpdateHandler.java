package com.sfan.hydro.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.util.FileUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@Component
public class SystemUpdateHandler extends TextWebSocketHandler {

    @Value("${hydro.version}")
    private String currentVersion;

    private static String remoteVersion;

    private static final String FILE_SUFFIX = ".zip";
    private static final String NEW_VERSION_PATH = "https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/hydro-%s%s";
    private static final String VERSION_CHECK = "https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/lastest_version";
    private Logger logger = LoggerFactory.getLogger(SystemUpdateHandler.class);

    private WebSocketSession session;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        this.session = session;
        String payload = message.getPayload();
        switch (payload){
            case "check":
                try {
                    sendMessage(checkUpdate());
                }catch (IOException e){
                    logger.error("check update failed", e);
                }
                break;
            case "download":
                try {
                    sendMessage(downloadUpdate());
                }catch (IOException e){
                    logger.error("download update failed", e);
                }
                break;
            case "install":
                updateSystem();
                break;
        }
    }

    private void sendMessage(ReturnMessage message) throws IOException{
        String responseModel = mapper.writeValueAsString(message);
        this.session.sendMessage(new TextMessage(responseModel));
    }

    private ReturnMessage checkUpdate() throws IOException {
        ReturnMessage message = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(VERSION_CHECK);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                remoteVersion = EntityUtils.toString(response.getEntity(), "UTF-8").replace("\n", "");
                if(versionCompare(currentVersion, remoteVersion) < 0){
                    logger.info("System gets an update - Version: {}", remoteVersion);
                    message = new ReturnMessage("check", true, remoteVersion);
                }else{
                    message = new ReturnMessage("check", false, "");
                }
            }
        }finally {
            if(response != null){
                response.close();
            }
            httpClient.close();
        }
        return message;
    }

    private ReturnMessage downloadUpdate() throws IOException{
        if(remoteVersion != null && versionCompare(currentVersion, remoteVersion) < 0){
            Runnable download = () -> {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = null;
                try {
                    response = httpClient.execute(new HttpGet(String.format(NEW_VERSION_PATH, remoteVersion, FILE_SUFFIX)));
                    File rootLocation = FileUtil.filepathResolver(FileType.Update.getPath());
                    if(!rootLocation.exists()){
                        rootLocation.mkdirs();
                    }

                    try (ReadableByteChannel in = Channels.newChannel(response.getEntity().getContent());
                         FileChannel out = new FileOutputStream(new File(rootLocation, "hydro" + FILE_SUFFIX)).getChannel()){
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
                        long size = new URL(String.format(NEW_VERSION_PATH, remoteVersion, FILE_SUFFIX)).openConnection().getContentLength();
                        while (in.read(buffer) != -1){
                            buffer.flip();
                            out.write(buffer);
                            buffer.clear();
                            sendMessage(new ReturnMessage("process", true, (int)((out.position() * 1.0 / size) * 100)));
                        }
                    }
                    sendMessage(new ReturnMessage("download", true, "download finish"));

                    FileUtil.deCompressRecursion(new FileInputStream(FileUtil.filepathResolver(FileType.Update.getPath() + "hydro" + FILE_SUFFIX)), null, FileType.Update.getPath() + "hydro");

                    sendMessage(new ReturnMessage("decompress", true, "decompress finish"));
                }catch (IOException e){
                    logger.error("{} - Download update failed", Thread.currentThread().getName(), e);
                }finally {
                    try {
                        if(response != null){
                            response.close();
                        }
                        httpClient.close();
                    }catch (IOException e){}
                }
            };
            new Thread(download).start();
        }else {
            return new ReturnMessage("download", false, "System is up to date, need not download update package");
        }
        return new ReturnMessage("download", true, "Update package download started");
    }

    private void updateSystem() throws IOException{
        String osName = System.getProperty("os.name");
        Process process;
        String homeDir = System.getProperty("user.dir");
        logger.info("Ready to restart server: homeDir:{} osName:{}", homeDir, osName);
        if(osName.toLowerCase().startsWith("windows")){
            logger.info("execute cmd: {}", String.format("cmd.exe /c %s\\hydro-cli -u", homeDir));
            process = Runtime.getRuntime()
                    .exec(String.format("cmd.exe /c %s\\hydro-cli -u", homeDir));
        }else{

        }
    }

    private int versionCompare(String version1, String version2){
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        for(int i = 0; i < minLength; i++){
            if(versionArray1[i].length() < versionArray2[i].length()){
                return -1;
            }else if(versionArray1[i].compareTo(versionArray2[i]) < 0){
                return -1;
            }
        }
        return versionArray1.length - versionArray2.length;
    }

    class ReturnMessage{
        private String cmd;
        private boolean success;
        private Object data;

        public String getCmd() {
            return cmd;
        }
        public void setCmd(String cmd) {
            this.cmd = cmd;
        }
        public boolean isSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }

        public ReturnMessage(String cmd, boolean success, Object data) {
            this.cmd = cmd;
            this.success = success;
            this.data = data;
        }
    }
}
