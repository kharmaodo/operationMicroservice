package sn.free.selfcare.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.service.FileTransferService;

@Service
public class FileTransferServiceImpl implements FileTransferService {

	private Logger log = LoggerFactory.getLogger(FileTransferServiceImpl.class);

	private String host;

	private Integer port;

	private String username;

	private String password;

	private Integer sessionTimeout;

	private Integer channelTimeout;

	private final ApplicationProperties applicationProperties;

	public FileTransferServiceImpl(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
		this.host = this.applicationProperties.getSftp().getHost();
		this.port = this.applicationProperties.getSftp().getPort();
		this.username = this.applicationProperties.getSftp().getUsername();
		this.password = this.applicationProperties.getSftp().getPassword();
		this.sessionTimeout = this.applicationProperties.getSftp().getSessionTimeout();
		this.channelTimeout = this.applicationProperties.getSftp().getChannelTimeout();
	}

	@Override
	@Async
	public CompletableFuture<byte[]> downloadFromFtp(String remoteFilePath) throws IOException, SftpException {
		byte[] fileData = null;
		ChannelSftp channelSftp = createChannelSftp();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		   if (channelSftp != null) {
				channelSftp.get(remoteFilePath, baos);
				fileData = baos.toByteArray();
				baos.flush();
			}
		} finally {
			disconnectChannelSftp(channelSftp);
		}
		return CompletableFuture.completedFuture(fileData);
	}

	private ChannelSftp createChannelSftp() {
		try {
			JSch jSch = new JSch();
			Session session = jSch.getSession(username, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect(sessionTimeout);
			Channel channel = session.openChannel("sftp");
			channel.connect(channelTimeout);
			return (ChannelSftp) channel;
		} catch(JSchException ex) {
			log.error("Create ChannelSftp error", ex);
		}

		return null;
	}

	private void disconnectChannelSftp(ChannelSftp channelSftp) {
		try {
			if( channelSftp == null)
				return;

			if(channelSftp.isConnected())
				channelSftp.disconnect();

			if(channelSftp.getSession() != null)
				channelSftp.getSession().disconnect();

		} catch(Exception ex) {
			log.error("SFTP disconnect error", ex);
		}
	}
    //TODO : MD : Perf : Privilégier des APIs existants et non natif ou à défaut du java.nio
	@Override
	public byte[] downloadFile(String localFilePath) {
		String factureRootPah = applicationProperties.getFactureConf().getRootPath();
		Path fullPath = Paths.get(factureRootPah, localFilePath);
		File file = fullPath.toFile();
		if (file.exists() && file.isFile() && file.canRead()) {
			try {
				return FileUtils.readFileToByteArray(file);
			}
			catch (IOException e) {
				log.error("Unable to read file", e);
				return null;
			}
		}
		return null;
	}

    //TODO : MD : Perf : Privilégier des APIs existants et non natif ou à défaut du java.nio
	@Override
	public boolean uploadFile(MultipartFile file, String toPath) {
		String factureRootPah = applicationProperties.getFactureConf().getRootPath();
		Path fullPath = Paths.get(factureRootPah, toPath);
		Path parentPath = fullPath.getParent();
		if (parentPath != null) {
			try {
				Files.createDirectories(parentPath);
				File dir = parentPath.toFile();
				if (dir.exists() && dir.isDirectory() && dir.canWrite()) {
					File f = fullPath.toFile();
					f.createNewFile();
					try (InputStream inputStream = file.getInputStream(); FileOutputStream outputStream = new FileOutputStream(f)) {
						byte[] buffer = new byte[2048];
						int length;
						/*copying the contents from input stream to
			    	     * output stream using read and write methods
			    	     */
			    	    while ((length = inputStream.read(buffer)) > 0){
			    	    	outputStream.write(buffer, 0, length);
			    	    }
					}
				}
				return true;
			}
			catch (IOException e1) {
				log.warn("Saving invoice file error : {}", e1.getMessage());
				return false;
			}
		}
		return false;
	}

}
