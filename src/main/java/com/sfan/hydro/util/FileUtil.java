package com.sfan.hydro.util;

import com.sfan.hydro.domain.enumerate.FileType;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static boolean writeInFile(byte[] fileBytes, String filePath, String fileName) throws IOException{
		Path rootLocation = Paths.get(filepathResolver(filePath).getPath());
		if(Files.notExists(rootLocation)){
			Files.createDirectories(rootLocation);
		}
		Path path = rootLocation.resolve(fileName);
		Files.write(path, fileBytes);
		return true;
	}

	public static File getFile(FileType type, String fileName){
		File file = filepathResolver(type.getPath() + fileName);
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	public static String getFileContext(FileType type, String filePath) throws IOException{
		File file = filepathResolver(type.getPath() + filePath);
		if(file.exists()){
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuilder content = new StringBuilder();
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				content.append(temp);
				content.append("\r\n");
			}
			fileReader.close();
			return content.toString();
		}
		return "";
	}
	
	public static boolean deleteFile(FileType type, String filePath) throws IOException{
		File targetFile = filepathResolver(type.getPath() + filePath);
		if(!targetFile.exists())
			return true;
		deleteFile(targetFile);
		return true;
	}

	private static void deleteFile(File file){
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File item : files) {
				deleteFile(item);
			}
		}
		file.delete();
	}

	public static File deCompressRecursion(InputStream archiveFile, FileType fileType, String deCompressLocation) throws IOException{
		File rootPath;
		if(fileType == null){
			rootPath = new File(deCompressLocation);
		}else{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL url = classLoader.getResource(fileType.getPath().replaceFirst(ResourceUtils.CLASSPATH_URL_PREFIX + "/", ""));
			rootPath = new File(url.getFile() + deCompressLocation);
		}

		if(!rootPath.exists()){
			rootPath.mkdir();
		}

		ZipInputStream zin = new ZipInputStream(archiveFile);
		ZipEntry zipEntry;
		byte[] buffer = new byte[1024 * 10];
		while((zipEntry = zin.getNextEntry()) != null){
			String fileName = zipEntry.getName();
			File file = new File(rootPath.getPath() + File.separator + fileName);
			if(zipEntry.isDirectory()){
				file.mkdir();
			}else{
				FileOutputStream fout = new FileOutputStream(file);
				int len;
				while ((len = zin.read(buffer)) > 0){
					fout.write(buffer, 0, len);
				}
				fout.close();
			}
		}
		zin.closeEntry();
		zin.close();
		return rootPath;
	}

	public static File filepathResolver(String filePath){
		File file = null;
		if(filePath.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)){
			filePath = filePath.replace("classpath:", "");

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL url = classLoader.getResource(filePath);
			if(url == null){
				File rootPath = null;
				try {
					rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
				}catch (FileNotFoundException e){

				}
				file = new File(rootPath.getAbsolutePath(), filePath);
			}else{
				file = new File(url.getFile());
			}
		}else if (filePath.startsWith(ResourceUtils.FILE_URL_PREFIX)){
			file = new File(filePath.replace("file:", ""));
		}else{
			file = new File(filePath);
		}
		return file;
	}
}
