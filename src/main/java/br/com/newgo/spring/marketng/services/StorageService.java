package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.configs.StorageProperties;
import br.com.newgo.spring.marketng.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public String storeFile(MultipartFile file) throws IOException {
        // Limpa o nome do arquivo para garantir que não há caracteres ilegais no nome
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Verifica se o arquivo é válido (por exemplo, se não é nulo ou vazio)
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file: " + fileName);
            }
            // Verifica se o nome do arquivo é seguro
            // (e.g., se não contém caracteres ".." que poderiam levar a um caminho absoluto fora da pasta de armazenamento)
            if (fileName.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + fileName);
            }
            // Copia o conteúdo do arquivo para a pasta de armazenamento
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            // Retorna o nome do arquivo armazenado
            return fileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }
    }

}
