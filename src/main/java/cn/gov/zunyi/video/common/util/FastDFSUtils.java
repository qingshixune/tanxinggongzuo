package cn.gov.zunyi.video.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadCallback;

/**
 * 上传图片到FastDFS
 */
public class FastDFSUtils {

	static {
		try {
			ClientGlobal.init(FastDFSUtils.class.getClassLoader().getResource("fdfs_client.conf").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public static String[] uploadPic(String path, String fileName, long size) {
		String[] fileIds = null;
		try {
			// ClientGloble 读配置文件
			// 老大客户端
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			String extName = FilenameUtils.getExtension(fileName);

			NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("fileName", fileName);
			meta_list[1] = new NameValuePair("fileExt", extName);
			meta_list[2] = new NameValuePair("fileSize", String.valueOf(size));

			// http://172.16.15.244:8081/group1/M00/00/00/rBAP9FfFG62AZsuBAADeW7MfEHA287.png
			// group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
			fileIds = storageClient.upload_file(path, extName, meta_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileIds;
	}

	public static String[] uploadPic(InputStream inStream, String fileName, long size) {
		String[] fileIds = null;
		try {
			// ClientGloble 读配置文件
			// 老大客户端
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			String extName = FilenameUtils.getExtension(fileName);

			NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("fileName", fileName);
			meta_list[1] = new NameValuePair("fileExt", extName);
			meta_list[2] = new NameValuePair("fileSize", String.valueOf(size));

			// http://172.16.15.244:8081/group1/M00/00/00/rBAP9FfFG62AZsuBAADeW7MfEHA287.png
			// group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
			fileIds = storageClient.upload_file(null, size, new UploadFileSender(inStream), extName, meta_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileIds;
	}

	public static String uploadPic(byte[] pic, String fileName, long size) {
		String[] fileIds = null;
		try {
			// ClientGloble 读配置文件
			// 老大客户端
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			String extName = FilenameUtils.getExtension(fileName);
			// 设置图片meta信息
			NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("fileName", fileName);
			meta_list[1] = new NameValuePair("fileExt", extName);
			meta_list[2] = new NameValuePair("fileSize", String.valueOf(size));
			// 上传且返回path
			fileIds = storageClient.upload_file(pic, extName, meta_list);
			return fileIds[0] + "/" + fileIds[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean deletePic(String fileUrl) {
		try {
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			StorePath storePath = StorePath.praseFromUrl(fileUrl);
			int i = storageClient.delete_file(storePath.getGroup(), storePath.getPath());
			System.out.println(i == 0 ? "删除成功" : "删除失败:" + i);
			return i == 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String deletePic(String[] fileIds) {
		try {
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			int i = storageClient.delete_file(fileIds[0], fileIds[1]);
			System.out.println(i == 0 ? "删除成功" : "删除失败:" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileIds[1];
	}

	private static class UploadFileSender implements UploadCallback {
		private InputStream inStream;

		public UploadFileSender(InputStream inStream) {
			this.inStream = inStream;
		}

		public int send(OutputStream out) throws IOException {
			int readBytes;
			while ((readBytes = inStream.read()) > 0) {
				out.write(readBytes);
			}
			return 0;
		}
	}

}