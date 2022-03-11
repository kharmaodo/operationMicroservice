package sn.free.selfcare.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.jcraft.jsch.SftpException;
import org.springframework.web.multipart.MultipartFile;

public interface FileTransferService {
	boolean uploadFile(MultipartFile file, String toPath);
	CompletableFuture<byte[]> downloadFromFtp(String remoteFilePath) throws IOException, SftpException;
	byte[] downloadFile(String localFilePath);
}
