package com.utils;

import java.io.*;

public class FileOperatorUtils {

	/**
	 * 
	 * @param filePathAndName
	 * @return
	 */
	public static String readFile(String filePathAndName, String charset) {
		StringBuffer sb = new StringBuffer("");

		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			if (null != charset)
				isr = new InputStreamReader(
						new FileInputStream(filePathAndName), charset);
			else
				isr = new InputStreamReader(
						new FileInputStream(filePathAndName));

			br = new BufferedReader(isr);
			String str = null;

			while ((str = br.readLine()) != null) {
				sb.append(str + "\r\n");
			}
		} catch (FileNotFoundException e) {
			System.out.println("读取文件[" + filePathAndName + "]捕捉到异常");
			return null;
		} catch (IOException e) {
			System.out.println("读取文件[" + filePathAndName + "]捕捉到异常");
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException e) {
				System.out.println("关闭文件[" + filePathAndName + "]捕捉到异常" + e);
				return null;
			}
		}
		return sb.toString();
	}

	/**
	 * create folder by the given name
	 * 
	 * @param folderPath
	 * @return
	 */
	@SuppressWarnings("finally")
	public static boolean createFolder(String folderPath) {
		boolean result = false;
		File newFolder = new File(folderPath);
		try {
			if (!newFolder.exists())
				result = newFolder.mkdirs();
			else
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建目录[" + folderPath + "]失败" + e);
		} finally {
			return result;
		}

	}

	/**
	 * delete file
	 * 
	 * @param filePathAndName
	 * @return
	 */
	@SuppressWarnings("finally")
	public static boolean deleteFile(String filePathAndName) {
		boolean result = false;
		File delFile = new File(filePathAndName);
		try {
			if (delFile.exists()) {
				delFile.delete();
				result = true;
			} else {
				result = false;
				System.out.println("删除文件[" + filePathAndName + "]失败，该文件不存在");
			}
		} catch (Exception e) {
			System.out.println("删除文件[" + filePathAndName + "]失败");
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	/**
	 * move the file from oldpath to newpath
	 * 
	 * @param filename
	 * @param oldpath
	 * @param newpath
	 * @param cover
	 */
	@SuppressWarnings("finally")
	public static boolean moveFile(String filename, String oldpath,
			String newpath, boolean cover) {
		File oldfile = new File(oldpath + File.separator + filename);
		File newfile = new File(newpath + File.separator + filename);
		if (!oldfile.exists())
			return false;
		if (!newfile.exists())
			createFolder(newpath);
		boolean result = false;
		try {
			if (!oldpath.equals(newpath)) {

				if (newfile.exists()) {// 若在待转移目录下，已经存在待转移文件
					if (cover)// 覆盖
					{
						deleteFile(newfile.getAbsolutePath());
						result = oldfile.renameTo(newfile);
					} else
						System.out.println("文件[" + filename + "]已存在");
				} else {
					result = oldfile.renameTo(newfile);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("移动文件失败"+ e);
		} finally {
			return result;
		}
	}

	public static void write2File(String fileDirectoryAndName, String content,
			String charset) {
		// FileWriter fw = null;

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			String fileName = fileDirectoryAndName;
			File myFile = new File(fileName);
			if (!myFile.exists())
				myFile.createNewFile();
			else {
				// 如果存在重复文件，则覆盖
				myFile.delete();
				// throw new Exception("the new file already exists!");
			}
			// fw = new FileWriter(myFile);
			// fw.write(content);

			fos = new FileOutputStream(myFile);
			osw = new OutputStreamWriter(fos, charset);
			osw.write(content);
			osw.flush();
		} catch (Exception e) {
			System.out.println("创建文件出错！" + e);
			e.printStackTrace();
		} finally {
			try {
				// if (fw != null)
				// fw.close();

				if (osw != null)
					osw.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭文件失败"+ e);
			}
		}
	}

	public static void main(String[] args) {
		// boolean test=FileOperatorUtils.moveFile("1406442673523.txt",
		// "D:\\logs", "D:\\logs\\temp", true);
		String test = FileOperatorUtils.readFile(
				"C:/Users/windows/Desktop/关键短语提取/in.txt", "utf-8");
		// System.out.println();
		System.out.println(test);
	}
}
