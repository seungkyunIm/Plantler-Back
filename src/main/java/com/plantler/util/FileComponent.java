package com.plantler.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.dto.FileDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileComponent {
	
	private final String ROOT = (new File("").getAbsolutePath()); 
	private final String PATH = "upload";
	private final String OS = "\\";

	private String getRootPath(String subPath) {
		return (subPath != null) ?
				ROOT.concat(OS).concat(PATH).concat(OS).concat(subPath)			
			:
				ROOT.concat(OS).concat(PATH);
	}
	
	private String getFileExtension(String originalFilename) {
        // 파일 확장자 추출 로직 수정
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            return ""; // 확장자 없음
        }
    }
	
	public File getFile(String url) {
		String path = getRootPath(url);
	    return new File(path);
	}
	
	public FileDTO addFile(MultipartFile multipartFile) throws IllegalStateException, IOException  {
		String file_server_name = UUID.randomUUID().toString(); 
		String file_name = multipartFile.getOriginalFilename();
		String file_type = multipartFile.getContentType();
		String file_url = file_server_name;
		String file_extension = getFileExtension(multipartFile.getOriginalFilename());
		int file_sort = 0;
		
		String path = getRootPath(null);
		log.info("path : {}", path);
		
		File directory = new File(path);
        if (!directory.exists()) {
            System.out.println("Directory does not exist. Creating directory..." + path);
            directory.mkdirs();
        }
		
        File file = new File(path.concat(OS).concat(file_server_name));
		multipartFile.transferTo(file);
		// 파일 저장 완료!!
		System.out.println("FILE UPLOADED SUCCESSFULLY");
		
		return FileDTO.builder()
			.file_server_name(file_server_name)
			.file_name(file_name)
			.file_type(file_type)
			.file_url(file_url)
			.file_extension(file_extension)
			.file_sort(file_sort)
			.build();
	}
	
	public FileDTO editFile(FileDTO fileDTO, MultipartFile multipartFile) throws IllegalStateException, IOException {
		String file_server_name = UUID.randomUUID().toString();
        String file_name = multipartFile.getOriginalFilename();
        String file_type = multipartFile.getContentType();
        String file_extension = getFileExtension(file_name);
        String file_url = file_server_name;
        fileDTO.setFile_server_name(file_server_name);
        fileDTO.setFile_name(file_name);
        fileDTO.setFile_type(file_type);
        fileDTO.setFile_url(file_url);
        fileDTO.setFile_extension(file_extension);
        fileDTO.setFile_sort(0);
        // 파일 저장
        String dir = getRootPath(null);
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(dir.concat(OS).concat(file_server_name));
        multipartFile.transferTo(file);

        return fileDTO;
	}
	
	public FileDTO deleteFile(FileDTO fileDTO) {
		// 파일 삭제 (서버에서)
        String filePath = getRootPath(fileDTO.getFile_server_name());
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        return fileDTO;
	}
	
}
